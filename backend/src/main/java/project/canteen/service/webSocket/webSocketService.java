package project.canteen.service.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import project.canteen.entity.auth.account;
import project.canteen.model.auth.ResponMessage;

@Service
public class webSocketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public void sendNotificationToUser(String username, String message) {
//        ResponMessage responMessage = new ResponMessage();
//        account account = new account();
//        account.setUsername(message);
//        responMessage.setData(account);
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications",message );
    }
    public void sendNotification(String message) {
        messagingTemplate.convertAndSend("/topic/public", message);
    }
}
