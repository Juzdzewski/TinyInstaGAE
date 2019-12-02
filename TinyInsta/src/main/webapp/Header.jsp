<%@ page import="java.security.Principal" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.logging.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.google.gae.TinyInsta.*" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>

<!doctype html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, maximum-scale=1, user-scalable=0">
  <title>Tiny Insta</title>
  <link rel="stylesheet" href="style.css">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>


<%
DateFormat df = new SimpleDateFormat("MM.d.yyyy");
UserService userService = UserServiceFactory.getUserService();         	          	
String requestUri = request.getRequestURI();
Principal userPrincipal = request.getUserPrincipal();

String url, urlLinkText;
Boolean userFlag = false;

if (userPrincipal == null) { 
	url = userService.createLoginURL(requestUri);	
  	userFlag = false;
} 
else { 
	url = userService.createLogoutURL("/");	
  	userFlag = true;
	Entity user = TinyInstaEndPoint.getUser(userService.getCurrentUser().getEmail());
	if(user == null)
	{
		TinyInstaEndPoint.saveUser(userService.getCurrentUser().getEmail(), "John");
	}
   }
 %>

  <!-- Header -->

  <header>
    <div class="container">
      <div class="logo-box">
        <div class="logo">
          <i class="fa fa-paw"></i>
        </div>
        <h1>Tiny Insta</h1>
        <span>Vos plus belles images !</span>
      </div>

      <!-- Navigation -->
	
      <div class="user-ctrl">
        <nav class="main-nav">
          <ul>
            <li><a href="/">Home</a></li>
            <% if (userFlag) { %>            
            <li>
              <a href="#">Welcome <%= userService.getCurrentUser().getNickname() %> <i class="fa fa-chevron-down"></i></a>
              <ul>
                <li><a href="/Profile.jsp">My Profile</a></li>
                <li><a href="<%= url %>">Logout</a></li>
              </ul>
            </li>
            <% } else { %>
            <li>
              <a href="<%= url %>">Login</a>
            </li>
            <% } %>     
          </ul>
        </nav>
      </div>
    </div>
 
  </header>