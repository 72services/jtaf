function EventController() {
    var util = new Util();

    var jtafEvent;
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
            jtafEvent = new Object();
            document.getElementById("event_name").focus();
            util.i18n();
        } else {
            util.xhrGet("/jtaf/res/events/" + id, function(response) {
                parseAndFill(response);
                util.i18n();
            });
        }
    };

    this.save = function() {
        fillEvent();
        util.xhrPost("/jtaf/res/events/", function(response) {
            parseAndFill(response);
            window.location = "series.html?id=" + series_id + "&space_id=" + space_id + "&message=" + "Event saved";
        }, jtafEvent);
    };

    this.back = function() {
        window.location = "series.html?id=" + series_id + "&space_id=" + space_id
    };

    function parseAndFill(response) {
        jtafEvent = JSON.parse(response);
        fillForm();
    }

    function fillForm() {
        document.getElementById("event_id").value = jtafEvent.id;
        document.getElementById("event_name").value = jtafEvent.name;
        document.getElementById("event_longname").value = jtafEvent.longName;
        var index = 0;
        switch (jtafEvent.type) {
            case "RUN_LONG":
                index = 1;
                break;
            case "JUMP_THROW":
                index = 2;
                break;
        }
        document.getElementById("event_type").options[index].selected = true;
        if (jtafEvent.gender === "m") {
            document.getElementById("event_gender_m").checked = true;
        }
        else {
            document.getElementById("event_gender_f").checked = true;
        }
        document.getElementById("event_a").value = jtafEvent.a;
        document.getElementById("event_b").value = jtafEvent.b;
        document.getElementById("event_c").value = jtafEvent.c;
        document.getElementById("event_name").focus();
        
        document.getElementById("save").disabled = readonly;
    }

    function fillEvent() {
        jtafEvent.name = document.getElementById("event_name").value;
        jtafEvent.longName = document.getElementById("event_longname").value;
        jtafEvent.type = document.getElementById("event_type").value;
        if (document.getElementById("event_gender_m").checked) {
            jtafEvent.gender = "m";
        }
        else {
            jtafEvent.gender = "f";
        }
        jtafEvent.a = document.getElementById("event_a").value;
        jtafEvent.b = document.getElementById("event_b").value;
        jtafEvent.c = document.getElementById("event_c").value;
        jtafEvent.series_id = series_id;
    }

}