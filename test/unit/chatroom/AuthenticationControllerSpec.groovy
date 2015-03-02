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

        when:
        controller.login()

        then:
        1 * controller.authenticationWebSocketService.notifyUserLoggedIn(_)
        response.getCookie('chatUsername').value == 'joe'
        response.getCookie('chatUserId').value == '1'
        response.redirectedUrl == '/chat'
    }

    void "test on login if user is not saved to db, redirect to index to try again"() {
        given:
        params.username = ''

        when:
        controller.login()

        then:
        0 * controller.authenticationWebSocketService.notifyUserLoggedIn(_)
        !response.getCookie('chatUsername')
        !response.getCookie('chatUserId')
        response.redirectedUrl == '/'
    }

    void "test logout if user is in db, deactivate and remove cookies"() {
        given:
        request.setCookies(new Cookie('chatUserId', '1'))
        Client client = new Client(id: 1, username: 'joe', active: true).save(failOnError: true)

        when:
        controller.logout()

        then:
        1 * controller.authenticationWebSocketService.notifyUserLoggedOut(client)
        !response.getCookie('chatUsername').maxAge
        !response.getCookie('chatUserId').maxAge
        !client.active
        response.redirectedUrl == '/'
    }
}
