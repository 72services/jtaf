var shareController = new ShareController();

function ShareController() {
    var util = new Util();

    var user;
    var space;
    var userSpaces;

    this.loadData = function() {
        util.showMessage();
        
        var id = util.searchMap.space_id;
        util.xhrGet("/jtaf/res/users/current", function(response) {
            user = JSON.parse(response);
            util.i18n();
        });
        util.xhrGet("/jtaf/res/spaces/" + id, function(response) {
            space = JSON.parse(response);
            util.i18n();
        });
        util.xhrGet("/jtaf/res/userspaces?space_id=" + id, function(response) {
            userSpaces = JSON.parse(response);
            createSharesTableBody();
            util.i18();
        });
    };

    this.addShare = function() {
        var index = userSpaces.length;
        var table = document.getElementById("shares_table");
        var userSpace = new Object();
        userSpace.space = space;
        userSpace.user = new Object();
        userSpaces[index] = userSpace;
        createRow(table, userSpace, index);
        document.getElementById("userinput").focus();
    };

    this.deleteUserSpace = function(id) {
        if (confirm(util.translate("Are you sure?"))) {
            util.xhrDelete("/jtaf/res/userspaces/" + id, function() {
                loadData();
                util.info("Share deleted");
            });
        }
    };

    this.save = function(index) {
        var userSpace = userSpaces[index];
        var userinput = document.getElementById("userinput");
        if (userinput != null) {
            userSpace.user.email = userinput.value;
        }
        var select = document.getElementById("select_" + index);
        userSpace.role = select.options[select.selectedIndex].value;

        util.xhrPost("/jtaf/res/userspaces/", function() {
            loadData();
            util.info("Share saved");
        }, userSpace);
    };

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
        select.setAttribute("onchange", "save(" + i + ")");
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
        var optionOwner = document.createElement("option");
        optionOwner.innerHTML = "OWNER";
        if (userSpace.role === "OWNER") {
            optionOwner.selected = true;
        }
        select.appendChild(optionOwner);
        cellRole.appendChild(select);

        var cellFunction = row.insertCell(2);
        cellFunction.setAttribute("style", "text-align: right;");
        if (user.email !== userSpace.user.email) {
            var del = document.createElement("a");
            del.href = "#";
            del.setAttribute("onclick", "deleteUserSpace(" + userSpace.id + ")");
            del.appendChild(document.createTextNode("Delete"));
            cellFunction.appendChild(del);
        }
    }

}