function CompetitionController() {
    var util = new Util();

    var competition;
    var series_id;
    var space_id;
    var readonly;

    this.loadData = function() {
        util.showMessage();

        series_id = util.searchMap.series_id;
        space_id = util.searchMap.space_id;
        readonly = util.searchMap.readonly;

        var id = util.searchMap.id;
        if (id === undefined) {
            competition = {};
            document.getElementById("competition_name").focus();
            util.i18n();
        } else {
            util.xhrGet("/res/competitions/" + id, function(response) {
                parseAndFill(response);
                util.i18n();
            });
        }
    };

    this.back = function() {
        window.location.href = "series.html?id=" + series_id + "&space_id=" + space_id;
    };

    this.save = function() {
        fillCompetition();
        util.xhrPost("/res/competitions/", function(response) {
            parseAndFill(response);
            window.location.href = "series.html?id=" + series_id + "&space_id=" + space_id + "&message=" + "Competition saved";
        }, competition);
    };

    this.isReadOnly = function() {
        return readonly;
    };

    function parseAndFill(response) {
        competition = JSON.parse(response);
        fillForm();
    }

    function fillForm() {
        document.getElementById("competition_id").value = competition.id;
        document.getElementById("competition_name").value = competition.name;
        document.getElementById("competition_date").value = competition.competitionDate;
        document.getElementById("competition_medalpercentage").value = competition.medalPercentage;
        document.getElementById("competition_alwaysthreemedals").checked = competition.alwaysThreeMedals;
        document.getElementById("competition_locked").checked = competition.locked;
        document.getElementById("competition_name").focus();

        document.getElementById("save").disabled = readonly;
    }

    function fillCompetition() {
        competition.name = document.getElementById("competition_name").value;
        competition.competitionDate = document.getElementById("competition_date").value;
        competition.medalPercentage = document.getElementById("competition_medalpercentage").value;
        competition.alwaysThreeMedals = document.getElementById("competition_alwaysthreemedals").checked;
        competition.locked = document.getElementById("competition_locked").checked;
        competition.series_id = series_id;
    }

}