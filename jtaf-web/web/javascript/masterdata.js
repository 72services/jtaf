var ascending = true;
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

function deleteSerie(id) {
    xhrDelete("/jtaf/res/series/" + id, function() {
        loadData();
        info("Serie deleted");
    });
}

function deleteClubs(id) {
    xhrDelete("/jtaf/res/clubs/" + id, function() {
        loadData();
        info("Club deleted");
    });
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
        for (var i in seriesList) {
            var series = seriesList[i];
            var row = table.insertRow(i);

            var onclickEdit = "window.location = 'series.html?id=" +
                    series.id + "'";

            var cellName = row.insertCell(0);
            cellName.className = "edit";
            cellName.innerHTML = series.name;
            cellName.setAttribute("onclick", onclickEdit);

            var del = document.createElement("a");
            del.setAttribute("href", "#");
            del.setAttribute("onclick", "deleteSeries(" + series.id + ")");
            del.appendChild(document.createTextNode("Delete"));

            var cellFunction = row.insertCell(1);
            cellFunction.appendChild(del);
        }
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
        for (var i in clubs) {
            var club = clubs[i];
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
        }
    }
}
function sortBy(property) {
    seriesList.sort(createComparator(property));
    if (ascending) {
        seriesList.reverse();
    }
    ascending = !ascending;
    createTableBody(seriesList);
}

function filter(property) {
    var filteredSeries = new Array();
    var searchString = document.getElementById(property).value;
    if (searchString !== "") {
        var j = 0;
        for (i in seriesList) {
            var series = seriesList[i];
            if (series[property].toLowerCase().indexOf(searchString.toLowerCase()) !== -1) {
                filteredSeries[j] = series;
                j++;
            }
        }
        createTableBody(filteredSeries);
    } else {
        createTableBody(seriesList);
    }
}
