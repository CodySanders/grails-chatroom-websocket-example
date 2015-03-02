package chatroom

import org.codehaus.groovy.runtime.powerassert.PowerAssertionError
import spock.lang.Specification

class ClientServiceSpec extends Specification {
    def clientService

    def setup() {
    }

    def cleanup() {
    }

    void "test get client"() {
        given:
        Client client = new Client(username: 'joe', active: true).save(failOnError: true)

        when:
        Client result = clientService.getClient(client.id.toString())

        then:
        result == client
    }

    void "test login client saves and returns client"() {
        when:
        Client result = clientService.loginClient('joe')

        then:
        result.id
        result.username == 'joe'
        result.active
    }

    void "test login client throws exception for blank username"() {
        when:
        clientService.loginClient('')

        then:
        thrown(PowerAssertionError)
    }

    void "test login client throws exception for null username"() {
        when:
        clientService.loginClient(null)

        then:
        thrown(PowerAssertionError)
    }

    void "test logout client deactivates and saves client"() {
        when:
        Client result = clientService.logoutClient(new Client(username: 'joe'))

        then:
        result.id
        !result.active
    }

    void "test logout client throws exception for null client"() {
        when:
        clientService.logoutClient(null)

        then:
        thrown(PowerAssertionError)
    }
}
