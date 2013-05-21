createHeader();

function createHeader() {
    var div = document.createElement("div");
    div.id = "navigation";

    var table = document.createElement("table");
    table.className = "navigation";
    var row = table.insertRow();

    var cell0 = row.insertCell(0);
    cell0.className = "navigation";

    var home = document.createElement("a");
    home.id = "navigation_0";
    home.className = "navigation_inactive";
    home.name = "navigation_element";
    home.href = "/jtaf/index.html";
    home.setAttribute("onclick", "activateNavigation(0);");
    home.innerHTML = "Home";
    cell0.appendChild(home);

    var masterdata = document.createElement("a");
    masterdata.id = "navigation_1";
    masterdata.className = "navigation_inactive";
    masterdata.name = "navigation_element";
    masterdata.href = "/jtaf/masterdata/index.html";
    masterdata.setAttribute("onclick", "activateNavigation(1);");
    masterdata.innerHTML = "Master data";
    cell0.appendChild(masterdata);

    var cell1 = row.insertCell(1);
    cell1.style.textAlign = "right";
    getUserInfo(cell1);

    div.appendChild(table);

    document.body.appendChild(div);

    var active = sessionStorage.getItem("navigation_active");
    if (active !== undefined) {
        var active_element = el(active);
        if (active_element !== null) {
            active_element.className = "navigation_active";
        }
    } else {
        home.className = "navigation_active";
    }
}

function activateNavigation(index) {
    deactivateLinks();
    var element = el("navigation_" + index);
    sessionStorage.setItem("navigation_active", element.id);
    element.className = "navigation_active";
}

function activateLink(id) {
    deactivateLinks();
    var element = el(id);
    sessionStorage.setItem("navigation_active", id);
    element.className = "navigation_active";
}

function deactivateLinks() {
    var elements = document.getElementsByName("navigation_element");
    for (var i in elements) {
        elements[i].className = "navigation_inactive";
    }
}

function getUserInfo(cell2) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/jtaf/res/users/current", true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            var user = JSON.parse(xhr.response);
            cell2.innerHTML = user.firstName + " " + user.lastName + " (" + user.email + ")";
        } else if (xhr.status === 204) {
            var login = document.createElement("a");
            login.href = "profile.html";
            login.innerHTML = "Login";
            cell2.appendChild(login);

            cell2.appendChild(document.createTextNode(" "));

            var register = document.createElement("a");
            register.href = "register.html";
            register.innerHTML = "Register for JTAF";
            cell2.appendChild(register);
        } else {
            error(xhr.status);
        }
    };
    xhr.send();
}