var series;
var athletes;
var ascending = true;
var space_id;
var series_id;

function loadData() {
    space_id = searchMap.space_id;

    series_id = searchMap.id;

    if (series_id === undefined) {
        series = new Object();
        document.getElementById("series_name").focus();
    } else {
        xhrGet("/jtaf/res/series/" + series_id, function(response) {
            parseAndFillSeries(response);
        });
        xhrGet("/jtaf/res/categories?series_id=" + series_id, function(response) {
            parseAndFillCategories(response);
        });
        xhrGet("/jtaf/res/events?series_id=" + series_id, function(response) {
            parseAndFillEvents(response);
        });
        xhrGet("/jtaf/res/athletes?series_id=" + series_id, function(response) {
            parseAndFillAthletes(response);
        });
    }
    var active_tab = localStorage.getItem("active_tab");
    if (active_tab != null) {
        switchTo(active_tab);
    }
}

function parseAndFillSeries(response) {
    series = JSON.parse(response);

    fillForm();
    fillCompetitionTable();
}

function fillForm() {
    document.getElementById("series_id").value = series.id;
    document.getElementById("series_name").value = series.name;
    document.getElementById("series_name").focus();
}

function fillCompetitionTable() {
    var table = document.getElementById("competition_table");
    table.innerHTML = "";
    if (series.competitions === undefined || series.competitions.length === 0) {
        var row = table.insertRow(0);
        var cellName = row.insertCell(0);
        cellName.innerHTML = "No competitions found";
        cellName.setAttribute("colspan", 3);
    }
    else {
        var i = 0;
        series.competitions.forEach(function(competition) {
            var row = table.insertRow(i);
            var onclickEdit = "window.location = 'competition.html?id=" + competition.id + "&series_id=" + series_id + "'";
            var cellName = row.insertCell(0);
            cellName.className = "edit";
            cellName.innerHTML = competition.name;
            cellName.setAttribute("onclick", onclickEdit);
            var cellDate = row.insertCell(1);
            cellDate.className = "edit";
            cellDate.innerHTML = competition.competitionDate;
            cellDate.setAttribute("onclick", onclickEdit);
            var sheet = document.createElement("a");
            sheet.setAttribute("href", "/jtaf/res/reports/sheet?competitionid=" + competition.id);
            sheet.setAttribute("target", "_blank");
            sheet.appendChild(document.createTextNode("Sheets"));
            var sheetOrderByClub = document.createElement("a");
            sheetOrderByClub.setAttribute("href", "/jtaf/res/reports/sheet?orderby=club&competitionid=" + competition.id);
            sheetOrderByClub.setAttribute("target", "_blank");
            sheetOrderByClub.appendChild(document.createTextNode("(order by club)"));
            var del = document.createElement("a");
            del.setAttribute("href", "#");
            del.setAttribute("onclick", "deleteCompetition(" +
                    competition.id + ")");
            del.appendChild(document.createTextNode("Delete"));
            var cellFunction = row.insertCell(2);
            cellFunction.setAttribute("style", "text-align: right;");
            cellFunction.appendChild(sheet);
            cellFunction.appendChild(document.createTextNode(" "));
            cellFunction.appendChild(sheetOrderByClub);
            cellFunction.appendChild(document.createTextNode(" "));
            cellFunction.appendChild(del);
            i++;
        });
    }
}

