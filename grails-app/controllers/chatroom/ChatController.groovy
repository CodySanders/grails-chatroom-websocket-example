package chatroom

import grails.plugin.cookie.CookieService

class ChatController {
    CookieService cookieService

    def index() {
        String username = cookieService.getCookie('chatUsername')

        if(username && Client.findByUsernameAndActive(username, true))
            render(view: 'index', model: [currentUsers: Client.findAllByActive(true)])
        else
            redirect(controller: 'authentication', action: 'index')
    }
}
