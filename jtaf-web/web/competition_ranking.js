var ranking;

function loadData() {
    var id = param().id;
    if (id === undefined) {
    } else {
        xhrGet("/jtaf/res/rankings/competition/" + id, function(response) {
            parseAndFill(response);
        });
    }
}

function parseAndFill(response) {
    ranking = JSON.parse(response);
    el("title").appendChild(document.createTextNode("Ranking of " + ranking.competition.name));

    createTable();
}

function createTable() {
    for (var c in ranking.categories) {
        var category = ranking.categories[c];
        var table = document.createElement("table");
        table.setAttribute("style", "width: 100%");
        var row = document.createElement("tr");
        var cell = document.createElement("td");
        cell.setAttribute("colspan", 6);
        var title = document.createElement("h2");
        title.innerHTML = createCategoryTitle(category.category);
        cell.appendChild(title);
        row.appendChild(cell);
        table.appendChild(row);

        for (var a in category.athletes) {
            var athlete = category.athletes[a];
            row = document.createElement("tr");

            cell = document.createElement("td");
            var rank = parseInt(a) + 1;
            cell.innerHTML = rank + ".";
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.innerHTML = athlete.lastName;
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.innerHTML = athlete.firstName;
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.innerHTML = athlete.year;
            row.appendChild(cell);

            cell = document.createElement("td");
            if (athlete.club !== undefined && athlete.club !== null) {
                cell.innerHTML = athlete.club.abbreviation;
            }
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.innerHTML = calculateTotalPoints(athlete);
            row.appendChild(cell);

            table.appendChild(row);

            row = document.createElement("tr");

            cell = document.createElement("td");
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.setAttribute("style", "font-size: 11px");
            cell.setAttribute("colspan", 5);
            cell.innerHTML = createResultRow(athlete);
            row.appendChild(cell);

            table.appendChild(row);
        }
    }
    el("main").appendChild(table);
}

function calculateTotalPoints(athlete) {
    var total = 0;
    for (var r in athlete.results) {
        var result = athlete.results[r];
        total += result.points;
    }
    return total;
}

function createResultRow(athlete) {
    var text = "";
    var first = true;
    for (var r in athlete.results) {
        var result = athlete.results[r];
        if (!first) {
            text += "&nbsp;&nbsp;";
        }
        text += result.event.name + ": " + result.result;
        first = false;
    }
    return text;
}

function createCategoryTitle(category) {
    return category.abbreviation + " " + category.name + " " +
            category.yearFrom + " - " + category.yearTo;
}
