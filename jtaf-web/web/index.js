var spaces;

function loadData() {
    activateLink("navigation_0");
    xhrGet("/jtaf/res/spaces", function(response) {
        spaces = JSON.parse(response);
        fillSpaces();
    });
}

function fillSpaces() {
    if (spaces !== undefined && spaces !== null) {
        var ul_spaces = el("ul_spaces");
        ul_spaces.innerHTML = "";

        spaces.forEach(function(space) {
            var li_space = document.createElement("li");
            ul_spaces.appendChild(li_space);

            var space_name = document.createElement("b");
            space_name.innerHTML = space.name;
            li_space.appendChild(space_name);

            li_space.appendChild(document.createElement("br"));
            li_space.appendChild(document.createElement("br"));
            li_space.appendChild(document.createElement("br"));


            var ul_series = document.createElement("ul");
            ul_series.className = "series";
            li_space.appendChild(ul_series);

            space.series.forEach(function(series) {
                var li_series = document.createElement("li");
                ul_series.appendChild(li_series);

                var series_name = document.createElement("b");
                series_name.innerHTML = series.name;
                li_series.appendChild(series_name);

                li_series.appendChild(document.createElement("br"));
                li_series.appendChild(document.createElement("br"));
                li_series.appendChild(document.createElement("br"));

                var a_ranking = document.createElement("a");
                a_ranking.href = "javascript:openSeriesRankingPdf(" + series.id + ");";
                a_ranking.innerHTML = "Ranking";
                li_series.appendChild(a_ranking);

                li_series.appendChild(document.createElement("br"));
                li_series.appendChild(document.createElement("br"));
                li_series.appendChild(document.createElement("br"));

                var ul_competitions = document.createElement("ul");
                ul_competitions.className = "competition";
                li_series.appendChild(ul_competitions);

                series.competitions.forEach(function(competition) {
                    var li_competition = document.createElement("li");
                    var table = document.createElement("table");
                    table.style.width = "100%";
                    var row = table.insertRow(0);
                    var cell0 = row.insertCell(0);
                    cell0.innerHTML = "<b>" + competition.name + "</b> ";
                    var cell1 = row.insertCell(1);
                    cell1.innerHTML = competition.competitionDate;
                    var cell2 = row.insertCell(2);
                    cell2.style.textAlign = "right;";
                    cell2.innerHTML = "Athletes: " + competition.numberOfAthletes;
                    li_competition.appendChild(table);

                    li_competition.appendChild(document.createElement("br"));
                    li_competition.appendChild(document.createElement("br"));

                    var a_results = document.createElement("a");
                    a_results.href = "resultinput/result_input.html?id=" + competition.id;
                    a_results.innerHTML = "Enter results";
                    li_competition.appendChild(a_results);

                    li_competition.appendChild(document.createTextNode(" "));

                    var a_ranking = document.createElement("a");
                    a_ranking.href = "competition_ranking.html?id=" + competition.id;
                    a_ranking.innerHTML = "Ranking";
                    li_competition.appendChild(a_ranking);

                    ul_competitions.appendChild(li_competition);
                });
            });
        });
    }
}

function openSeriesRankingPdf(id) {
    var newtab = window.open();
    newtab.location = "/jtaf/res/reports/seriesranking?seriesid=" + id;
}