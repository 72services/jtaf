function PasswordController() {
    var util = new Util();

    var user;

    this.loadData = function() {
        util.showMessage();

        util.xhrGet("/jtaf/res/users/current", function(response) {
            parseAndFill(response);
            util.i18n();
        });
    };

    this.save = function() {
        if (checkPassword()) {
            fillUser();
            util.xhrPost("/jtaf/res/users/changepassword", function(response) {
                parseAndFill(response);
                util.info("Password changed");
            }, user);
        }
    };

    function parseAndFill(response) {
        user = JSON.parse(response);
        fillForm();
    }

    function fillForm() {
        document.getElementById("user_email").value = user.email;
    }

    function fillUser() {
        user.secret = document.getElementById("user_password").value;
    }

    function checkPassword() {
        var password = document.getElementById("user_password").value;
        var confirm = document.getElementById("user_password_confirm").value;
        return password === confirm;
    }


}

var passwordController = new PasswordController();