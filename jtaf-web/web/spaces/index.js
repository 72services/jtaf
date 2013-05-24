var spaces;

function loadData() {
    xhrGet("/jtaf/res/spaces", function(response) {
        spaces = JSON.parse(response);
        createSpacesTableBody();
    });
}

function deleteSpace(id) {
    if (confirm("Are you sure?")) {
        xhrDelete("/jtaf/res/spaces/" + id, function() {
            loadData();
            info("Space deleted");
        });
    }
}

function createSpacesTableBody() {
    var table = el("spaces_table");
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
            share.appendChild(document.createTextNode("Share"));
            var del = document.createElement("a");
            del.href = "#";
            del.setAttribute("onclick", "deleteSpace(" + space.id + ")");
            del.appendChild(document.createTextNode("Delete"));
            var cellFunction = row.insertCell(1);
            cellFunction.appendChild(share);
            cellFunction.appendChild(document.createTextNode(" "));
            cellFunction.appendChild(del);
            i++;
        });
    }
}