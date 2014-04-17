function CompetitionController() {
    var util = new Util();

    var competition;
    var series_id;
    var space_id;

    this.loadData = function() {
        util.showMessage();

        series_id = util.searchMap.series_id;
        space_id = util.searchMap.space_id;
        
        var id = util.searchMap.id;
        if (id === undefined) {
            competition = new Object();
            document.getElementById("competition_name").focus();
            util.i18n();
        } else {
            util.xhrGet("/jtaf/res/competitions/" + id, function(response) {
                parseAndFill(response);
                util.i18n();
            });
        }
    };

    this.back = function() {
        window.location = "series.html?id=" + series_id + "&space_id=" + space_id;
    };

    this.save = function() {
        fillCompetition();
        util.xhrPost("/jtaf/res/competitions/", function(response) {
            parseAndFill(response);
            window.location = "series.html?id=" + series_id + "&space_id=" + space_id + "&message=" + "Competition saved";
        }, competition);
    };

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

    function fillCompetition() {
        competition.name = document.getElementById("competition_name").value;
        competition.competitionDate = document.getElementById("competition_date").value;
        competition.series_id = series_id;
    }

}

var competitionController = new CompetitionController();