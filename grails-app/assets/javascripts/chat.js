var Chat = function() {
    var init = function() {
        bindEnterKeyMessageSend();
        bindLogoutButton();
        subscribeWebSockets();
    };

    var subscribeWebSockets = function() {
        WS.setup(function() {
            WS.subscribe('/topic/chat/message/new', function(data) {
                addNewMessage(JSON.parse(data.body));
            });

            WS.subscribe('/topic/chat/user/login', function(data) {
                var user = JSON.parse(data.body);
                addUserToList(user);
                addNewAdminMessage(user.username +' has logged in.');
            });

            WS.subscribe('/topic/chat/user/logout', function(data) {
                var user = JSON.parse(data.body);
                removeUserFromList(user);
                addNewAdminMessage(user.username +' has logged out.');
            });
        });
    };

    var bindEnterKeyMessageSend = function() {
        $("#message").keyup(function(event) {
            if(event.which == 13) {
                WS.send('/ws/chat/message/new', { user: $.cookie('chatUsername'), message: $('#message').val() });
                $('#message').val('');
            }
        });
    };

    var bindLogoutButton = function() {
        $('#logout').click(function() {
            WS.closeConnection();
        });
    };

    var addNewMessage = function(messageJson) {
        drawMessage(messageJson.user, messageJson.message, false);
    };

    var addNewAdminMessage = function(message) {
        drawMessage('Admin', message, true);
    };

    var drawMessage = function(user, message, adminMessage) {
        var templateString = $('#messageTemplate').html();
        var messageBoard = $('#messageBoard');

        templateString = templateString.replace('__USER__', user);
        templateString = templateString.replace('__MESSAGE__', message);
        templateString = templateString.replace('__EM__', adminMessage ? '<em>' : '');
        templateString = templateString.replace('__EM_CLOSE__', adminMessage ? '</em>' : '');
        messageBoard.append(templateString);
        messageBoard.prop('scrollTop', messageBoard.prop('scrollHeight'));
    };

    var addUserToList = function(user) {
        var templateString = $('#userTemplate').html();

        templateString = templateString.replace('__USER__', user.username);
        templateString = templateString.replace('__ID__', user.userId);
        $('#userList').append(templateString);
    };

    var removeUserFromList = function(user) {
        $('#'+ user.userId).remove();
    };


    return {
        init: init
    };
}();

$(document).ready(function() {
    Chat.init();
    $('#message').focus();
});