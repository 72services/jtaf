function ProfileController() {
    var util = new Util();

    var user;

    this.loadData = function() {
        util.showMessage();
        
        util.xhrGet("/res/users/current", function(response) {
            parseAndFill(response);
            util.i18n();
        });
    };

    this.save = function() {
        fillUser();
        util.xhrPost("/res/users/", function(response) {
            parseAndFill(response);
            util.info("Profile saved");
            window.location.reload();
        }, user);
    };

   this.back = function() {
        window.location.href = "/spaces/index.html";
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