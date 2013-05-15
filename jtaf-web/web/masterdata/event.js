var jtafEvent;
var series;

function loadData() {
    series = JSON.parse(localStorage.getItem("series"));

    var id = param().id;
    if (id === undefined) {
        jtafEvent = new Object();
        el("event_name").focus();
    } else {
        xhrGet("/jtaf/res/events/" + id, function(response) {
            parseAndFill(response);
        });
    }
}

function back() {
    window.location = "series.html?id=" + series.id;
}

function parseAndFill(response) {
    jtafEvent = JSON.parse(response);
    fillForm();
}

function fillForm() {
    el("event_id").value = jtafEvent.id;
    el("event_name").value = jtafEvent.name;
    var index = 0;
    switch (jtafEvent.type) {
        case "run_long":
            index = 1;
            break;
        case "jump_throw":
            index = 2;
            break;
    }
    el("event_type").options[index].selected = true;
    if (jtafEvent.gender === "m") {
        el("event_gender_m").checked = true;
    }
    else {
        el("event_gender_f").checked = true;
    }
    el("event_a").value = jtafEvent.a;
    el("event_b").value = jtafEvent.b;
    el("event_c").value = jtafEvent.c;
    el("event_name").focus();
}

function save() {
    fillEvent();
    xhrPost("/jtaf/res/events/", function(response) {
        parseAndFill(response);
        info("Event saved");
    }, jtafEvent);
}

function fillEvent() {
    jtafEvent.name = el("event_name").value;
    jtafEvent.type = el("event_type").value;
    if (el("event_gender_m").checked) {
        jtafEvent.gender = "m";
    }
    else {
        jtafEvent.gender = "f";
    }
    jtafEvent.a = el("event_a").value;
    jtafEvent.b = el("event_b").value;
    jtafEvent.c = el("event_c").value;
    jtafEvent.series = series;
}

function deleteEvent(id) {
    xhrDelete("/jtaf/res/events/" + id, function() {
        loadData();
        info("Event deleted");
    });
}
