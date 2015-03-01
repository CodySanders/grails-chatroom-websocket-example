var WS = function() {
    $(window).unload(function() {
        closeConnection();
    });

    var client = undefined;
    var instanceOf = function() {
        if(typeof client === 'undefined')
            console.error("setup must be called to get a valid client.");
        else
            return client;
    };

    var getClient = function(callback) {
        if(typeof client === 'undefined') {
            if(window.location.host.indexOf('localhost') > -1)
                client = Stomp.over(new SockJS(window.location.protocol + "//" + window.location.host + "/chatroom/stomp"));
            else
                client = Stomp.over(new SockJS(window.location.protocol + "//" + window.location.host + "/stomp"));

            callback();
        } else {
            callback();
        }
    };

    var isClientConnected = false;

    var getConnection = function(callback) {
        if(!isClientConnected) {
            instanceOf().connect({}, function() {
                isClientConnected = true;
                callback();
            });
        } else {
            callback();
        }
    };

    var closeConnection = function() {
        instanceOf().disconnect();
    };

    var send = function(url, json) {
        instanceOf().send(url, {}, JSON.stringify(json));
    };

    var setup = function(callback) {
        getClient(function() {
            getConnection(function() {
                if (typeof callback === "function")
                    callback();
            });
        });
    };

    var subscribe = function(url, callback) {
        instanceOf().subscribe(url, callback);
    };

    return {
        closeConnection: closeConnection,
        send: send,
        setup: setup,
        subscribe: subscribe
    };
}();
