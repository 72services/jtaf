var profileController = new ProfileController();
window.onload = profileController.loadData();

function ProfileController() {
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
        fillUser();
        util.xhrPost("/jtaf/res/users/", function(response) {
            parseAndFill(response);
            util.info("Profile saved");
            window.location.reload();
        }, user);
    };

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

    function fillUser() {
        user.lastName = document.getElementById("user_lastName").value;
        user.firstName = document.getElementById("user_firstName").value;
    }

}