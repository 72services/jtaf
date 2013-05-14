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

    el("search_term").focus();
}

function search() {
    if (event.keyCode === 13) {
        var error_div = el("error");
        if (error_div !== null) {
            document.body.removeChild(error_div);
        }
        var searchterm = el("search_term").value;
        var number = parseInt(searchterm);
        if (number !== undefined && !isNaN(number) && typeof number === "number") {
            xhrGet("/jtaf/res/athletes/" + number, function(response) {
                parseAndFill(response);
            });
        } else {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "/jtaf/res/athletes/search?query=" + searchterm, true);
            xhr.onload = function() {
                if (xhr.status === 200) {
                    parseAndFill(xhr.response);
                } else if (xhr.status === 404) {
                    clear();
                    el("search_term").focus();
                    error("No athlete matches your search criteria");
                } else {
                    error(xhr.status);
                }
            };
            xhr.send();
        }
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
    else if (athlete.gender === "f") {
        el("athlete_gender_f").checked = true;
    }
    if (athlete.category !== undefined) {
        el("athlete_category").value = athlete.category.abbreviation;
        el("results").setAttribute("style", "visibility: visible;");
        fillEventsTable();
    }
    else {
        el("athlete_category").value = "";
        el("results").setAttribute("style", "visibility: hidden;");
    }
    fillClubSelect();
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
    } else if (ev.type === "run_long") {
// A*((B-L)/100)^C
        var parts = result.split(".");
        var time = parts[0] * 6000 + parts[1] * 100;
        points = ev.a * Math.pow((ev.b - time) / 100, ev.c);
    } else if (ev.type === "jump_throw") {
// A*((L-B)/100)^C
        points = ev.a * Math.pow((result * 100 - ev.b) / 100, ev.c);
    }
    points = Math.round(points);
    if (isNaN(points) || points < 0) {
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

function addAthlete() {
    clear();
}

function clear() {
    athlete = new Object();
    athlete.id = null;
    athlete.firstName = null;
    athlete.lastName = null;
    athlete.category = undefined;
    var table = el("athlete_events");
    table.innerHTML = "";
    fillForm();
}