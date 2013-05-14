var club;

function loadData() {
    var id = param().id;
    if (id === undefined) {
        club = new Object();
        el("club_abbreviation").focus();
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
    el("club_id").value = club.id;
    el("club_abbreviation").value = club.abbreviation;
    el("club_name").value = club.name;
    el("club_name").focus();
}

function save() {
    fillClub();
    xhrPost("/jtaf/res/clubs/", function(response) {
        parseAndFill(response);
        info("Club saved");
    }, club);
}

function fillClub() {
    club.abbreviation = el("club_abbreviation").value;
    club.name = el("club_name").value;
}

function deleteClub(id) {
    xhrDelete("/jtaf/res/clubs/" + id, function() {
        loadData();
        info("Club deleted");
    });
}
