package chatroom

class ChatController {
    def index() {
        String username = g.cookie(name: 'chatUsername')

        if(username && Client.findByUsernameAndActive(username, true))
            render(view: 'index', model: [currentUsers: Client.findAllByActive(true)])
        else
            redirect(controller: 'authentication', action: 'index')
    }
}
