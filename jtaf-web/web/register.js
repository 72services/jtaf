var registerController = new RegisterController();

function RegisterController() {
    var user;

    this.save = function() {
        if (checkPassword()) {
            fillUser();
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/jtaf/res/users", true);
            xhr.onload = function() {
                if (xhr.status === 200) {
                    info("Thanks for registering!<br/>Please check your mailbox.");
                } else if (xhr.status === 412) {
                    error("User already exists");
                } else {
                    error(xhr.status);
                }
            };
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.send(JSON.stringify(user));
        }
        else {
            error("Passwords do not match!");
        }
    };

    function fillUser() {
        user = new Object();
        user.email = document.getElementById("user_email").value;
        user.secret = document.getElementById("user_password").value;
        user.lastName = document.getElementById("user_lastName").value;
        user.firstName = document.getElementById("user_firstName").value;
    }

    function checkPassword() {
        var password = document.getElementById("user_password").value;
        var confirm = document.getElementById("user_password_confirm").value;
        return password === confirm;
    }

}