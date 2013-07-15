var competition;
var series_id;

function loadData() {
    series_id = searchMap.series_id;
    var id = searchMap.id;
    if (id === undefined) {
        competition = new Object();
        document.getElementById("competition_name").focus();
        i18n();
    } else {
        xhrGet("/jtaf/res/competitions/" + id, function(response) {
            parseAndFill(response);
            i18n();
        });
    }
}

function back() {
    window.location = "series.html?id=" + series_id;
}

function parseAndFill(response) {
    competition = JSON.parse(response);
    fillForm();
}

function fillForm() {
    document.getElementById("competition_id").value = competition.id;
    document.getElementById("competition_name").value = competition.name;
    document.getElementById("competition_date").value = competition.competitionDate;
    document.getElementById("competition_name").focus();
}

function save() {
    fillCompetition();
    xhrPost("/jtaf/res/competitions/", function(response) {
        parseAndFill(response);
        info("Competition saved");
    }, competition);
}

function fillCompetition() {
    competition.name = document.getElementById("competition_name").value;
    competition.competitionDate = document.getElementById("competition_date").value;
    competition.series_id = series_id;
}

function deleteCompetition(id) {
    if (confirm(translate("Are you sure?"))) {
        xhrDelete("/jtaf/res/competitions/" + id, function() {
            loadData();
            info("Competition deleted");
        });
    }
}