function parseAndFillCategories(response) {
    var categories = JSON.parse(response);
    var table = document.getElementById("category_table");
    table.innerHTML = "";
    if (categories === undefined || categories.length === 0) {
        var row = table.insertRow(0);
        var cellName = row.insertCell(0);
        cellName.innerHTML = "No categories found";
        cellName.setAttribute("colspan", 6);
    }
    else {
        var i = 0;
        categories.forEach(function(category) {
            var row = table.insertRow(i);
            var onclickEdit = "window.location = 'category.html?id=" + category.id + "&series_id=" + series_id + "'";
            var cellAbbr = row.insertCell(0);
            cellAbbr.className = "edit";
            cellAbbr.innerHTML = category.abbreviation;
            cellAbbr.setAttribute("onclick", onclickEdit);
            var cellName = row.insertCell(1);
            cellName.className = "edit";
            cellName.innerHTML = category.name;
            cellName.setAttribute("onclick", onclickEdit);
            var cellYearFrom = row.insertCell(2);
            cellYearFrom.className = "edit";
            cellYearFrom.innerHTML = category.yearFrom;
            cellYearFrom.setAttribute("onclick", onclickEdit);
            var cellYearTo = row.insertCell(3);
            cellYearTo.className = "edit";
            cellYearTo.innerHTML = category.yearTo;
            cellYearTo.setAttribute("onclick", onclickEdit);
            var cellGender = row.insertCell(4);
            cellGender.className = "edit";
            cellGender.innerHTML = category.gender;
            cellGender.setAttribute("onclick", onclickEdit);
            var sheet = document.createElement("a");
            sheet.setAttribute("href", "/jtaf/res/reports/sheet?categoryid=" + category.id);
            sheet.setAttribute("target", "_blank");
            sheet.appendChild(document.createTextNode("Sheet"));
            var del = document.createElement("a");
            del.setAttribute("href", "#");
            del.setAttribute("onclick", "deleteCategory(" +
                    category.id + ")");
            del.appendChild(document.createTextNode("Delete"));
            var cellFunction = row.insertCell(5);
            cellFunction.setAttribute("style", "text-align: right;");
            cellFunction.appendChild(sheet);
            cellFunction.appendChild(document.createTextNode(" "));
            cellFunction.appendChild(del);
            i++;
        });
    }
}

function parseAndFillEvents(response) {
    var events = JSON.parse(response);
    var table = document.getElementById("event_table");
    table.innerHTML = "";
    if (events === undefined || events.length === 0) {
        var row = table.insertRow(0);
        var cellName = row.insertCell(0);
        cellName.innerHTML = "No events found";
        cellName.setAttribute("colspan", 8);
    }
    else {
        var i = 0;
        events.forEach(function(event) {
            var row = table.insertRow(i);
            var onclickEdit = "window.location = 'event.html?id=" + event.id + "&series_id=" + series_id + "'";
            var cellName = row.insertCell(0);
            cellName.className = "edit";
            cellName.innerHTML = event.name;
            cellName.setAttribute("onclick", onclickEdit);
            var cellType = row.insertCell(1);
            cellType.className = "edit";
            cellType.innerHTML = event.type;
            cellType.setAttribute("onclick", onclickEdit);
            var cellGender = row.insertCell(2);
            cellGender.className = "edit";
            cellGender.innerHTML = event.gender;
            cellGender.setAttribute("onclick", onclickEdit);
            var cellA = row.insertCell(3);
            cellA.className = "edit";
            cellA.innerHTML = event.a;
            cellA.setAttribute("onclick", onclickEdit);
            var cellB = row.insertCell(4);
            cellB.className = "edit";
            cellB.innerHTML = event.b;
            cellB.setAttribute("onclick", onclickEdit);
            var cellC = row.insertCell(5);
            cellC.className = "edit";
            cellC.innerHTML = event.c;
            cellC.setAttribute("onclick", onclickEdit);
            var del = document.createElement("a");
            del.setAttribute("href", "#");
            del.setAttribute("onclick", "deleteEvent(" + event.id + ")");
            del.appendChild(document.createTextNode("Delete"));
            var cellFunction = row.insertCell(6);
            cellFunction.setAttribute("style", "text-align: right;");
            cellFunction.appendChild(del);
            i++;
        });
    }
}

function parseAndFillAthletes(response) {
    athletes = JSON.parse(response);
    if (athletes === undefined || athletes.length === 0) {
        var table = document.getElementById("athlete_table");
        table.innerHTML = "";
        var row = table.insertRow(0);
        var cellName = row.insertCell(0);
        cellName.innerHTML = "No athletes found";
        cellName.setAttribute("colspan", 8);
    }
    else {
        fillAthletesTable(athletes);
    }
}

