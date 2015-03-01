package chatroom

import grails.transaction.Transactional

@Transactional
class AuthenticationWebSocketService {
    def brokerMessagingTemplate

    def notifyUserLoggedIn(Client client) {
        brokerMessagingTemplate.convertAndSend("/topic/chat/user/login", [userId: client.id, username: client.username])
    }

    def notifyUserLoggedOut(Client client) {
        brokerMessagingTemplate.convertAndSend("/topic/chat/user/logout", [userId: client.id, username: client.username])
    }
}
