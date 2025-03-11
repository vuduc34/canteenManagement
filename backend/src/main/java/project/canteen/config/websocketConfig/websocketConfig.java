package project.canteen.config.websocketConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import project.canteen.config.jwtConfig.jwtProvider;

@Configuration
@EnableWebSocketMessageBroker
public class websocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private  webSocketAuthInterceptor webSocketAuthInterceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Endpoint WebSocket
                .setAllowedOriginPatterns("*") // Cho phép truy cập từ mọi nguồn
                .addInterceptors(webSocketAuthInterceptor);
//                .withSockJS(); // Hỗ trợ SockJS (fallback cho trình duyệt không hỗ trợ WebSocket)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Broker để gửi tin nhắn
        registry.setApplicationDestinationPrefixes("/app"); // Prefix cho các tin nhắn client gửi lên
        registry.setUserDestinationPrefix("/user"); // Định tuyến tin nhắn đến từng user
    }
}
