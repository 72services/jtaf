var club;
var space_id;

function loadData() {
    space_id = searchMap.space_id;
    var id = searchMap.id;
    if (id === undefined) {
        club = new Object();
        document.getElementById("club_abbreviation").focus();
    } else {
        xhrGet("/jtaf/res/clubs/" + id, function(response) {
            parseAndFill(response);
        });
    }
}

function parseAndFill(response) {
    club = JSON.parse(response);
    fillForm();
}

function fillForm() {
    document.getElementById("club_id").value = club.id;
    document.getElementById("club_abbreviation").value = club.abbreviation;
    document.getElementById("club_name").value = club.name;
    document.getElementById("club_name").focus();
}

function save() {
    fillClub();
    xhrPost("/jtaf/res/clubs/", function(response) {
        parseAndFill(response);
        info("Club saved");
    }, club);
}

function fillClub() {
    club.abbreviation = document.getElementById("club_abbreviation").value;
    club.name = document.getElementById("club_name").value;
    club.space_id = space_id;
}

function deleteClub(id) {
    if (confirm(getString("Are you sure?"))) {
        xhrDelete("/jtaf/res/clubs/" + id, function() {
            loadData();
            info("Club deleted");
        });
    }
}

function back() {
    window.location = "space.html?id=" + space_id;
}