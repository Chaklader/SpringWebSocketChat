<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" ng-app="springChat">
	<head>
	  <meta charset="utf-8" />
	  <title>Log In</title>
	  
	  <!-- Loading Bootstrap -->
		<link href="/static/lib/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">
	
	  <!-- Loading Flat UI -->
	  <link href="/static/lib/flat-ui/dist/css/flat-ui.css" rel="stylesheet">
	  
	  <link href="/static/css/chat.css" rel="stylesheet">
	</head>
	<body>
		<div class="login_page container">
	        <div class="login">
		        <div class="login-screen">
		          <div class="login-icon">
		            <h4>Welcome to <small>Spring WebSocket Chat</small></h4>
		          </div>
		
		          <div class="login-form">
		          	<p><small style="color:#666">Enter a nickname to join. If you want to become admin use the password 'rockandroll'</small></p>
		          	<form action="<c:url value="/login"></c:url>" method="post" role="form">
			            <div class="form-group">
			              <input type="text" class="form-control login-field" placeholder="Enter your name" name="username" />
			              <label class="login-field-icon fui-user" for="login-name"> Username</label>
			            </div>
			
			            <div class="form-group">
			              <input type="password" class="form-control login-field" name="password" />
			              <label class="login-field-icon fui-lock" for="login-pass">Ppassword </label>
			            </div>
			
						<input class="btn btn-primary btn-lg btn-block"  type="submit" value="Log In"/>
		            </form>
					  <br/>
					  <input class="btn btn-primary btn-lg btn-block"  type="button" onclick="signUp()" value="Sign Up"/>
		          </div>
		        </div>
		      </div>
	    </div>
	    <!-- /.container -->
	<script>
		function signUp () {
			window.location.href = '<c:url value="/sign_un"></c:url>';
		}
	</script>

		
	</body>
</html>