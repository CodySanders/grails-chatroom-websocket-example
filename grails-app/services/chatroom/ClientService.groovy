package chatroom

import grails.transaction.Transactional

@Transactional
class ClientService {
    def getClient(String clientId) {
        Client.get(clientId)
    }

    def loginClient(String username) {
        assert username
        Client client = Client.findOrCreateByUsername(username)
        client.active = true
        client.save(flush: true)
    }

    def logoutClient(Client client) {
        assert client
        client.active = false
        client.save(flush: true)
    }
}
