var seriesList;
var clubs;

function loadData() {
    xhrGet("/jtaf/res/series", function(response) {
        seriesList = JSON.parse(response);
        createSeriesTableBody();
    });

    xhrGet("/jtaf/res/clubs", function(response) {
        clubs = JSON.parse(response);
        createClubsTableBody();
    });
}

function deleteSeries(id) {
    if (confirm("Are you sure?")) {
        xhrDelete("/jtaf/res/series/" + id, function() {
            loadData();
            info("Serie deleted");
        });
    }
}

function copySeries(id) {
    xhrPost("/jtaf/res/series/" + id + "?function=copy", function() {
        loadData();
        info("Series copied");
    });

}

function deleteClub(id) {
    if (confirm("Are you sure?")) {
        xhrDelete("/jtaf/res/clubs/" + id, function() {
            loadData();
            info("Club deleted");
        });
    }
}

function createSeriesTableBody() {
    var table = el("series_table");
    table.innerHTML = "";

    if (seriesList === undefined || seriesList.length === 0) {
        var row = table.insertRow(0);
        var cell = row.insertCell(0);
        cell.innerHTML = "No series found";
        cell.setAttribute("colspan", 2);
    }
    else {
        var i = 0;
        seriesList.forEach(function(series) {
            var row = table.insertRow(i);
            var onclickEdit = "window.location = 'series.html?id=" +
                    series.id + "'";
            var cellName = row.insertCell(0);
            cellName.className = "edit";
            cellName.innerHTML = series.name;
            cellName.setAttribute("onclick", onclickEdit);
            var copy = document.createElement("a");
            copy.setAttribute("href", "#");
            copy.setAttribute("onclick", "copySeries(" + series.id + ")");
            copy.appendChild(document.createTextNode("Copy"));
            var del = document.createElement("a");
            del.setAttribute("href", "#");
            del.setAttribute("onclick", "deleteSeries(" + series.id + ")");
            del.appendChild(document.createTextNode("Delete"));
            var cellFunction = row.insertCell(1);
            cellFunction.appendChild(copy);
            cellFunction.appendChild(document.createTextNode(" "));
            cellFunction.appendChild(del);
            i++;
        });
    }
}

function createClubsTableBody() {
    var table = el("club_table");
    table.innerHTML = "";

    if (clubs === undefined || clubs.length === 0) {
        var row = table.insertRow(0);
        var cell = row.insertCell(0);
        cell.innerHTML = "No clubs found";
        cell.setAttribute("colspan", 3);
    }
    else {
        var i = 0;
        clubs.forEach(function(club) {
            var row = table.insertRow(i);
            var onclickEdit = "window.location = 'club.html?id=" +
                    club.id + "'";
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
            del.appendChild(document.createTextNode("Delete"));

            var cellFunction = row.insertCell(2);
            cellFunction.appendChild(del);
            i++;
        });
    }
}

