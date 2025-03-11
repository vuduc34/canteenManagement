package project.canteen.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.service.auth.imageService;

import java.io.IOException;

@RestController
@RequestMapping(constant.API.PREFIX_AUTH)
public class imageController {
    @Autowired
    private imageService imageService;
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponMessage uploadImage(@RequestParam("file") MultipartFile file) {
        ResponMessage responMessage = new ResponMessage();
        try {
            String filename = imageService.saveImage(file);
            responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
            responMessage.setMessage(constant.MESSAGE.SUCCESS);
            responMessage.setData(filename);
            return responMessage;
        } catch ( IOException e) {
            responMessage.setResultCode(constant.RESULT_CODE.ERROR);
            responMessage.setMessage(constant.MESSAGE.ERROR);
            responMessage.setData(e.getMessage());
            return responMessage;
        }
    }

    // API lấy ảnh theo tên file
    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            byte[] image = imageService.getImage(filename);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
