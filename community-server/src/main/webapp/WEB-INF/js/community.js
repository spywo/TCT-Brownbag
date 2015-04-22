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
        },
        
        authErrorHandler : function(calResult) {
            UIManager.activePage($("#loginPage"));
            UIManager.hideLoading();
            
            $("#password").val("");
            $("#password").focus();
        }
    }
})();

(function() {
    var MessageClientParent = function() {
        var stompClient = null;

        var isInitSubscriptionDone = false;

        var funcInvokingMap = {};

        var connect = function() {
            // reset the subscription flag in case the connection needs to be
            // regenerated
            isInitSubscriptionDone = false;

            var socket = new SockJS('/community/portfolio');
            stompClient = Stomp.over(socket);

            // once the browser has been closed, the disconnected frame will be
            // automatically sent
            stompClient.connect({}, function(frame) {
                initSubscription();
            }, function(error) {
                // all the STOMP web socket error will be handled here

                clearFuncWaitingInvocation();

                UIManager.hideLoading();
                UIManager.showWarning(error);
            });
        }

        var initSubscription = function() {

            stompClient.subscribe('/user/queue/login', MsgSubscribeManager.loginSuccessHandler);
            stompClient.subscribe('/user/queue/errors', MsgSubscribeManager.errorHandler);
            stompClient.subscribe('/user/queue/authError', MsgSubscribeManager.authErrorHandler);

            isInitSubscriptionDone = true;
        }

        var isConnected = function() {
            if (typeof stompClient != "undefined" && stompClient != null && stompClient.connected) {
                return true;
            } else {
                return false;
            }
        }

        // invoke the function once STOMP connection has been generated and all
        // subscriptions have been done
        var invokeLater = function(func, paramArray) {
            if (!isConnected() || !isInitSubscriptionDone) {
                // setTimeout(invokeLater, 100, client, func, params);
                var to = setTimeout(function() {
                    invokeLater.apply(null, [ func, paramArray ])
                }, 100);
                funcInvokingMap[func] = to;
            } else {
                func.apply(stompClient, paramArray);
            }
        }

        var clearFuncWaitingInvocation = function() {
            for ( var obj in funcInvokingMap) {
                clearTimeout(funcInvokingMap[obj]);
            }

            funcInvokingMap = {};
        }

        this.send = function(destination, header, playload) {
            if (isConnected()) {
                stompClient.send(destination, header, playload);
            } else {
                // recover the connection
                connect();

                invokeLater(stompClient.send, [ destination, header, playload ]);
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
    // for mobile app there is only one window
    if (!UserManager.isLoggedIn()) {
        UIManager.activePage($("#loginPage"));
    }

    UIEventManager.registerLoginSubmit();
});
