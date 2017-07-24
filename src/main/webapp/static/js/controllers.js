'use strict';
/* Controllers */

var app = angular.module('springChat.controllers', ['toaster']);
	app.controller('ChatController', ['$scope', '$compile', '$location', '$interval', 'toaster', 'ChatSocket', 'convRepo', 'participantsRepo',
		function($scope, $compile, $location, $interval, toaster, chatSocket, convRepo, participantsRepo) {

		var typing = undefined;

		$scope.username     = '';
		$scope.participants = [];
		$scope.messages     = [];
		$scope.newMessage   = '';
		$scope.nameFilter = '';
		$scope.filteredParticipants = [];
		$scope.maxParticipantsInList = 10;
		$scope.chatMessengers = {};

		$scope.init = function() {

		}

		$scope.openChat = function(username) {
			var participants = $scope.username + "," + username;
			var conversation = convRepo.getConversationByParticipantName(participants);
			if (conversation == undefined) {
				var conversation = convRepo.createConversation(participants);
				$scope.openNewChatMessenger(conversation);
			} else {
				conversation.chatMessenger.isShow = true;
			}
		};

		$scope.openNewChatMessenger = function (conversation) {
			var chatMessenger = {
				width: 300,
				height:400,
				title : conversation.participants.join(),
				templateUrl:"/static/views/chatMessenger.html",
				resizable:true,
				draggable:true,
				position: { top : 50, left : 50},
				onOpen: function(){
					/*Some Logic...*/
				},
				conversationKey : conversation.conversationKey
			}
			conversation.chatMessenger = chatMessenger;
			$scope.chatMessengers[conversation.conversationKey] = chatMessenger;

			var html = "<ng-pop-up option='chatMessengers[\"" + conversation.conversationKey + "\"]'></ng-pop-up>";
			var compiledElement = $compile(html)($scope);

			var pageElement = angular.element(document.getElementById("chatMessengerWindows"));
			pageElement.append(compiledElement);
		};

		$scope.filterParticipants = function() {
			$scope.filteredParticipants = participantsRepo.getParticipants().filter(function(user) {
				return user.username.indexOf($scope.nameFilter) !== -1 &&
					user.username !== $scope.username;
			});

			var onlineParts = 0;
			for(var index in $scope.filteredParticipants) {
				if($scope.filteredParticipants[index].online) {
					onlineParts++;
				}
			}

		};

		$scope.sendMessage = function() {
			var destination = "/app/chat.message";
			chatSocket.send(destination, {}, JSON.stringify({message: $scope.newMessage}));
			$scope.newMessage = '';
		};

		var initStompClient = function() {
			chatSocket.init('/ws');
			
			chatSocket.connect(function(frame) {
				  
				$scope.username = frame.headers['user-name'];

				chatSocket.subscribe("/app/chat.participants", function(message) {
					$scope.participants = JSON.parse(message.body);
					participantsRepo.setParticipants( $scope.participants );
					$scope.filterParticipants();
				});
				  
				chatSocket.subscribe("/topic/chat.login", function(message) {
					var event = JSON.parse(message.body);
					var username = event.username;
					var loginDatetime = event.time;
					for(var index in participantsRepo.getParticipants()) {
						var participant = participantsRepo.getParticipant(index);
						if(participant.username == username) {
							participant.online = true;
							participant.loginDatetime = loginDatetime;
							$scope.filterParticipants();
							return;
						}
					}
					participantsRepo.getParticipants().unshift({username: JSON.parse(message.body).username, typing : false});
					$scope.filterParticipants();
				});
		        	 
				chatSocket.subscribe("/topic/chat.logout", function(message) {
					var username = JSON.parse(message.body).username;
					for(var index in participantsRepo.getParticipants()) {
						var participant = participantsRepo.getParticipant(index);
						if (participant.username == username) {
							participant.online = false;
						}
					}
					$scope.filterParticipants();
		        });
/*
				chatSocket.subscribe("/topic/chat.typing", function(message) {
					var parsed = JSON.parse(message.body);
					if(parsed.username == $scope.username) return;
				  					
					for(var index in participantsRepo.getParticipants()) {
						var participant = participantsRepo.getParticipant(index);
						  
						if(participant.username == parsed.username) {
							participant.typing = parsed.typing;
						}
				  	} 
				});*/

				chatSocket.subscribe("/topic/chat.message", function(message) {
					var parsed = JSON.parse(message.body);
					parsed.dateTime = new Date(parsed.dateTime);
					$scope.messages.unshift(parsed);
		        });
				  
				chatSocket.subscribe("/user/exchange/amq.direct/chat.message", function(message) {
					var parsed = JSON.parse(message.body);
					parsed.priv = true;
					parsed.dateTime = new Date(parsed.dateTime);

					var conversation = convRepo.getConversationById(parsed.conversationKey);
					if (conversation == undefined) {
						conversation = convRepo.createConversation(parsed.participants, parsed.conversationKey);
						conversation.messages.unshift(parsed);
						$scope.openNewChatMessenger(conversation);
					} else {
						conversation.messages.unshift(parsed);
						conversation.chatMessenger.isShow = true;
					}
		        });
				  
				chatSocket.subscribe("/user/exchange/amq.direct/errors", function(message) {
					toaster.pop('error', "Error", message.body);
		        });
		          
			}, function(error) {
				toaster.pop('error', 'Error', 'Connection error ' + error);
				
		    });
		};
		$scope.hoverIn = function(){
			this.hoverEdit = true;
		};

		$scope.hoverOut = function(){
			this.hoverEdit = false;
		};

		$scope.formatMessageTime = function(date){
			return formatMessageTime(date);
		};
		initStompClient();
		$scope.init();
	}]);

