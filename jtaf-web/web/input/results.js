var athlete;
var athletes;
var competition;
var clubs;
var space_id;

function loadData() {
    space_id = param().space_id;
    var id = param().id;

    xhrGet("/jtaf/res/competitions/" + id, function(response) {
        competition = JSON.parse(response);
        el("title").innerHTML = competition.name;
    });

    xhrGetSync("/jtaf/res/clubs?space_id=" + space_id, function(response) {
        clubs = JSON.parse(response);
    });

    el("search_term").focus();
}

function search() {
    clear();
    hideOutput();

    var searchterm = el("search_term").value;
    var number = parseInt(searchterm);
    if (number !== undefined && !isNaN(number) && typeof number === "number") {
        xhrGet("/jtaf/res/athletes/" + number, function(response) {
            parseAndFill(response);
        });
    } else {
        xhrGet("/jtaf/res/athletes/search?series_id=" + competition.series_id +
                "&query=" + searchterm, function(response) {
            parseAndFillAthletes(response);
        });
    }
}

function hideOutput() {
    el("input_form").className = "invisible";
    el("athlete_list").className = "invisible";
}

function fillClubSelect() {
    var select = el("athlete_club");
    for (var i in clubs) {
        var club = clubs[i];
        var option = document.createElement("option");
        option.value = club.id;
        option.innerHTML = club.abbreviation;
        select.appendChild(option);
        if (athlete.club !== undefined && athlete.club !== null &&
                athlete.club.id === club.id) {
            option.selected = true;
        }
    }
}

function parseAndFill(response) {
    athlete = JSON.parse(response);
    fillForm();
}

function fillForm() {
    el("input_form").className = "";

    el("athlete_id").value = athlete.id;
    el("athlete_lastName").value = athlete.lastName;
    el("athlete_lastName").focus();
    el("athlete_firstName").value = athlete.firstName;
    el("athlete_year").value = athlete.year;
    if (athlete.gender !== undefined && athlete.gender !== null) {
        el("athlete_gender_" + athlete.gender).checked = true;
    } else {
        el("athlete_gender_m").checked = false;
        el("athlete_gender_f").checked = false;
    }
    if (athlete.category !== undefined) {
        el("athlete_category").value = athlete.category.abbreviation;
        el("results").setAttribute("style", "visibility: visible;");
        fillEventsTable();
    } else {
        el("athlete_category").value = "";
        el("results").setAttribute("style", "visibility: hidden;");
    }
    fillClubSelect();
}

function fillEventsTable() {
    var table = el("athlete_events");
    table.innerHTML = "";
    if (athlete.category !== undefined) {
        var i = 0;
        athlete.category.events.forEach(function(aevent) {
            var row = table.insertRow(i);
            var cellName = row.insertCell(0);
            cellName.innerHTML = aevent.name;
            var cellResult = row.insertCell(1);
            var result = document.createElement("input");
            result.setAttribute("type", "text");
            result.id = "result" + i;
            result.setAttribute("pattern", "\\d+\\.\\d{2}");
            result.setAttribute("onblur", "calculatePoints(" + i + ")");
            //result.setAttribute("onkeyup", "mask('result" + i + "', '00.00', event);");
            cellResult.appendChild(result);
            var cellPoints = row.insertCell(2);
            var points = document.createElement("input");
            points.id = "points" + i;
            points.setAttribute("type", "text");
            points.setAttribute("readonly");
            points.setAttribute("disabled");
            points.setAttribute("style", "width: 100px");
            cellPoints.appendChild(points);
            if (athlete.results[i] !== undefined) {
                result.value = athlete.results[i].result;
                points.value = athlete.results[i].points;
            }
            i++;
        });
        el("result0").focus();
    }
}

function calculatePoints(i) {
    var ev = athlete.category.events[i];
    var result = el("result" + i).value;
    var points = 0;
    if (ev.type === "run") {
        points = ev.a * Math.pow((ev.b - result * 100) / 100, ev.c);
    } else if (ev.type === "run_long") {
        var parts = result.split(".");
        var time = parts[0] * 6000 + parts[1] * 100;
        points = ev.a * Math.pow((ev.b - time) / 100, ev.c);
    } else if (ev.type === "jump_throw") {
        points = ev.a * Math.pow((result * 100 - ev.b) / 100, ev.c);
    }
    points = Math.round(points);
    if (isNaN(points) || points < 0) {
        points = 0;
    }
    athlete.results[i] = {result: result, points: points, event: ev, competition: competition};
    el("points" + i).value = points;
}

function save() {
    fillAthlete();
    xhrPost("/jtaf/res/athletes/", function(response) {
        parseAndFill(response);
        info("Athlete saved");
    }, athlete);
}

function fillAthlete() {
    athlete.firstName = el("athlete_firstName").value;
    athlete.lastName = el("athlete_lastName").value;
    athlete.year = el("athlete_year").value;
    if (el("athlete_gender_m").checked) {
        athlete.gender = "m";
    } else {
        athlete.gender = "f";
    }
    athlete.series_id = competition.series_id;
}

function deleteAthlete(id) {
    xhrDelete("/jtaf/res/athletes/" + id, function() {
        loadData();
        info("Athlete deleted");
    });
}

function addAthlete() {
    clear();
}

function clear() {
    var error_div = el("error");
    if (error_div !== null) {
        document.body.removeChild(error_div);
    }
    athletes = null;
    athlete = {lastName: "", firstName: ""};
    var table = el("athlete_events");
    table.innerHTML = "";
    fillForm();
}

function parseAndFillAthletes(response) {
    el("athlete_list").className = "";

    athletes = JSON.parse(response);
    var table = el("athlete_table");
    table.innerHTML = "";
    if (athletes === undefined || athletes.length === 0) {
        var row = table.insertRow(0);
        var cellName = row.insertCell(0);
        cellName.innerHTML = "No athletes found";
        cellName.setAttribute("colspan", 7);
    } else {
        var i = 0;
        athletes.forEach(function(athlete) {
            var row = table.insertRow(i);
            var onclickEdit = "selectAthlete(" + athlete.id + ")";
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
                    ? athlete.club.name : "";
            cellClub.setAttribute("onclick", onclickEdit);
            i++;
        });
    }
}

function selectAthlete(id) {
    xhrGet("/jtaf/res/athletes/" + id, function(response) {
        el("athlete_list").className = "invisible";
        parseAndFill(response);
    });
}
