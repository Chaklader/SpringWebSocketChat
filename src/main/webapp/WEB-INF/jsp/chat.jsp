<!DOCTYPE html>
<html lang="en" ng-app="springChat">
	<head>
	  <meta charset="utf-8" />
	  <title>Spring WebSocket Chat</title>
	  <meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	  <link href="/static/lib/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">
	  <link href="/static/lib/flat-ui/dist/css/flat-ui.css" rel="stylesheet">
	  <link href="/static/lib/angularjs-toaster/toaster.css" rel="stylesheet">
	  <link href="/static/lib/ng-popup/ngPopup.css" rel="stylesheet">
      
	  <link href="/static/css/chat.css" rel="stylesheet">
	</head>
	<body>
		
		<div class="container" ng-controller="ChatController">
			<toaster-container></toaster-container>

			<div id="chatMessengerWindows"></div>
			<div class="row">
				<nav class="navbar navbar-inverse navbar-embossed" role="navigation">
		            <div class="collapse navbar-collapse" id="navbar-collapse-01">
		              <h1>Spring WebSocket Chat</h1>
		              <ul class="nav navbar-nav navbar-right">           
		              	<%--<li><a href="websocket" target="_blank">Stats</a></li>--%>
		                <li><a href="/security_logout">Logout ({{username}})</a></li>
		               </ul>
		            </div><!-- /.navbar-collapse -->
		          </nav><!-- /navbar -->
			</div>
	        <div class="row">
				<div class="col-xs-8 chat-box">
					<div class="forum-chat-messages">
							<h4>Messages</h4>
							<div ng-repeat="message in messages">
								<small print-message></small>
							</div>
					</div>
					<div>
						<div class="form-group">
							<span><small>You will send this message to <strong>{{sendTo}}</strong> (click a participant name to send a private message)</small></span>
							<input id="newMessageInput" type="text" class="form-control" placeholder="Write your message and hit enter..." ng-model="newMessage" ng-keyup="$event.keyCode == 13 ? sendMessage() : startTyping()"/>
						</div>
					</div>
				</div>
        		<div class="participants_box">
        			<%--<h5>Participants [{{participants.length}}]</h5>--%>
        			<div class="share">
	        			<ul ng-repeat="participant in filteredParticipants | orderBy:['-online','loginDatetime'] | limitTo:maxParticipantsInList">
	        				<li class="participant_line">
								<span class="part_info">
									<span class="input-icon fui-new" ng-show="participant.typing"></span>
									<span class="input-icon fui-user" ng-show="!participant.typing"></span>
									<a href="" ng-click="openChat(participant.username)">{{participant.username}}</a>
								</span>
								<span class="part_online">
	        						<span class="online_mark" ng-show="participant.online"></span>
								</span>
	        				</li>
	        			</ul>
						<input type="text" class="form-control inline-control" ng-model="nameFilter" placeholder="Search"
							   ng-change="filterParticipants()"/>
        			</div>
        		</div>
        	</div>

	    </div>
	    <!-- /.container -->
	
		<!-- 3rd party -->
		<script src="/static/lib/jquery/dist/jquery.min.js"></script>
		<script src="/static/lib/angular/angular.min.js"></script>
	    <script src="/static/lib/angular-animate/angular-animate.min.js"></script>
	    <script src="/static/lib/angularjs-toaster/toaster.js"></script>
	    <script src="/static/lib/angularjs-scroll-glue/src/scrollglue.js"></script>
	    <script src="/static/lib/sockjs/sockjs.min.js"></script>
	    <script src="/static/lib/stomp/lib/stomp.min.js"></script>
	    <script src="/static/lib/ng-popup/ngPopup.js"></script>
		<script src="/static/lib/bootstrap/3.2.0/js/bootstrap.min.js"></script>
		
		<!-- App -->
		<script src="/static/js/app.js"></script>
		<script src="/static/js/controllers.js"></script>
		<script src="/static/js/services.js"></script>
		<script src="/static/js/directives.js"></script>
	</body>
</html>