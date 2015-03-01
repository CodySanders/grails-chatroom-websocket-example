<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <title>Chatroom</title>
        <link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}">

        <asset:javascript src="jquery.js"/>
        <asset:javascript src="spring-websocket" />
        <asset:javascript src="vendor/jquery.cookie.js"/>
        <asset:javascript src="vendor/bootstrap.js"/>
        <asset:javascript src="websocket.js"/>
        <asset:javascript src="chat.js"/>

        <asset:stylesheet src="bootstrap.css"/>
        <asset:stylesheet src="bootstrap-theme.css"/>
        <asset:stylesheet src="application.css"/>
    </head>

    <body>
        <div class="container">
            <div class="header">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li id="logout" role="presentation" class="active">
                            <g:link id="logout" controller="authentication" action="logout">LOGOUT</g:link>
                        </li>
                    </ul>
                </nav>
                <h1 class="text-center text-muted">Chatroom</h1>
            </div>
            <br/><br/>

            <!-- Messages -->
            <div class="col-md-9">
                <div class="panel panel-info ">
                    <div class="panel-heading">
                        MESSAGES
                    </div>
                    <div class="panel-body">
                        <ul id="messageBoard" class="media-list full-height-scrollable"></ul>
                    </div>
                    <div class="panel-footer bottom">
                        %{--<div class="input-group">--}%
                        <input type="text" id="message" class="form-control" placeholder="Enter Message" />
                            %{--<span class="input-group-btn">--}%
                                %{--<button id="sendButton" class="btn btn-info" type="button">SEND</button>--}%
                            %{--</span>--}%
                        %{--</div>--}%
                    </div>
                </div>
            </div>
            <!-- /Messages -->

            <!-- Users -->
            <div class="col-md-3">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        ONLINE USERS
                    </div>
                    <div class="panel-body">
                        <ul id="userList" class="media-list full-height-scrollable">
                            <g:each var="user" in="${currentUsers}">
                                <li id="${user.id}" class="media">
                                    <div class="media-body" >
                                        <h4>${user.username}</h4>
                                    </div>
                                </li>
                            </g:each>
                        </ul>
                    </div>
                </div>
            </div>
            <!-- /Users -->

            <!-- Reusable Template For Messages -->
            <div id="messageTemplate" class="hidden">
                <li class="media">
                    <div class="media-body" >
                        __EM__ __USER__: __MESSAGE__ __EM_CLOSE__
                    </div>
                </li>
            </div>
            <!---------------------------------->

            <!-- Reusable Template For Users -->
            <div id="userTemplate" class="hidden">
                <li id="__ID__" class="media">
                    <div class="media-body" >
                        <h4>__USER__</h4>
                    </div>
                </li>
            </div>
            <!---------------------------------->
        </div>  <!-- /container -->
    </body>
</html>