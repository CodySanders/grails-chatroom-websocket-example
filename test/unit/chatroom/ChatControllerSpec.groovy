package chatroom

import grails.plugin.cookie.CookieService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ChatController)
@Mock(Client)
class ChatControllerSpec extends Specification {

    def setup() {
        controller.cookieService = Mock(CookieService)
    }

    def cleanup() {
    }

    void "test index is rendered when user is active in db with valid cookie"() {
        given:
        new Client(username: 'joe', active: true).save(failOnError: true)
        new Client(username: 'userNotActive', active: false).save(failOnError: true)

        when:
        controller.index()

        then:
        1 * controller.cookieService.getCookie('chatUsername') >> 'joe'
        view == '/chat/index'
        model.currentUsers.size() == 1
    }

    void "test redirected to authentication index when user is active in db but no valid cookie"() {
        given:
        new Client(username: 'joe', active: true).save(failOnError: true)

        when:
        controller.index()

        then:
        1 * controller.cookieService.getCookie('chatUsername') >> null
        response.redirectedUrl == '/'
    }

    void "test redirected to authentication index when user is not active in db but has valid cookie"() {
        given:
        new Client(username: 'joe', active: false).save(failOnError: true)

        when:
        controller.index()

        then:
        1 * controller.cookieService.getCookie('chatUsername') >> 'joe'
        response.redirectedUrl == '/'
    }
}
