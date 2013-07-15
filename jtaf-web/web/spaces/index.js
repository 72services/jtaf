var spaces;
var user;

function loadData() {
    xhrGet("/jtaf/res/spaces?my=true", function(response) {
        spaces = JSON.parse(response);
        createSpacesTableBody();
        i18n();
    });
    xhrGet("/jtaf/res/users/current", function(response) {
        user = JSON.parse(response);
        i18n();
    });
}

function deleteSpace(id) {
    if (confirm(transalte("Are you sure?"))) {
        xhrDelete("/jtaf/res/spaces/" + id, function() {
            loadData();
            info("Space deleted");
        });
    }
}

function createSpacesTableBody() {
    var table = document.getElementById("spaces_table");
    table.innerHTML = "";

    if (spaces === undefined || spaces.length === 0) {
        var row = table.insertRow(0);
        var cell = row.insertCell(0);
        cell.innerHTML = "No spaces found";
        cell.setAttribute("colspan", 2);
    }
    else {
        var i = 0;
        spaces.forEach(function(space) {
            var row = table.insertRow(i);
            var onclickEdit = "window.location = 'space.html?id=" + space.id + "'";
            var cellName = row.insertCell(0);
            cellName.className = "edit";
            cellName.innerHTML = space.name;
            cellName.setAttribute("onclick", onclickEdit);
            var share = document.createElement("a");
            share.href = "share.html?space_id=" + space.id;
            var shareSpan = document.createElement("span");
            shareSpan.setAttribute("class", "i18n");
            shareSpan.innerHTML = "Share";
            share.appendChild(shareSpan);
            var del = document.createElement("a");
            del.href = "#";
            del.setAttribute("onclick", "deleteSpace(" + space.id + ")");
            var delSpan = document.createElement("span");
            delSpan.setAttribute("class", "i18n");
            delSpan.innerHTML = "Delete";
            del.appendChild(delSpan);
            var cellFunction = row.insertCell(1);
            cellFunction.style.width = "100px";
            cellFunction.style.textAlign = "right";
            if (space.owner === user.email) {
                cellFunction.appendChild(share);
                cellFunction.appendChild(document.createTextNode(" "));
                cellFunction.appendChild(del);
            }
            i++;
        });
    }
}