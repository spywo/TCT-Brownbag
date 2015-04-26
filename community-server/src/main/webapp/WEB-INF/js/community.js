(function($) {
    window.MsgSubscribeManager = {
        notificationHandler : function(calResult) {

            var objectStore = $.indexedDB("Community-DB").objectStore("notification");
//            objectStore.add(calResult.body).done(function(){                
//            });;
//
//            var count = objectStore.count();

            $("#msgBadge").text(2);
            $("#msgBadge").removeClass('hide');

            UIManager.hideLoading();
        },
    }
})(jQuery);

(function() {
    window.AuthManager = {
        login : function() {
            $.ajax({
                url : '/community/login',
                type : 'post',
                data : 'username=' + $("#username").val() + "&password=" + $("#password").val(),
                async : true,
                error : function(data) {
                    UIManager.activePage($("#loginPage"));
                    UIManager.hideLoading();
                    UIManager.showWarning("Please enter correct username and password.");

                    $("#password").val("");
                    $("#password").focus();

                    console.log(data.statusText)
                },
                success : function(data) {
                    UserManager.setUser(data);

                    UIManager.activePage($("#homePage"));
                    UIManager.hideLoading();

                    MessageClient.getInstance().init();
                }
            });
        }
    }
})();

(function() {
    var MessageClientParent = function() {
        var stompClient = null;

        var connect = function() {
            var socket = new SockJS('/community/message');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, function(frame) {
                stompClient.subscribe('/topic/notification', MsgSubscribeManager.notificationHandler);

            }, function(error) {
                UIManager.hideLoading();
                UIManager.showWarning(error);
            });
        }

        var isConnected = function() {
            if (typeof stompClient != "undefined" && stompClient != null && stompClient.connected) {
                return true;
            } else {
                return false;
            }
        }

        this.init = function() {
            if (!isConnected()) {
                connect();
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

                AuthManager.login();
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

$(function() {
    $.indexedDB("Community-DB", {
        "schema" : {
            "1" : function(versionTransaction) {
                var notificationTable = versionTransaction.createObjectStore("notification", {
                    "keyPath" : "id",
                    "autoIncrement" : true
                });
                notificationTable.createIndex("id");
            }
        }
    }).done(function() {

    });
})
