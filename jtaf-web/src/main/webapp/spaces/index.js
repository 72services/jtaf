var mySpacesController = new MySpacesController();
window.onload = mySpacesController.loadData();

function MySpacesController() {
    var util = new Util();

    var spaces;
    var user;

    this.loadData = function() {
        util.showMessage();
        
        util.xhrGet("/jtaf/res/spaces?my=true", function(response) {
            spaces = JSON.parse(response);
            createSpacesTableBody();
            util.i18n();
        });
        util.xhrGet("/jtaf/res/users/current", function(response) {
            user = JSON.parse(response);
            util.i18n();
        });
    };

    this.deleteSpace = function(id) {
        if (confirm(util.translate("Are you sure?"))) {
            util.xhrDelete("/jtaf/res/spaces/" + id, function() {
                loadData();
                util.info("Space deleted");
            });
        }
    };

    function createSpacesTableBody() {
        var table = document.getElementById("spaces_table");
        table.innerHTML = "";

        if (spaces === undefined || spaces.length === 0) {
            var row = table.insertRow(0);
            var cell = row.insertCell(0);
            cell.innerHTML = util.translate("No spaces found");
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
                shareSpan.className = "i18n";
                shareSpan.innerHTML = "Share";
                share.appendChild(shareSpan);
                var del = document.createElement("a");
                del.href = "#";
                del.setAttribute("onclick", "mySpacesController.deleteSpace(" + space.id + ")");
                var delSpan = document.createElement("span");
                delSpan.className = "i18n";
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

}