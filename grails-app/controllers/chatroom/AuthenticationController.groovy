package chatroom

import grails.plugin.cookie.CookieService

import javax.servlet.http.Cookie

class AuthenticationController {
    AuthenticationWebSocketService authenticationWebSocketService
    CookieService cookieService

    def index() {
        String username = cookieService.getCookie('chatUsername')

        if(username && Client.findByUsernameAndActive(username, true))
            redirect(controller: 'chat', action: 'index')
        else
            render(view: 'index')
    }

    def login() {
        Client client = Client.findOrCreateByUsername(params.username)
        client.active = true
        client = client.save(flush: true)

        if(client) {
            Cookie cookie = new Cookie("chatUsername", client.username)
            cookie.maxAge = -1
            cookie.path = '/'
            response.addCookie(cookie)

            cookie = new Cookie("chatUserId", client.id.toString())
            cookie.maxAge = -1
            cookie.path = '/'
            response.addCookie(cookie)

            authenticationWebSocketService.notifyUserLoggedIn(client)

            redirect(controller: 'chat', action: 'index')
        } else {
            redirect(action: 'index')
        }
    }

    def logout() {
        Client client = Client.get(cookieService.getCookie('chatUserId'))

        cookieService.deleteCookie('chatUsername')
        cookieService.deleteCookie('chatUserId')
        authenticationWebSocketService.notifyUserLoggedOut(client)

        if(client) {
            client.active = false
            client.save(flush: true)
        }

        redirect(action: 'index')
    }
}
