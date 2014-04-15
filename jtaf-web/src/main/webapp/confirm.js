var confirmController = new ConfirmController();

window.onload = confirmController.loadData();

function ConfirmController() {
    var util = new Util();
    var confirmation_id;

    this.loadData = function() {
        util.showMessage();
        
        confirmation_id = util.searchMap.confirmation_id;
        document.getElementById("confirmation_id").value = confirmation_id;
    };

    this.confirm = function() {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/jtaf/res/users/confirm", true);
        xhr.onload = function() {
            if (xhr.status === 204) {
                document.getElementById("confirmation_id").setAttribute("disabled", "true");
                document.getElementById("confirmation_id").setAttribute("readonly", "true");
                util.info("Confirmation successful");
            } else {
                util.error(xhr.status);
            }
        };
        xhr.setRequestHeader("Content-Type", "text/plain");
        xhr.send(confirmation_id);
    };

}