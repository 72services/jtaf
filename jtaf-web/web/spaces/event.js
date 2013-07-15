var jtafEvent;
var series_id;

function loadData() {
    series_id = searchMap.series_id;

    var id = searchMap.id;
    if (id === undefined) {
        jtafEvent = new Object();
        document.getElementById("event_name").focus();
        i18n();
    } else {
        xhrGet("/jtaf/res/events/" + id, function(response) {
            parseAndFill(response);
            i18n();
        });
    }
}

function back() {
    window.location = "series.html?id=" + series_id;
}

function parseAndFill(response) {
    jtafEvent = JSON.parse(response);
    fillForm();
}

function fillForm() {
    document.getElementById("event_id").value = jtafEvent.id;
    document.getElementById("event_name").value = jtafEvent.name;
    var index = 0;
    switch (jtafEvent.type) {
        case "run_long":
            index = 1;
            break;
        case "jump_throw":
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
}

function save() {
    fillEvent();
    xhrPost("/jtaf/res/events/", function(response) {
        parseAndFill(response);
        info("Event saved");
    }, jtafEvent);
}

function fillEvent() {
    jtafEvent.name = document.getElementById("event_name").value;
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

function deleteEvent(id) {
    if (confirm(translate("Are you sure?"))) {
        xhrDelete("/jtaf/res/events/" + id, function() {
            loadData();
            info("Event deleted");
        });
    }
}