app.controller('ChatMessengerController', ['$scope', '$interval', 'toaster', 'ChatSocket', 'convRepo', 'participantsRepo',
	function($scope, $interval, toaster, chatSocket, convRepo, participantsRepo) {
		$scope.showAttach = false;
		$scope.username     = '';
		$scope.messages     = [];
		$scope.newMessage   = '';
		$scope.nameFilter = '';
		$scope.friendsToAdd = [];
		$scope.filteredFriends = [];
		$scope.maxFriendsInList = 5;
		$scope.showFriendsSearch = false;

		$scope.conversation;

		$scope.init = function() {
			$scope.conversation = convRepo.getConversationById( $scope.option.conversationKey);
			$scope.messages = $scope.conversation.messages;
		}

		$scope.sendMessage = function() {
			var participants = $scope.conversation.participants.join();
			var destination = "/app/chat.private." + participants + "." + $scope.option.conversationKey;
			$scope.messages.unshift({
				message: $scope.newMessage,
				username: 'you',
				priv: true,
				to: participants,
				dateTime : new Date()
			});

			chatSocket.send(destination, {}, JSON.stringify({message: $scope.newMessage}));
			$scope.newMessage = '';
		};

		$scope.sendFileMessage = function(fileName) {
			var participants = $scope.conversation.participants.join();
			var destination = "/app/chat.private." + participants + "." + $scope.option.conversationKey;
			$scope.messages.unshift({
				message: 'Sent file',
				username: 'you',
				priv: true,
				fileName: fileName,
				to: participants,
				dateTime : new Date()
			});

			chatSocket.send(destination, {}, JSON.stringify({message:  'Sent file', fileName: fileName}));
		};

		$scope.encodeURIComponent = function (url) {
			return encodeURIComponent(url)
		}

		$scope.startTyping = function() {
		};

		$scope.addMorFriends = function () {
			$scope.showFriendsSearch = true;
		}

		$scope.doneAddFriend = function () {
			$scope.showFriendsSearch = false;
			$scope.nameFilter = '';
			$scope.filteredFriends = [];

			for (var index in $scope.friendsToAdd) {
				var friend = $scope.friendsToAdd[index];
				if (!$scope.conversation.participants.includes(friend)) {
					$scope.conversation.participants.unshift(friend)
				}
			}
			$scope.option.title = $scope.conversation.participants.join();
			$scope.friendsToAdd = [];
		}

		$scope.addFriend = function (friendName) {
			if ($scope.friendsToAdd.includes(friendName)) {
				return;
			}
			$scope.friendsToAdd[$scope.friendsToAdd.length] = friendName;
			$scope.filterFriends();
		}

		$scope.removeFriend = function (friendName) {
			$scope.friendsToAdd = $scope.friendsToAdd.filter(function(name) {
				return name !== friendName;
			});
			$scope.filterFriends();
		}

		$scope.filterFriends = function() {
			$scope.filteredFriends = participantsRepo.getParticipants().filter(function(user) {
				var name = user.username;
				return name.indexOf($scope.nameFilter) !== -1
					&& !$scope.conversation.participants.includes(name)
					&& !$scope.friendsToAdd.includes(name)
					&& name !== $scope.username;
			});
		};
		$scope.hoverIn = function(){
			this.hoverEdit = true;
		};

		$scope.hoverOut = function(){
			this.hoverEdit = false;
		};

		$scope.formatMessageTime = function(date){
			return formatMessageTime(date);
		};

		$scope.fileNameChanged = function(target){
			var files = target.files
			if (files.length == 0) {
				return;
			}

			var oMyForm = new FormData();
			var fileToSend = files[0];
			oMyForm.append("file", fileToSend);
			var fileName = fileToSend.name;

			$.ajax({
				url: '/upload',
				data: oMyForm,
				dataType: 'text',
				processData: false,
				contentType: false,
				type: 'POST',
				success: function(data){
					//	$('#result').html(data);
				}
			});

			$interval(new function() {
				$scope.showAttach = false;
				$scope.sendFileMessage(fileName);
			}, 100, 1);
		}

		$scope.init();
	}]);

