var div = document.createElement("div");
div.id = "navigation";

var table = document.createElement("table");
table.className = "navigation";
var row = table.insertRow();

var cell0 = row.insertCell(0);
cell0.className = "navigation";
cell0.style.width = "130px";
cell0.style.paddingTop = "0px";

var logo = document.createElement("img");
logo.src = "/jtaf/images/logo.png";
cell0.appendChild(logo);

var cell1 = row.insertCell(1);
cell1.className = "navigation";
cell1.style.paddingTop = "10px";

var home = document.createElement("a");
home.id = "navigation_0";
home.className = "navigation_inactive";
home.name = "navigation_element";
home.href = "/jtaf/index.html";
home.setAttribute("onclick", "activateNavigation(0);");
home.innerHTML = "Home";
cell1.appendChild(home);

var spaces = document.createElement("a");
spaces.id = "navigation_1";
spaces.className = "navigation_inactive";
spaces.name = "navigation_element";
spaces.href = "/jtaf/spaces/index.html";
spaces.setAttribute("onclick", "activateNavigation(1);");
spaces.innerHTML = "My spaces";
cell1.appendChild(spaces);

var cell2 = row.insertCell(2);
cell2.style.textAlign = "right";
getUserInfo(cell2);

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
            cell.appendChild(profile);
            cell.appendChild(document.createTextNode(" (" + user.email + ")"));
        } else if (xhr.status === 204) {
            var login = document.createElement("a");
            login.href = "/jtaf/spaces/index.html";
            login.innerHTML = "Login";
            cell.appendChild(login);

            cell.appendChild(document.createTextNode(" "));

            var register = document.createElement("a");
            register.href = "/jtaf/register.html";
            register.innerHTML = "Register";
            cell.appendChild(register);
        } else {
            error(xhr.status);
        }
    };
    xhr.send();
}