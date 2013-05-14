var athlete;
var series;
var competition;
var clubs;

function loadData() {
    var id = param().id;
    xhrGet("/jtaf/res/competitions/" + id, function(response) {
        competition = JSON.parse(response);
        el("title").innerHTML = competition.name;
    });

    if (series === undefined) {
        series = JSON.parse(localStorage.getItem("series"));
    }

    xhrGetSync("/jtaf/res/clubs", function(response) {
        clubs = JSON.parse(response);
    });

    el("search_id").focus();
}

function search() {
    if (event.keyCode === 13) {
        var id = el("search_id").value;
        xhrGet("/jtaf/res/athletes/" + id, function(response) {
            parseAndFill(response);
        });
    }
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
    el("athlete_id").value = athlete.id;
    el("athlete_lastName").value = athlete.lastName;
    el("athlete_lastName").focus();
    el("athlete_firstName").value = athlete.firstName;
    el("athlete_year").value = athlete.year;
    if (athlete.gender === "m") {
        el("athlete_gender_m").checked = true;
    }
    else {
        el("athlete_gender_f").checked = true;
    }
    el("athlete_category").value = athlete.category.abbreviation;
    fillClubSelect()
            ;
    if (athlete.category !== undefined
            && competition !== undefined
            && competition !== null) {
        el("results").setAttribute("style", "visbility: visible;");
        fillEventsTable();
    }
}

function fillEventsTable() {
    var table = el("athlete_events");
    table.innerHTML = "";

    if (athlete.category !== undefined) {
        for (var i in athlete.category.events) {
            var aevent = athlete.category.events[i];
            var row = table.insertRow(i);
            var cellName = row.insertCell(0);
            cellName.innerHTML = aevent.name;

            var cellResult = row.insertCell(1);
            var result = document.createElement("input");
            result.setAttribute("type", "text");
            result.id = "result" + i;
            result.setAttribute("pattern", "\\d+\\.\\d{2}");
            result.setAttribute("onblur", "calculatePoints(" + i + ")");
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
        }
        el("result0").focus();
    }
}

function calculatePoints(i) {
    var ev = athlete.category.events[i];
    var result = el("result" + i).value;

    var points = 0;
    if (ev.type === "run") {
        // A*((B-L)/100)^C
        points = ev.a * Math.pow((ev.b - result * 100) / 100, ev.c);
    }
    else if (ev.type === "throw") {
        // A*((L-B)/100)^C
        points = ev.a * Math.pow((result * 100 - ev.b) / 100, ev.c);
    }
    else if (ev.type === "jump") {
        // A*(L-B)^C
        points = ev.a * Math.pow((result * 100 - ev.b) / 100, ev.c);
    }
    points = Math.round(points);
    if (points < 0) {
        points = 0;
    }
    var resultObject = new Object();
    resultObject.result = result;
    resultObject.points = points;
    resultObject.event = ev;
    resultObject.competition = competition;
    athlete.results[i] = resultObject;
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
    athlete.series = series;
}

function deleteAthlete(id) {
    xhrDelete("/jtaf/res/athletes/" + id, function() {
        loadData();
        info("Athlete deleted");
    });
}
