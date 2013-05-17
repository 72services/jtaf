createHeader();

function createHeader() {
    var div = document.createElement("div");
    div.id = "navigation";

    var table = document.createElement("table");
    table.className = "navigation";
    var row = table.insertRow();

    var cell0 = row.insertCell(0);
    cell0.className = "navigation";
    var home_div = document.createElement("div");
    home_div.id = "navigation_0";
    home_div.className = "navigation_inactive";
    home_div.name = "navigation_element";
    var home = document.createElement("a");
    home.setAttribute("href", "/jtaf/index.html");
    home.setAttribute("onclick", "activateNavigation(0); ");
    home.innerHTML = "Home";
    home_div.appendChild(home);
    cell0.appendChild(home_div);

    var cell1 = row.insertCell(1);
    cell1.className = "navigation";
    var masterdata_div = document.createElement("div");
    masterdata_div.id = "navigation_1";
    masterdata_div.className = "navigation_inactive";
    masterdata_div.name = "navigation_element";
    var masterdata = document.createElement("a");
    masterdata.setAttribute("href", "/jtaf/masterdata/masterdata.html");
    masterdata.setAttribute("onclick", "activateNavigation(1);");
    masterdata.innerHTML = "Master data";
    masterdata_div.appendChild(masterdata);
    cell1.appendChild(masterdata_div);

    var cell2 = row.insertCell(2);
    cell2.setAttribute("style", "text-align: right;");
    getUserInfo(cell2);

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
    xhrGet("/jtaf/res/users/current", function(response) {
        var user = JSON.parse(response);
        cell2.innerHTML = user.email;
    });
}