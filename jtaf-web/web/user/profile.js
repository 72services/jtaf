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
    el("user_email").value = user.email;
    el("user_lastName").value = user.lastName;
    el("user_firstName").value = user.firstName;
    el("user_lastName").focus();
}

function save() {
    fillUser();
    xhrPost("/jtaf/res/users/", function(response) {
        parseAndFill(response);
        info("Profile saved");
    }, user);
}

function fillUser() {
    user.lastName = el("user_lastName").value;
    user.firstName = el("user_firstName").value;
}
