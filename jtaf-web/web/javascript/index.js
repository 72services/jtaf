var ascending = true;
var seriesList;
var competitions;

function loadData() {
    xhrGet("/jtaf/res/series?withCompetitions=true", function(response) {
        seriesList = JSON.parse(response);
        fillSeries();
    });
}

function fillSeries() {
    if (seriesList !== undefined && seriesList !== null) {
        var ul_series = el("ul_series");
        ul_series.innerHTML = "";

        for (var s in seriesList) {
            var series = seriesList[s];
            var li_series = document.createElement("li");

            var series_name = document.createElement("b");
            series_name.innerHTML = series.name;
            li_series.appendChild(series_name);

            li_series.appendChild(document.createElement("br"));
            li_series.appendChild(document.createElement("br"));

            var a_ranking = document.createElement("a");
            a_ranking.setAttribute("href",
                    "seriesRanking.html?id=" + series.id);
            a_ranking.innerHTML = "Ranking";
            li_series.appendChild(a_ranking);

            li_series.appendChild(document.createElement("br"));
            li_series.appendChild(document.createElement("br"));
            ul_series.appendChild(li_series);

            var ul_competitions = document.createElement("ul");
            ul_competitions.className = "competition";
            li_series.appendChild(ul_competitions);

            for (var c in series.competitions) {
                var competition = series.competitions[c];
                var li_competition = document.createElement("li");
                li_competition.innerHTML = "<b>" + competition.name
                        + "</b> " + competition.competitionDate;

                li_competition.appendChild(document.createElement("br"));
                li_competition.appendChild(document.createElement("br"));

                var a_results = document.createElement("a");
                a_results.setAttribute("href",
                        "resultinput/resultInput.html?id=" + competition.id);
                a_results.innerHTML = "Enter results";
                li_competition.appendChild(a_results);

                li_competition.appendChild(document.createTextNode(" "));

                var a_ranking = document.createElement("a");
                a_ranking.setAttribute("href",
                        "competitionRanking.html?id=" + competition.id);
                a_ranking.innerHTML = "Ranking";
                li_competition.appendChild(a_ranking);

                ul_competitions.appendChild(li_competition);
            }
        }
    }
}
