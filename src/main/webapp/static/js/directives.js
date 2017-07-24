/* Directives */

var directives = angular.module('springChat.directives', []);
directives.directive('printMessage', function () {
	    return {
	    	restrict: 'A',
	        template: '<span ng-mouseover="hoverIn()" ng-mouseleave="hoverOut()"><span ng-show="message.priv">[private] </span><strong>{{message.username}}<span ng-show="message.to"> -> {{message.to}}</span>:</strong> {{message.message}} </span><span ng-show="hoverEdit" class="message_time">{{formatMessageTime(message.dateTime)}}</span><br/>'
	       
	    };
});
directives.directive('printChatMessage', function () {
	    return {
	    	restrict: 'A',
	        template: '<span ng-mouseover="hoverIn()" ng-mouseleave="hoverOut()"><strong>{{message.username}} :</strong> {{message.message}}' +
				'<span ng-show="message.fileName">: <a target="_blank" href="/download?file={{encodeURIComponent(message.fileName)}}">{{message.fileName}}</a></span>' +
				'</span><span ng-show="hoverEdit" class="message_time">{{formatMessageTime(message.dateTime)}}</span><br/>'

	    };
});