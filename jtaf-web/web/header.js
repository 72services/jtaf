var div = document.createElement("div");

div.id = "navigation";

var table = document.createElement("table");
table.className = "navigation";

var row0 = table.insertRow(0);

var cell00 = row0.insertCell(0);
var cell01 = row0.insertCell(1);
cell01.style.textAlign = "right";
cell01.style.paddingTop = "35px";
getUserInfo(cell01);

var row1 = table.insertRow(1);

var cell10 = row1.insertCell(0);
cell10.className = "navigation";
cell10.style.paddingTop = "40px";

var home = document.createElement("a");
home.id = "navigation_0";
home.className = "navigation_inactive";
home.name = "navigation_element";
home.href = "/jtaf/index.html";
home.setAttribute("onclick", "activateNavigation(0);");
home.innerHTML = "Home";
cell10.appendChild(home);

var spaces = document.createElement("a");
spaces.id = "navigation_1";
spaces.className = "navigation_inactive";
spaces.name = "navigation_element";
spaces.href = "/jtaf/spaces/index.html";
spaces.setAttribute("onclick", "activateNavigation(1);");
spaces.innerHTML = "My spaces";
cell10.appendChild(spaces);

var cell11 = row1.insertCell(1);

div.appendChild(table);

document.body.appendChild(div);

var active = sessionStorage.getItem("navigation_active");
if (active !== undefined) {
    var active_element = document.getElementById(active);
    if (active_element !== null) {
        active_element.className = "navigation_active";
    }
} else {
    home.className = "navigation_active";
}

function activateNavigation(index) {
    deactivateLinks();
    var element = document.getElementById("navigation_" + index);
    sessionStorage.setItem("navigation_active", element.id);
    element.className = "navigation_active";
}

function activateLink(id) {
    deactivateLinks();
    var element = document.getElementById(id);
    sessionStorage.setItem("navigation_active", id);
    element.className = "navigation_active";
}

function deactivateLinks() {
    var elements = document.getElementsByName("navigation_element");
    for (var i in elements) {
        elements[i].className = "navigation_inactive";
    }
}

function getUserInfo(cell) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/jtaf/res/users/current", true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            var user = JSON.parse(xhr.response);
            var profile = document.createElement("a");
            profile.href = "/jtaf/user/profile.html";
            profile.innerHTML = user.firstName + " " + user.lastName;
            profile.style.color = "white";
            cell.appendChild(profile);
            cell.appendChild(document.createTextNode(" (" + user.email + ")"));
            cell.style.color = "white";
        } else if (xhr.status === 204) {
            var login = document.createElement("a");
            login.href = "/jtaf/spaces/index.html";
            login.innerHTML = "Login";
            login.style.color = "white";
            cell.appendChild(login);

            cell.appendChild(document.createTextNode(" "));

            var register = document.createElement("a");
            register.href = "/jtaf/register.html";
            register.innerHTML = "Register";
            register.style.color = "white";
            cell.appendChild(register);
        } else {
            error(xhr.status);
        }
    };
    xhr.send();
}