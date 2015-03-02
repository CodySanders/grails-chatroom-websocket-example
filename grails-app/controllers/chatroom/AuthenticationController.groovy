package chatroom

class AuthenticationController {
    AuthenticationWebSocketService authenticationWebSocketService
    ClientService clientService
    CookieService cookieService

    def index() {
        String username = g.cookie(name: 'chatUsername')

        if(username && Client.findByUsernameAndActive(username, true))
            redirect(controller: 'chat', action: 'index')
        else
            render(view: 'index')
    }

    def login() {
        Client client = clientService.loginClient(params.username)

        if(client) {
            response.addCookie(cookieService.getRootPathSessionCookie('chatUsername', client.username))
            response.addCookie(cookieService.getRootPathSessionCookie('chatUserId', client.id.toString()))
            authenticationWebSocketService.notifyUserLoggedIn(client)

            redirect(controller: 'chat', action: 'index')
        } else {
            redirect(action: 'index')
        }
    }

    def logout() {
        Client client = clientService.getClient(g.cookie(name: 'chatUserId'))

        response.addCookie(cookieService.getRootPathExpiredCookie('chatUsername'))
        response.addCookie(cookieService.getRootPathExpiredCookie('chatUserId'))
        clientService.logoutClient(client)
        authenticationWebSocketService.notifyUserLoggedOut(client)

        redirect(action: 'index')
    }
}
