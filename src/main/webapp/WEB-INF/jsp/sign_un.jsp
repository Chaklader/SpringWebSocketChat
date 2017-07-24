<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" ng-app="springChat">
	<head>
	  <meta charset="utf-8" />
	  <title>Sign UP</title>
	  
	  <!-- Loading Bootstrap -->
	  <link href="/static/lib/flat-ui/dist/css/vendor/bootstrap.min.css" rel="stylesheet">
	
	  <!-- Loading Flat UI -->
	  <link href="/static/lib/flat-ui/dist/css/flat-ui.css" rel="stylesheet">
	  
	  <link href="/static/css/chat.css" rel="stylesheet">
	</head>
	<body>
		<div class="container">
	        <div class="sign_un">
		        <div class="sign-un-screen">
		
		          <div class="login-form">
		          	<p><small style="color:#666">Please enter your nickname and password into the fields and click on the Sign Up button. </small></p>
		          	<form action="<c:url value="/sign_un"></c:url>" method="post" role="form">
			            <div class="form-group">
			              <input type="text" class="form-control login-field" placeholder="Enter your name" id="username" name="username" />
			              <label class="login-field-icon fui-user" for="username">Username</label>
			            </div>
			
			            <div class="form-group">
			              <input type="password" class="form-control login-field" id="password" name="password" />
			              <label class="login-field-icon fui-lock" for="password">Password </label>
			            </div>

			            <div class="form-group">
			              <input type="password" class="form-control login-field" id="confirm_password" name="confirm_password" />
			              <label class="login-field-icon fui-lock" for="confirm_password">Confirm Password </label>
			            </div>
			
						<input class="btn btn-primary btn-lg btn-block" type="submit" value="Sign Up"/>
		            </form>
		          </div>
		        </div>
		      </div>
	    </div>
	    <!-- /.container -->
	

		
	</body>
</html>