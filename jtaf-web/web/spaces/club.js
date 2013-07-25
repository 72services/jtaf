var clubController = new ClubController();

function ClubController() {
    var util = new Util();

    var club;
    var space_id;

    this.loadData = function() {
        space_id = util.searchMap.space_id;
        var id = util.searchMap.id;
        if (id === undefined) {
            club = new Object();
            document.getElementById("club_abbreviation").focus;
            util.i18n();
        } else {
            util.xhrGet("/jtaf/res/clubs/" + id, function(response) {
                parseAndFill(response);
                util.i18n();
            });
        }
    };

    this.save = function() {
        fillClub();
        util.xhrPost("/jtaf/res/clubs/", function(response) {
            parseAndFill(response);
            info("Club saved");
        }, club);
    };

    this.back = function() {
        window.location = "space.html?id=" + space_id;
    };

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

    function fillClub() {
        club.abbreviation = document.getElementById("club_abbreviation").value;
        club.name = document.getElementById("club_name").value;
        club.space_id = space_id;
    }

}