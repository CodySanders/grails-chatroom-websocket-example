package chatroom

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import javax.servlet.http.Cookie

@TestFor(AuthenticationController)
@Mock(Client)
class AuthenticationControllerSpec extends Specification {

    def setup() {
        controller.authenticationWebSocketService = Mock(AuthenticationWebSocketService)
        controller.clientService = Mock(ClientService)
        controller.cookieService = Spy(CookieService)
    }

    def cleanup() {
    }

    void "test redirected to chat index when user is active in db with valid cookie"() {
        given:
        request.setCookies(new Cookie('chatUsername', 'joe'))
        new Client(username: 'joe', active: true).save(failOnError: true)

        when:
        controller.index()

        then:
        response.redirectedUrl == '/chat'
    }

    void "test index rendered when user is active in db but no valid cookie"() {
        given:
        new Client(username: 'joe', active: true).save(failOnError: true)

        when:
        controller.index()

        then:
        view == '/authentication/index'
    }

    void "test index rendered when user is not active in db but has valid cookie"() {
        given:
        request.setCookies(new Cookie('chatUsername', 'joe'))
        new Client(username: 'joe', active: false).save(failOnError: true)

        when:
        controller.index()

        then:
        view == '/authentication/index'
    }

    void "test on login user is saved to db, cookies are set, and redirected to chat index"() {
        given:
        params.username = 'joe'

        Client client = new Client(username: 'joe')
        Cookie usernameCookie = new Cookie('chatUsername', 'joe')
        Cookie userIdCookie = new Cookie('chatUserId', '1')

        when:
        controller.login()

        then:
        1 * controller.clientService.loginClient('joe') >> client
        1 * controller.cookieService.getRootPathSessionCookie('chatUsername', 'joe') >> usernameCookie
        1 * controller.cookieService.getRootPathSessionCookie('chatUserId', 'null') >> userIdCookie
        1 * controller.authenticationWebSocketService.notifyUserLoggedIn(client)
        response.getCookie('chatUsername') == usernameCookie
        response.getCookie('chatUserId') == userIdCookie
        response.redirectedUrl == '/chat'
    }

    void "test on login if user is not saved to db, redirect to index to try again"() {
        given:
        params.username = ''

        when:
        controller.login()

        then:
        1 * controller.clientService.loginClient('') >> null
        0 * controller.cookieService.getRootPathSessionCookie(_, _)
        0 * controller.cookieService.getRootPathSessionCookie(_, _)
        0 * controller.authenticationWebSocketService.notifyUserLoggedIn(_)
        !response.getCookie('chatUsername')
        !response.getCookie('chatUserId')
        response.redirectedUrl == '/'
    }

    void "test logout if user is in db, deactivate and remove cookies"() {
        given:
        request.setCookies(new Cookie('chatUserId', '1'))

        Client client = new Client(id: 1, username: 'joe')
        Cookie usernameCookie = new Cookie('chatUsername', '')
        Cookie userIdCookie = new Cookie('chatUserId', '')

        when:
        controller.logout()

        then:
        1 * controller.clientService.getClient('1') >> client
        1 * controller.cookieService.getRootPathExpiredCookie('chatUsername') >> usernameCookie
        1 * controller.cookieService.getRootPathExpiredCookie('chatUserId') >> userIdCookie
        1 * controller.clientService.logoutClient(client)
        1 * controller.authenticationWebSocketService.notifyUserLoggedOut(client)
        response.getCookie('chatUsername') == usernameCookie
        response.getCookie('chatUserId') == userIdCookie
        response.redirectedUrl == '/'
    }
}
