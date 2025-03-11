package project.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.canteen.common.constant;
import project.canteen.model.auth.ResponMessage;
import project.canteen.service.webSocket.webSocketService;

@RestController
@RequestMapping(constant.API.PREFIX)
public class TestController {
	@Autowired
	private webSocketService webSocketService;

	@GetMapping("/")
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponMessage test() {
		ResponMessage responMessage = new ResponMessage();
		responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
		responMessage.setMessage(constant.MESSAGE.SUCCESS);
		responMessage.setData("ROLE_USER");
		return responMessage;
	}

	@GetMapping("/abc")
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponMessage testAdmin() {
		ResponMessage responMessage = new ResponMessage();
		responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
		responMessage.setMessage(constant.MESSAGE.SUCCESS);
		responMessage.setData("ROLE_ADMIN");
		return responMessage;
	}
	@GetMapping("/test/ws")
	@ResponseBody
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponMessage testWebSocket(String username, String message) {
		ResponMessage responMessage = new ResponMessage();
		try{
			webSocketService.sendNotificationToUser(username,message);
			responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
			responMessage.setMessage(constant.MESSAGE.SUCCESS);
			responMessage.setData(message);
		}
		catch (Exception e) {
			responMessage.setResultCode(constant.RESULT_CODE.ERROR);
			responMessage.setMessage(constant.MESSAGE.ERROR);
			responMessage.setData(e.getMessage());
		}
		return responMessage;
	}
	@GetMapping("/test/ws/br")
	@ResponseBody
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponMessage testWebSocketBr(String message) {
		ResponMessage responMessage = new ResponMessage();
		try{
			webSocketService.sendNotification(message);
			responMessage.setResultCode(constant.RESULT_CODE.SUCCESS);
			responMessage.setMessage(constant.MESSAGE.SUCCESS);
			responMessage.setData(message);
		}
		catch (Exception e) {
			responMessage.setResultCode(constant.RESULT_CODE.ERROR);
			responMessage.setMessage(constant.MESSAGE.ERROR);
			responMessage.setData(e.getMessage());
		}
		return responMessage;
	}

}
