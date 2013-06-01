var athlete;
var clubs;
var series_id;
var space_id;

function loadData() {
    var id = searchMap.id;
    series_id = searchMap.series_id;
    space_id = searchMap.space_id;

    if (id === undefined) {
        fillClubSelect();
        athlete = new Object();
        document.getElementById("athlete_lastName").focus();
    } else {
        xhrGet("/jtaf/res/athletes/" + id, function(response) {
            parseAndFill(response);
        });
    }
}

function fillClubSelect() {
    xhrGet("/jtaf/res/clubs?space_id=" + space_id, function(response) {
        clubs = JSON.parse(response);
        var select = document.getElementById("athlete_club");
        select.innerHTML = "";

        var option = document.createElement("option");
        option.innerHTML = "";
        select.appendChild(option);

        clubs.forEach(function(club) {
            option = document.createElement("option");
            option.value = club.id;
            option.innerHTML = club.abbreviation;
            select.appendChild(option);
            if (athlete.club !== undefined && athlete.club !== null &&
                    athlete.club.id === club.id) {
                option.selected = true;
            }
        });
    });
}

function back() {
    window.location = "series.html?id=" + series_id;
}

function parseAndFill(response) {
    athlete = JSON.parse(response);
    fillForm();
}

function fillForm() {
    document.getElementById("athlete_id").value = athlete.id;
    document.getElementById("athlete_lastName").value = athlete.lastName;
    document.getElementById("athlete_lastName").focus();
    document.getElementById("athlete_firstName").value = athlete.firstName;
    document.getElementById("athlete_year").value = athlete.year;
    if (athlete.gender !== undefined && athlete.gender !== null) {
        document.getElementById("athlete_gender_" + athlete.gender).checked = true;
    } else {
        document.getElementById("athlete_gender_m").checked = false;
        document.getElementById("athlete_gender_f").checked = false;
    }
    document.getElementById("athlete_category").value = athlete.category.abbreviation;
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
    athlete.firstName = document.getElementById("athlete_firstName").value;
    athlete.lastName = document.getElementById("athlete_lastName").value;
    athlete.year = document.getElementById("athlete_year").value;
    if (document.getElementById("athlete_gender_m").checked) {
        athlete.gender = "m";
    } else {
        athlete.gender = "f";
    }
    var select = document.getElementById("athlete_club");
    var id = select.options[select.selectedIndex].value;
    if (id !== null) {
        clubs.forEach(function(club) {
            if (club.id === parseInt(id)) {
                athlete.club = club;
                return;
            }
        });
    }
    athlete.series_id = series_id;
}
