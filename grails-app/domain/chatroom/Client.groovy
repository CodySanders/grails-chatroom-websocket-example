package chatroom

class Client {
    String username
    Boolean active

    static constraints = {
        username nullable: false, blank: false
    }
}
