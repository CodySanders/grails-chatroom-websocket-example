package chatroom

import spock.lang.*

import javax.servlet.http.Cookie

class CookieServiceSpec extends Specification {
    def cookieService

    def setup() {
    }

    def cleanup() {
    }

    void "test get cookie with path set to root and expire time to session"() {
        when:
        Cookie result = cookieService.getRootPathSessionCookie('user', 'joe')

        then:
        result.name == 'user'
        result.value == 'joe'
        result.maxAge == -1
        result.path == '/'
    }

    void "test get cookie with path set to root and expire time to now"() {
        when:
        Cookie result = cookieService.getRootPathExpiredCookie('user')

        then:
        result.name == 'user'
        result.value == ''
        result.maxAge == 0
        result.path == '/'
    }
}
