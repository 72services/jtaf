var indexController = new IndexController();

function IndexController() {
    var util = new Util();

    var spaces;
    var userSpaces;
    var user;

    this.loadData = function() {
        header.activateLink("navigation_0");
        getCurrentUser();

        util.xhrGet("/jtaf/res/spaces", function(response) {
            spaces = JSON.parse(response);
            fillSpaces();
            util.i18n();
        });
        util.xhrGet("/jtaf/res/userspaces/current", function(response) {
            userSpaces = JSON.parse(response);
            fillSpaces();
        });
    };

    this.openSeriesRankingPdf = function(id) {
        var newtab = window.open();
        newtab.location = "/jtaf/res/reports/seriesranking?seriesid=" + id;
    };

    function getCurrentUser() {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "/jtaf/res/users/current", true);
        xhr.onload = function() {
            if (xhr.status === 200) {
                user = JSON.parse(xhr.response);
            } else if (xhr.status === 204) {
            } else {
                error(xhr.status);
            }
        };
        xhr.send();
    }

    function fillSpaces() {
        if (spaces !== undefined && spaces !== null) {
            var divSeries = document.getElementById("series");
            divSeries.innerHTML = "";

            spaces.forEach(function(space) {
                var ul_series = document.createElement("ul");
                ul_series.className = "series";
                divSeries.appendChild(ul_series);

                space.series.forEach(function(series) {
                    var li_series = document.createElement("li");
                    ul_series.appendChild(li_series);

                    var series_name = document.createElement("b");
                    series_name.className = "bigger";
                    series_name.style.paddingRight = "30px";
                    series_name.innerHTML = series.name;
                    li_series.appendChild(series_name);

                    var a_ranking = document.createElement("a");
                    a_ranking.href = "javascript:index.openSeriesRankingPdf(" + series.id + ");";
                    a_ranking.innerHTML = '<span class="i18n">Ranking</span>';
                    li_series.appendChild(a_ranking);

                    var ul_competitions = document.createElement("ul");
                    ul_competitions.className = "competition";
                    li_series.appendChild(ul_competitions);

                    series.competitions.forEach(function(competition) {
                        var li_competition = document.createElement("li");
                        var table = document.createElement("table");
                        table.style.width = "100%";
                        table.style.marginBottom = "20px";
                        var row = table.insertRow(0);
                        var cell0 = row.insertCell(0);
                        var name = document.createElement("b");
                        name.className = "bigger";
                        name.innerHTML = competition.name;
                        cell0.appendChild(name);
                        var cell1 = row.insertCell(1);
                        cell1.innerHTML = competition.competitionDate;
                        var cell2 = row.insertCell(2);
                        cell2.style.textAlign = "right";
                        cell2.innerHTML = '<span class="i18n">Athletes: </span>' + competition.numberOfAthletes;

                        var cell3 = row.insertCell(3);
                        cell3.style.textAlign = "right";

                        if (user != null && isUserGranted(user.email, series)) {
                            var a_results = document.createElement("a");
                            a_results.href = "input/results.html?id=" + competition.id + "&space_id=" + space.id;
                            a_results.innerHTML = '<span class="i18n">Enter results</span>';
                            cell3.appendChild(a_results);

                            cell3.appendChild(document.createTextNode(" "));
                        }

                        var a_ranking = document.createElement("a");
                        a_ranking.href = "competition_ranking.html?id=" + competition.id;
                        a_ranking.innerHTML = '<span class="i18n">Ranking</span>';
                        cell3.appendChild(a_ranking);

                        li_competition.appendChild(table);
                        ul_competitions.appendChild(li_competition);
                    });
                });
            });
        }
    }

    function isUserGranted(email, series) {
        for (var u in userSpaces) {
            var userSpace = userSpaces[u];
            if (userSpace.user.email === email
                    && userSpace.space.id === series.space_id) {
                return true;
            }
        }
        return false;
    }

}