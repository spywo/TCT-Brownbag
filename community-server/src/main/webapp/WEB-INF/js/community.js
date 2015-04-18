(function() {
    window.MsgSubscribeManager = {
        errorHandler : function(calResult) {
            var response = JSON.parse(calResult.body);

            UIManager.hideLoading();
            UIManager.showWarning("Please enter correct username and password.");
        },

        loginSuccessHandler : function(calResult) {
            var response = JSON.parse(calResult.body);
            UserManager.setUser(response.payload);

            UIManager.activePage($("#homePage"));
            UIManager.hideLoading();
        }
    }
})();

(function() {
    var MessageClientParent = function() {
        var stompClient = null;
        
        var isInitSubscriptionDone = false;

        var connect = function() {
            isInitSubscriptionDone = false;
            
            var socket = new SockJS('/community/portfolio');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, function(frame) {
                initSubscription();
            }, function(error) {
                UIManager.showWarning(error);
            });
        }

        var initSubscription = function() {

            stompClient.subscribe('/user/queue/login', MsgSubscribeManager.loginSuccessHandler);
            stompClient.subscribe('/user/queue/errors', MsgSubscribeManager.errorHandler);
            
            isInitSubscriptionDone = true;
        }

                
        this.isConnected = function() {
            if (typeof stompClient != "undefined" && stompClient != null && stompClient.connected) {
                return true;
            } else {
                return false;
            }            
        }
        
        var invokeLater = function(client, func, paramArray) {
            if (!client.isConnected() || !isAllSubscribed) {
               // setTimeout(invokeLater, 100,  client, func, params);
                setTimeout(function() { invokeLater.apply(null, [client, func, paramArray]) }, 100);
            } else {
                func.apply(stompClient, paramArray);
            }
        } 

        this.send = function(destination, header, playload) {
            try {
                if (this.isConnected()) {
                    stompClient.send(destination, header, playload);
                } else {
                    // recover the connection
                    connect();
                    
                    invokeLater(this, stompClient.send, [destination, header, playload]);
                }
            } catch (err) {
                UIManager.hideLoading();
                UIManager.showWarning(err.toString());
            }
        }
    };

    var MessageClientImpl = function() {
    }

    MessageClientImpl.prototype = new MessageClientParent();

    window.MessageClient = {
        getInstance : function() {
            // all impls share same prototype, which is different with other
            // language, like Java.
            return new MessageClientImpl();
        }
    }
})();

(function($) {
    window.UserManager = {
        getUser : function() {
            return window.current_user;

        },
        
        setUser : function(user) {
            window.current_user = user;
        },
        
        isLoggedIn : function() {
            if (typeof window.current_user == 'undefined') {
                return false;
            } else {
                return true;
            }
        }
    }
})(jQuery);

(function($) {
    window.UIManager = {
        activePage : function(page) {
            $(".page-section").each(function() {
                $(this).hide().addClass('hide');
            });

            $(page).hide().removeClass('hide').slideDown('fast');
        },

        // show and hide the loading overlay
        showLoading : function() {
            $('#loading_overlay').on('show.bs.modal', function() {
                var top = Math.round(($(this).height() - $(this).find('.modal-content').height()) / 2);
                top = top > 0 ? top : 0;
                $(this).find('.modal-content').css("margin-top", top);
            }).modal('show');
        },

        hideLoading : function() {
            $('#loading_overlay').modal('hide');
        },

        showWarning : function(content) {
            $('#warning_overlay').on('show.bs.modal', function() {
                var top = Math.round(($(this).height() - $(this).find('.modal-content').height()) / 2);
                top = top > 0 ? top : 0;
                $(this).find('.modal-content').css("margin-top", top);
                $(this).find('.modal-body').html(content);
            }).modal('show');
        }
    }
})(jQuery);

(function($) {
    window.UIEventManager = {
        registerLoginSubmit : function() {
            // register the click event for login submit button
            $("#loginSubmit").click(function() {
                UIManager.showLoading();

                MessageClient.getInstance().send("/app/login", {}, JSON.stringify({
                    'loginId' : $("#username").val(),
                    'password' : $("#password").val()
                }));
            });
        }
    }
})(jQuery);

$(function() {
    var client = MessageClient.getInstance();

    if (!UserManager.isLoggedIn()) {
        UIManager.activePage($("#loginPage"));
    }

    UIEventManager.registerLoginSubmit();
});
