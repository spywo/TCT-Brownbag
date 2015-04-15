(function() {
	var MessageClientParent = function() {
		var stompClient;

		var connect = function() {
			var socket = new SockJS('/community/portfolio');
			stompClient = Stomp.over(socket);

			stompClient.connect({}, function(frame) {
				initSubscribe();
			}, function(error) {
				// popup connection issue?
			});
		}

		var initSubscribe = function() {
			
			var handler = function(calResult) {
				var response = JSON.parse(calResult.body);
				if(response.status == "Error") {
					// popup
				} else {
					UIManager.activePage($("#homePage"));					
				}
			
				$('#Loading_Modal').modal('hide');				
				
			}
			stompClient.subscribe('/user/queue/login', handler);
			stompClient.subscribe('/user/queue/errors', handler);
		}

		this.getStompClient = function() {
			if (typeof stompClient == "undefined") {
				connect();
			} else if (stompClient) {

			}
			return stompClient;
		}

		this.send = function(destination, header, playload) {
			stompClient.send(destination, header, playload);
		}
	};
	var MessageClientImpl = function() {
	}

	MessageClientImpl.prototype = new MessageClientParent();

	MessageClient = {
		getInstance : function() {
			return new MessageClientImpl();
		}
	}
})();

(function($) {
	UIManager = {
		activePage : function(page) {
			$(".page-section").each(function(){
				$(this).hide().addClass('hide');
			}); 
			
			$(page).hide().removeClass('hide').slideDown('fast');
		}
	}
})(jQuery);

$(function() {
	var client = MessageClient.getInstance().getStompClient();

	UIManager.activePage($("#loginPage"));
	
	$("#loginSubmit").click(function() {
		$('#Loading_Modal').modal('show');
		
		client.send("/app/login", {}, JSON.stringify({
			'loginId' : $("#username").val(),
			'password' : $("#password").val()
		}));
	});
});