springChat.value('participantsRepo', {
	participants : [],

	setParticipants : function(parts) {
		this.participants = parts;
	},
	getParticipants : function() {
		return this.participants;
	},
	getParticipant : function(index) {
		return this.participants[index];
	}

})

springChat.value('convRepo', {
	conversations : [],
	getConversationById : function(convId) {
		for(var index in this.conversations) {
			var conversation = this.conversations[index];
			if (convId == conversation.conversationKey) {
				return conversation;
			}
		}
		return null;
	},

	getConversationByParticipantName : function(name) {
		for(var index in this.conversations) {
			var conversation = this.conversations[index];
			var parts = conversation.participants.join();
			if (parts == name) {
				return conversation;
			}
		}
		return null;
	},

	createConversation : function(parts, conversationKey) {
		var conversation =  {
			conversationKey : conversationKey == undefined ? this.generateConversationKey() : conversationKey,
			participants : parts.split(','),
			messages : []
		};
		this.conversations.unshift(conversation);
		return conversation;
	},

	generateConversationKey : function() {
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		});
	}
})

function formatMessageTime(date){
	var monthNames = [
		"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
		"Aug", "Sep", "Oct", "Nov", "Dec"
	];
	var currentDate = new Date();
	var day = date.getDate();
	var monthIndex = date.getMonth();
	var year = date.getFullYear();

	if (year == currentDate.getFullYear() && monthIndex == currentDate.getMonth() && day == currentDate.getDate()) {
		return formatAMPM(date);
	} else {
		return day + '-' + monthNames[monthIndex];
	}
};

function formatAMPM(date) {
	var hours = date.getHours();
	var minutes = date.getMinutes();
	var ampm = hours >= 12 ? 'pm' : 'am';
	hours = hours % 12;
	hours = hours ? hours : 12; // the hour '0' should be '12'
	minutes = minutes < 10 ? '0'+minutes : minutes;
	var strTime = hours + ':' + minutes + ' ' + ampm;
	return strTime;
}

function format24(date) {
	var hours = date.getHours();
	var minutes = date.getMinutes()
	return hours + ':' + minutes;
}