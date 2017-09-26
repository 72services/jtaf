function ClubController() {
    var util = new Util();

    var club;
    var space_id;
    var readonly;

    this.loadData = function() {
        util.showMessage();

        space_id = util.searchMap.space_id;
        readonly = util.searchMap.readonly;
        var id = util.searchMap.id;
        
        if (id === undefined) {
            club = {};
            //noinspection BadExpressionStatementJS
            document.getElementById("club_abbreviation").focus;
            util.i18n();
        } else {
            util.xhrGet("/res/clubs/" + id, function(response) {
                parseAndFill(response);
                util.i18n();
            });
        }
    };

    this.save = function() {
        fillClub();
        util.xhrPost("/res/clubs/", function(response) {
            parseAndFill(response);
            window.location.href = "space.html?id=" + space_id + "&message=" + "Club saved";
        }, club);
    };

    this.back = function() {
        window.location.href = "space.html?id=" + space_id;
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
        
        document.getElementById("save").disabled = readonly;
    }

    function fillClub() {
        club.abbreviation = document.getElementById("club_abbreviation").value;
        club.name = document.getElementById("club_name").value;
        club.space_id = space_id;
    }

}
