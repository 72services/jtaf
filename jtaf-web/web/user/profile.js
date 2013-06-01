var user;

function loadData() {
    xhrGet("/jtaf/res/users/current", function(response) {
        parseAndFill(response);
    });
}

function parseAndFill(response) {
    user = JSON.parse(response);
    fillForm();
}

function fillForm() {
    document.getElementById("user_email").value = user.email;
    document.getElementById("user_lastName").value = user.lastName;
    document.getElementById("user_firstName").value = user.firstName;
    document.getElementById("user_lastName").focus();
}

function save() {
    fillUser();
    xhrPost("/jtaf/res/users/", function(response) {
        parseAndFill(response);
        info("Profile saved");
        window.location.reload();
    }, user);
}

function fillUser() {
    user.lastName = document.getElementById("user_lastName").value;
    user.firstName = document.getElementById("user_firstName").value;
}
