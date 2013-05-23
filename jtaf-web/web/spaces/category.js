var category;
var events;
var series_id;

function loadData() {
    series_id = param().series_id;
    xhrGetSync("/jtaf/res/events?series_id=" + series_id, function(response) {
        events = JSON.parse(response);
    });

    var id = param().id;
    if (id === undefined) {
        category = new Object();
        fillEventsTable();
        el("category_abbr").focus();
    } else {
        xhrGet("/jtaf/res/categories/" + series_id, function(response) {
            parseAndFill(response);
        });
    }
}

function back() {
    window.location = "series.html?id=" + series_id;
}

function parseAndFill(response) {
    category = JSON.parse(response);
    fillForm();
}

function fillForm() {
    el("category_id").value = category.id;
    el("category_abbr").value = category.abbreviation;
    el("category_name").value = category.name;
    el("category_yearFrom").value = category.yearFrom;
    el("category_yearTo").value = category.yearTo;
    if (category.gender === "m") {
        el("category_gender_m").checked = true;
    }
    else {
        el("category_gender_f").checked = true;
    }
    fillEventsTable();
    el("category_abbr").focus();
}

function fillEventsTable() {
    var table = el("category_events");
    table.innerHTML = "";

    for (var i = 0; i < 10; i++) {
        var row = table.insertRow(i);
        var cellNo = row.insertCell(0);
        cellNo.className = "noborderradus";
        cellNo.innerHTML = i + 1;
        var cellEvents = row.insertCell(1);
        cellEvents.className = "noborderradus";
        var select = document.createElement("select");
        select.id = "select" + i;
        var option = document.createElement("option");
        option.innerHTML = "";
        select.appendChild(option);
        events.forEach(function(ev) {
            option = document.createElement("option");
            option.value = ev.id;
            option.innerHTML = ev.name + " (" + ev.gender + ")";
            if (category.events !== undefined &&
                    category.events[i] !== undefined &&
                    category.events[i].id === ev.id) {
                option.selected = true;
            }
            select.appendChild(option);
        });
        cellEvents.appendChild(select);
    }
}

function save() {
    fillCategory();
    xhrPost("/jtaf/res/categories/", function(response) {
        parseAndFill(response);
        info("Category saved");
    }, category);
}

function fillCategory() {
    category.abbreviation = el("category_abbr").value;
    category.name = el("category_name").value;
    category.yearFrom = el("category_yearFrom").value;
    category.yearTo = el("category_yearTo").value;
    if (el("category_gender_m").checked) {
        category.gender = "m";
    } else {
        category.gender = "f";
    }
    fillCategoryEvents();
    category.series_id = series_id;
}

function fillCategoryEvents() {
    category.events = [];
    for (var i = 0; i < 10; i++) {
        var select = el("select" + i);
        var id = select.options[select.selectedIndex].value;
        events.forEach(function(ev) {
            if (ev.id == id) {
                category.events.push(ev);
            }
        });
    }
}
