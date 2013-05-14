var athlete;
var series;
var clubs;

function loadData() {
    if (series === undefined) {
        series = JSON.parse(localStorage.getItem("series"));
    }

    xhrGet("/jtaf/res/clubs", function(response) {
        clubs = JSON.parse(response);
    });

    var id = param().id;
    if (id === undefined) {
        athlete = new Object();
        el("athlete_lastName").focus();
    } else {
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

function back() {
    window.location = "series.html?id=" + series.id;
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
    fillClubSelect();
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

