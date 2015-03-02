package chatroom

import grails.transaction.Transactional

import javax.servlet.http.Cookie

@Transactional
class CookieService {
    def getRootPathSessionCookie(String cookieKey, String cookieValue) {
        Cookie cookie = new Cookie(cookieKey, cookieValue)
        cookie.maxAge = -1
        cookie.path = '/'

        return cookie
    }

    def getRootPathExpiredCookie(String cookieKey) {
        Cookie cookie = new Cookie(cookieKey, '')
        cookie.maxAge = 0
        cookie.path = '/'

        return cookie
    }
}
