<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>db4o NerdDinner</title>


    <link rel="stylesheet" type="text/css" href="extjs/css/ext-all.css"/>
    <link rel="stylesheet" type="text/css" href="extjs/css/xtheme-gray.css"/>
    <link rel="stylesheet" type="text/css" href="default.css"/>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
    <script type="text/javascript" src="extjs/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="extjs/ext-all.js"></script>
    <script type="text/javascript" src="application.js"></script>
</head>
<body>
<div id="page">
    <div id="header">
        <div id="title">
            <h1><a href="/" title="Nerd Dinner" class="logo"></a></h1>
        </div>
        <div id="authcontainer">

            <c:if test="${!loggedIn}">
                <form action="openid/startAuth.html" method="post" id="loginForm">
                    <label for="openIdUrl">OpenID: </label>
                    <input id="openIdUrl" name="openIdUrl"/>
                    <input class="loginButton" type="submit" value="Login"/>
                </form>
            </c:if>
            <c:if test="${loggedIn}">
                Logged in as ${identity} <a href="/openid/logout.html">Log out</a>
            </c:if>
        </div>
        <div id="menucontainer">
            <ul id="menu">
                <li><a href="">Find Dinner</a></li>

                <li><a href="#host" id="hostButton">Host Dinner</a></li>


                <li class="last"><a href="">About</a></li>
            </ul>
        </div>

    </div>

    <div id="main">
        <div id="mapView"></div>
        <div id="application-container"></div>

    </div>
    <script type="text/javascript">
        dinnerApp = {

            <c:if test="${loggedIn}">
            logginState: true
            </c:if>
            <c:if test="${!loggedIn}">
            logginState: false
            </c:if>
        }
    </script>
</div>
</body>
</html>