function fillAthletesTable(athletes) {
    var table = document.getElementById("athlete_table");
    table.innerHTML = "";
    var i = 0;
    athletes.forEach(function(athlete) {
        var row = table.insertRow(i);
        var onclickEdit = "window.location = 'athlete.html?id=" + athlete.id +
                "&series_id=" + series_id + "&space_id=" + space_id + "'";
        var cellId = row.insertCell(0);
        cellId.className = "edit";
        cellId.innerHTML = athlete.id;
        cellId.setAttribute("onclick", onclickEdit);
        var cellLastName = row.insertCell(1);
        cellLastName.className = "edit";
        cellLastName.innerHTML = athlete.lastName;
        cellLastName.setAttribute("onclick", onclickEdit);
        var cellFirstName = row.insertCell(2);
        cellFirstName.className = "edit";
        cellFirstName.innerHTML = athlete.firstName;
        cellFirstName.setAttribute("onclick", onclickEdit);
        var cellYear = row.insertCell(3);
        cellYear.className = "edit";
        cellYear.innerHTML = athlete.year;
        cellYear.setAttribute("onclick", onclickEdit);
        var cellGender = row.insertCell(4);
        cellGender.className = "edit";
        cellGender.innerHTML = athlete.gender;
        cellGender.setAttribute("onclick", onclickEdit);
        var cellCategory = row.insertCell(5);
        cellCategory.className = "edit";
        cellCategory.innerHTML = athlete.category !== null
                ? athlete.category.abbreviation : "";
        cellCategory.setAttribute("onclick", onclickEdit);
        var cellClub = row.insertCell(6);
        cellClub.className = "edit";
        cellClub.innerHTML = athlete.club !== null
                ? athlete.club.abbreviation : "";
        cellClub.setAttribute("onclick", onclickEdit);

        var del = document.createElement("a");
        del.setAttribute("href", "#");
        del.setAttribute("onclick", "deleteAthlete(" + athlete.id + ")");
        del.appendChild(document.createTextNode("Delete"));
        var cellFunction = row.insertCell(7);
        cellFunction.setAttribute("style", "text-align: right;");
        cellFunction.appendChild(del);
        i++;
    });
}

function save() {
    fillSeries();
    xhrPost("/jtaf/res/series/", function(response) {
        parseAndFillSeries(response);
        info("Series saved");
    }, series);
}

function fillSeries() {
    series.name = document.getElementById("series_name").value;
    series.space_id = space_id;
}

function deleteCompetition(id) {
    if (confirm(getString("Are you sure?"))) {
        xhrDelete("/jtaf/res/competitions/" + id, function() {
            loadData();
            info("Competition deleted");
        });
    }
}

function deleteEvent(id) {
    if (confirm(getString("Are you sure?"))) {
        xhrDelete("/jtaf/res/events/" + id, function() {
            loadData();
            info("Event deleted");
        });
    }
}

function deleteAthlete(id) {
    if (confirm(getString("Are you sure?"))) {
        xhrDelete("/jtaf/res/athletes/" + id, function() {
            loadData();
            info("Athlete deleted");
        });
    }
}

function deleteCategory(id) {
    if (confirm(getString("Are you sure?"))) {
        xhrDelete("/jtaf/res/categories/" + id, function() {
            loadData();
            info("Category deleted");
        });
    }
}

function switchTo(div) {
    var els = document.getElementsByClassName("visible");
    for (var i in els) {
        els[i].className = "invisible";
    }
    document.getElementById(div).className = "visible";

    document.getElementById("a_competitions").className = "tab_inactive";
    document.getElementById("a_events").className = "tab_inactive";
    document.getElementById("a_categories").className = "tab_inactive";
    document.getElementById("a_athletes").className = "tab_inactive";

    document.getElementById("a_" + div).className = "tab_active";
    localStorage.setItem("active_tab", div);
}

function back() {
    localStorage.removeItem("active_tab");
    window.location = "index.html";
}


function sortBy(property) {
    if (event.srcElement.type === undefined) {
        athletes.sort(createComparator(property));
        if (ascending) {
            athletes.reverse();
        }
        ascending = !ascending;
        fillAthletesTable(athletes);
    }
}

function filter(property) {
    var filteredAthletes = new Array();
    var searchString = document.getElementById(property).value;
    if (searchString !== "") {
        var j = 0;
        athletes.forEach(function(athlete) {
            var valueToCompare = null;
            if (property === "category") {
                valueToCompare = athlete.category.abbreviation;
            } else if (property === "club") {
                valueToCompare = athlete.club.abbreviation;
            } else {
                valueToCompare = athlete[property];
            }
            if (valueToCompare !== null && valueToCompare.toLowerCase().indexOf(searchString.toLowerCase()) !== -1) {
                filteredAthletes[j] = athlete;
                j++;
            }
        });
        fillAthletesTable(filteredAthletes);
    } else {
        fillAthletesTable(athletes);
    }
}

function addCompetition() {
    window.location = "competition.html?series_id=" + series_id;
}

function addEvent() {
    window.location = "event.html?series_id=" + series_id;
}

function addCategory() {
    window.location = "category.html?series_id=" + series_id;
}

function addAthlete() {
    window.location = "athlete.html?series_id=" + series_id + "&space_id=" + space_id;
}