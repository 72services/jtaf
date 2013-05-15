var div = document.createElement("div");
div.id = "navigation";

var table = document.createElement("table");
table.className = "navigation";
var row = table.insertRow();

var cell0 = row.insertCell(0);
var home = document.createElement("a");
home.id = "navigation_0";
home.name = "navigation_element";
home.className = "navigation_inactive";
home.setAttribute("href", "/jtaf/index.html");
home.setAttribute("onclick", "activateNavigation();");
home.innerHTML = "Home";
cell0.appendChild(home);

var cell1 = row.insertCell(1);
var masterdata = document.createElement("a");
masterdata.id = "navigation_1";
masterdata.name = "navigation_element";
masterdata.className = "navigation_inactive";
masterdata.setAttribute("href", "/jtaf/masterdata/masterdata.html");
masterdata.setAttribute("onclick", "activateNavigation();");
masterdata.innerHTML = "Master data";
cell1.appendChild(masterdata);

div.appendChild(table);

document.body.appendChild(div);

var active = sessionStorage.getItem("navigation_active");
if (active !== undefined) {
    el(active).className = "navigation_active";
} else {
    home.className = "navigation_active";
}

function activateNavigation() {
    var els = document.getElementsByName("navigation_element");
    for (var i in els) {
        els[i].className = "navigation_inactive";
    }
    
    var el = event.srcElement;
    sessionStorage.setItem("navigation_active", el.id);
    el.className = "navigation_active";
}