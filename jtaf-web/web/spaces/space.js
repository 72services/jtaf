var space;

function loadData() {
    var id = searchMap.id;

    if (id === undefined) {
        space = new Object();
        createSeriesTableBody();
        createClubsTableBody();
        document.getElementById("space_name").focus();
        i18n();
    } else {
        xhrGet("/jtaf/res/spaces/" + id, function(response) {
            parseAndFillSpace(response);
            i18n();
        });
    }
}

function parseAndFillSpace(response) {
    space = JSON.parse(response);

    fillForm();
    createSeriesTableBody();
    createClubsTableBody();
}

function fillForm() {
    document.getElementById("space_id").value = space.id;
    document.getElementById("space_name").value = space.name;
    document.getElementById("space_name").focus();
}

function Series(id) {
    if (confirm(translate("Are you sure?"))) {
        xhrDelete("/jtaf/res/series/" + id, function() {
            loadData();
            info("Serie deleted");
        });
    }
}

function copySeries(id) {
    if (confirm(translate("Are you sure?"))) {
        xhrPost("/jtaf/res/series/" + id + "?function=copy", function() {
            loadData();
            info("Series copied");
        });
    }

}

function deleteClub(id) {
    if (confirm(translate("Are you sure?"))) {
        xhrDelete("/jtaf/res/clubs/" + id, function() {
            loadData();
            info("Club deleted");
        });
    }
}

function createSeriesTableBody() {
    var table = document.getElementById("series_table");
    table.innerHTML = "";

    if (space.series === undefined || space.series.length === 0) {
        var row = table.insertRow(0);
        var cell = row.insertCell(0);
        cell.innerHTML = translate("No series found");
        cell.setAttribute("colspan", 2);
    }
    else {
        var i = 0;
        space.series.forEach(function(series) {
            var row = table.insertRow(i);
            var onclickEdit = "window.location = 'series.html?id=" + series.id + "&space_id=" + space.id + "'";
            var cellName = row.insertCell(0);
            cellName.className = "edit";
            cellName.innerHTML = series.name;
            cellName.setAttribute("onclick", onclickEdit);
            var copy = document.createElement("a");
            copy.href = "#";
            copy.setAttribute("onclick", "copySeries(" + series.id + ")");
            var copySpan = document.createElement("span");
            copySpan.setAttribute("class", "i18n");
            copySpan.innerHTML = "Copy";
            copy.appendChild(copySpan);
            var del = document.createElement("a");
            del.href = "#";
            del.setAttribute("onclick", "deleteSeries(" + series.id + ")");
            var delSpan = document.createElement("span");
            delSpan.setAttribute("class", "i18n");
            delSpan.innerHTML = "Delete";
            del.appendChild(delSpan);
            var cellFunction = row.insertCell(1);
            cellFunction.style.width = "150px";
            cellFunction.style.textAlign = "right";
            cellFunction.appendChild(copy);
            cellFunction.appendChild(document.createTextNode(" "));
            cellFunction.appendChild(del);
            i++;
        });
    }
}

function createClubsTableBody() {
    var table = document.getElementById("club_table");
    table.innerHTML = "";

    if (space.clubs === undefined || space.clubs.length === 0) {
        var row = table.insertRow(0);
        var cell = row.insertCell(0);
        cell.innerHTML = translate("No clubs found");
        cell.setAttribute("colspan", 3);
    }
    else {
        var i = 0;
        space.clubs.forEach(function(club) {
            var row = table.insertRow(i);
            var onclickEdit = "window.location = 'club.html?id=" + club.id + "&space_id=" + space.id + "'";
            var cellAbbr = row.insertCell(0);
            cellAbbr.className = "edit";
            cellAbbr.innerHTML = club.abbreviation;
            cellAbbr.setAttribute("onclick", onclickEdit);
            var cellName = row.insertCell(1);
            cellName.className = "edit";
            cellName.innerHTML = club.name;
            cellName.setAttribute("onclick", onclickEdit);
            var del = document.createElement("a");
            del.setAttribute("href", "#");
            del.setAttribute("onclick", "deleteClub(" + club.id + ")");
            var delSpan = document.createElement("span");
            delSpan.setAttribute("class", "i18n");
            delSpan.innerHTML = "Delete";
            del.appendChild(delSpan);

            var cellFunction = row.insertCell(2);
            cellFunction.style.width = "150px";
            cellFunction.style.textAlign = "right";
            
            cellFunction.appendChild(del);
            i++;
        });
    }
}

function addSeries() {
    window.location = "series.html?space_id=" + space.id;
}

function addClub() {
    window.location = "club.html?space_id=" + space.id;
}

function fillSpace() {
    space.name = document.getElementById("space_name").value;
}

function save() {
    fillSpace();
    xhrPost("/jtaf/res/spaces/", function(response) {
        parseAndFillSpace(response);
        info("Space saved");
    }, space);
}
