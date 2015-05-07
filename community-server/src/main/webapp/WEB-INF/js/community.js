(function($) {
    window.MsgSubscribeManager = {
        notificationHandler : function(calResult) {

            $.indexedDB("CommunityDB").transaction([ "Notification" ], "readwrite").then(function() {
                console.log("Transaction completed");
            }, function() {
                console.log("Transaction aborted");
            }, function(t) {
                console.log("Transaction in progress");
                t.objectStore("Notification").add(calResult.body).then(function(result, event) {
                    console.log("Data added");

                    var promise = t.objectStore("Notification").count();
                    promise.done(function(result, event) {
                        $("#msgBadge").text(result);
                        $("#msgBadge").removeClass('hide');
                    });

                }, function(error, event) {
                    console.log("Error adding data");
                });
            });

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
        },
        
        registerTabNavButtonClick : function() {
            
            $("#homePage .navbar-fixed-bottom  [role='presentation']").each(function(index, element){                
                $(element).click(function() {
                    $("#homePage #mainContent .page-content").hide();
                    
                    $("#"+$(element).attr("targetContent")).removeClass("hide");
                    $("#"+$(element).attr("targetContent")).fadeIn(2000);
                    
                    $("#homePage .navbar-fixed-bottom  [role='presentation']").removeClass("active");
                    $(element).addClass("active");
                });
                
                $(element).css("cursor", "pointer");
            });
        }
    }
})(jQuery);

$(function() {
    $.indexedDB("CommunityDB", {
        "version" : 2,
        "upgrade" : function(transaction) {
        },
        "schema" : {
            "2" : function(transaction) {
                transaction.createObjectStore("Notification", {
                    // "keyPath": "id",
                    "autoIncrement" : true
                });
            },
        }
    }).done(function(db, event) {
        console.log("Successfully opened DB.");
    });
})

$(function() {
    // for mobile app there is only one window
    if (!UserManager.isLoggedIn()) {
        UIManager.activePage($("#loginPage"));
    }

    UIEventManager.registerLoginSubmit();    
    UIEventManager.registerTabNavButtonClick();
});
