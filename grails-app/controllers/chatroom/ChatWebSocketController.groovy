package chatroom

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo

class ChatWebSocketController {
    @MessageMapping("/chat/message/new")
    @SendTo("/topic/chat/message/new")
    protected sendMessage(Map messageJson) {
        return messageJson
    }
}
