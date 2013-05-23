var user;

function save() {
    if (checkPassword()) {
        fillUser();
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/jtaf/res/users", true);
        xhr.onload = function() {
            if (xhr.status === 200) {
                info("Thanks for registering!");
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
}

function fillUser() {
    user = new Object();
    user.email = el("user_email").value;
    user.secret = el("user_password").value;
    user.lastName = el("user_lastName").value;
    user.firstName = el("user_firstName").value;
}

function checkPassword() {
    var password = el("user_password").value;
    var confirm = el("user_password_confirm").value;
    return password === confirm;
}