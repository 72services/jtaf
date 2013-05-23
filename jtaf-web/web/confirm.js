var confirmation_id;

function loadData() {
    confirmation_id = param().confirmation_id;
    el("confirmation_id").value = confirmation_id;
}

function confirm() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/jtaf/res/users/confirm", true);
    xhr.onload = function() {
        if (xhr.status === 204) {
            info("Confirmation successful");
        } else {
            error(xhr.status);
        }
    };
    xhr.setRequestHeader("Content-Type", "text/plain");
    xhr.send(confirmation_id);
}

