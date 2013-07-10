var divHeader = document.createElement("div");
divHeader.id = "header";

var divUserinfo = document.createElement("div");
divUserinfo.id = "userinfo";
getUserInfo(divUserinfo);

var divNavigation = document.createElement("div");
divNavigation.id = "navigation";

var home = document.createElement("a");
home.id = "navigation_0";
home.className = "navigation_inactive";
home.name = "navigation_element";
home.href = "/jtaf/index.html";
home.setAttribute("onclick", "activateNavigation(0);");
home.innerHTML = "HOME";
divNavigation.appendChild(home);

var spaces = document.createElement("a");
spaces.id = "navigation_1";
spaces.className = "navigation_inactive";
spaces.name = "navigation_element";
spaces.href = "/jtaf/spaces/index.html";
spaces.setAttribute("onclick", "activateNavigation(1);");
spaces.innerHTML = "MY SPACES";
divNavigation.appendChild(spaces);

divHeader.appendChild(divUserinfo);
divHeader.appendChild(divNavigation);

document.body.appendChild(divHeader);

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

function getUserInfo(div) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/jtaf/res/users/current", true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            var user = JSON.parse(xhr.response);
            var profile = document.createElement("a");
            profile.href = "/jtaf/user/profile.html";
            profile.innerHTML = user.firstName + " " + user.lastName;
            profile.style.color = "white";
            div.appendChild(profile);
        } else if (xhr.status === 204) {
            var login = document.createElement("a");
            login.href = "/jtaf/spaces/index.html";
            login.innerHTML = "Login";
            login.style.color = "white";
            div.appendChild(login);

            div.appendChild(document.createTextNode(" "));

            var register = document.createElement("a");
            register.href = "/jtaf/register.html";
            register.innerHTML = "Register";
            register.style.color = "white";
            div.appendChild(register);
        } else {
            error(xhr.status);
        }
    };
    xhr.send();
}