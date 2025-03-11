package project.canteen.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class imageService {

    @Value("${image.upload-dir}")
    private String uploadDir;
    public String saveImage(MultipartFile file) throws IOException {
        // Kiểm tra thư mục tồn tại, nếu không thì tạo
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Đổi tên file tránh trùng lặp
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + File.separator + filename;

        // Lưu file vào thư mục
        file.transferTo(new File(filePath));

        return filename; // Trả về tên file để truy xuất sau này
    }
    public boolean deleteImage(String filename) {
        String filePath = uploadDir + File.separator + filename;
        File file = new File(filePath);

        if (file.exists()) {
            return file.delete(); // Xóa file và trả về true nếu thành công, false nếu thất bại
        }

        return false; // Trả về false nếu file không tồn tại
    }

    public byte[] getImage(String filename) throws IOException {
        String filePath = uploadDir + File.separator + filename;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filename);
        }
        return Files.readAllBytes(Paths.get(filePath));
    }
}
