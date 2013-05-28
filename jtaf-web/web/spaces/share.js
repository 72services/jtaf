var user;
var space;
var userSpaces;

function loadData() {
    var id = searchMap.space_id;
    xhrGet("/jtaf/res/users/current", function(response) {
        user = JSON.parse(response);
    });
    xhrGet("/jtaf/res/spaces/" + id, function(response) {
        space = JSON.parse(response);
    });
    xhrGet("/jtaf/res/userspaces?space_id=" + id, function(response) {
        userSpaces = JSON.parse(response);
        createSharesTableBody();
    });
}

function createSharesTableBody() {
    var table = document.getElementById("shares_table");
    table.innerHTML = "";

    if (userSpaces === undefined || userSpaces.length === 0) {
        var row = table.insertRow(0);
        var cell = row.insertCell(0);
        cell.innerHTML = "No shares found";
        cell.setAttribute("colspan", 2);
    }
    else {
        var i = 0;
        userSpaces.forEach(function(userSpace) {
            createRow(table, userSpace, i);
            i++;
        });
    }
}

function createRow(table, userSpace, i) {
    var row = table.insertRow(i);
    var cellUser = row.insertCell(0);
    if (userSpace.user.email === undefined) {
        var userinput = document.createElement("input");
        userinput.id = "userinput";
        userinput.setAttribute("type", "text");
        cellUser.appendChild(userinput);
    } else {
        cellUser.innerHTML = userSpace.user.email;
    }
    var cellRole = row.insertCell(1);

    var select = document.createElement("select");
    if (user.email === userSpace.user.email) {
        select.setAttribute("readonly");
        select.setAttribute("disabled");
    }
    select.id = "select_" + i;
    var optionInput = document.createElement("option");
    optionInput.innerHTML = "INPUT";
    if (userSpace.role === "INPUT") {
        optionInput.selected = true;
    }
    select.appendChild(optionInput);
    var optionAdmin = document.createElement("option");
    optionAdmin.innerHTML = "ADMIN";
    if (userSpace.role === "ADMIN") {
        optionAdmin.selected = true;
    }
    select.appendChild(optionAdmin);
    cellRole.appendChild(select);

    var save = document.createElement("a");
    save.href = "#";
    save.setAttribute("onclick", "save(" + i + ")");
    save.appendChild(document.createTextNode("Save"));
    var del = document.createElement("a");
    del.href = "#";
    del.setAttribute("onclick", "deleteUserSpace(" + space.id + ")");
    del.appendChild(document.createTextNode("Delete"));
    var cellFunction = row.insertCell(2);
    cellFunction.appendChild(save);
    cellFunction.appendChild(document.createTextNode(" "));
    cellFunction.appendChild(del);
}

function deleteUserSpace(id) {
    if (confirm("Are you sure?")) {
        xhrDelete("/jtaf/res/userspaces/" + id, function() {
            loadData();
            info("Share deleted");
        });
    }
}

function save(index) {
    var userSpace = userSpaces[index];
    var userinput = document.getElementById("userinput");
    if (userinput !== undefined) {
        userSpace.user.email = userinput.value;
    }
    var select = document.getElementById("select_" + index);
    userSpace.role = select.options[select.selectedIndex].value;

    xhrPost("/jtaf/res/userspaces/", function() {
        loadData();
        info("Share saved");
    }, userSpace);
}

function addShare() {
    var index = userSpaces.length;
    var table = document.getElementById("shares_table");
    var userSpace = new Object();
    userSpace.space = space;
    userSpace.user = new Object();
    userSpaces[index] = userSpace;
    createRow(table, userSpace, index);
    document.getElementById("userinput").focus();
}