
var confirmation_id;

function loadData() {
    confirmation_id = searchMap.confirmation_id;
    document.getElementById("confirmation_id").value = confirmation_id;
}

function confirm() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/jtaf/res/users/confirm", true);
    xhr.onload = function() {
        if (xhr.status === 204) {
            document.getElementById("confirmation_id").setAttribute("disabled");
            document.getElementById("confirmation_id").setAttribute("readonly");
            info("Confirmation successful");
        } else {
            error(xhr.status);
        }
    };
    xhr.setRequestHeader("Content-Type", "text/plain");
    xhr.send(confirmation_id);
}

