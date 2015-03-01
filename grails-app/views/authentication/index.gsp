<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <title>Welcome</title>
        <link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}">

        <asset:stylesheet src="bootstrap.css"/>
        <asset:stylesheet src="bootstrap-theme.css"/>
        <asset:stylesheet src="application.css"/>
    </head>

    <body>
        <div class="container">
            <div class="header thick-header">
                <h1 class="text-center text-muted">Chatroom</h1>
            </div>

            <g:form class="form-signin" action="login">
                <h2 class="form-signin-heading">Please sign in</h2>
                <g:textField id="username" class="form-control form-group" name="username" placeholder="Username" autofocus="true" />
                <g:submitButton class="btn btn-info btn-block" name="login" value="Login"/>
            </g:form>
        </div>
    </body>
</html>