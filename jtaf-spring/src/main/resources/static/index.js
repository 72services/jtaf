function IndexController() {
    var util = new Util();

    var series;
    var userSpaces;
    var user;

    this.loadData = function () {
        util.showMessage();

        header.activateLink("navigation_0");
        getCurrentUser();

        util.xhrGet("/res/spaces/series", function (response) {
            series = JSON.parse(response);
            fillSeries();
            util.i18n();
        });
        util.xhrGet("/res/userspaces/current", function (response) {
            userSpaces = JSON.parse(response);
            fillSeries();
        });
    };

    function getCurrentUser() {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "/res/users/current", true);
        xhr.onload = function () {
            if (xhr.status === 200) {
                user = JSON.parse(xhr.responseText);
            } else if (xhr.status === 204) {
            } else {
                util.error(xhr.status);
            }
        };
        xhr.send();
    }

    function fillSeries() {
        if (series !== undefined && series !== null) {
            var divSeries = document.getElementById("series");
            divSeries.innerHTML = "";

            var ul_series = document.createElement("ul");
            ul_series.className = "series";
            divSeries.appendChild(ul_series);

            series.forEach(function (series) {
                if (!series.hide) {
                    var li_series = document.createElement("li");
                    ul_series.appendChild(li_series);

                    if (series.logo !== null) {
                        var series_logo = document.createElement("img");
                        series_logo.src = "/res/series/logo/" + series.id;
                        series_logo.style.paddingRight = "10px";
                        li_series.appendChild(series_logo);
                    }

                    var series_name = document.createElement("b");
                    series_name.className = "bigger";
                    series_name.style.paddingRight = "10px";
                    series_name.innerHTML = series.name;
                    li_series.appendChild(series_name);

                    var lock = document.createElement("img");
                    lock.style.paddingRight = "30px";
                    if (series.locked) {
                        lock.src = "images/locked.png";
                    }
                    else {
                        lock.src = "images/unlocked.png";
                    }
                    li_series.appendChild(lock);

                    var a_ranking = document.createElement("a");
                    a_ranking.href = "series_ranking.html?id=" + series.id;
                    a_ranking.innerHTML = '<span class="i18n">Ranking</span>';
                    li_series.appendChild(a_ranking);

                    var ul_competitions = document.createElement("ul");
                    ul_competitions.className = "competition";
                    li_series.appendChild(ul_competitions);

                    series.competitions.forEach(function (competition) {
                            var li_competition = document.createElement("li");
                            var table = document.createElement("table");
                            table.style.width = "100%";
                            table.style.marginBottom = "20px";
                            var row = table.insertRow(0);
                            var cell0 = row.insertCell(0);
                            var name = document.createElement("b");
                            name.className = "bigger";
                            name.style.paddingRight = "10px";
                            name.innerHTML = competition.name;
                            cell0.appendChild(name);
                            var lock = document.createElement("img");
                            if (competition.locked) {
                                lock.src = "images/locked.png";
                            }
                            else {
                                lock.src = "images/unlocked.png";
                            }
                            cell0.appendChild(lock);
                            var cell1 = row.insertCell(1);
                            cell1.innerHTML = competition.competitionDate;
                            var cell2 = row.insertCell(2);
                            cell2.style.textAlign = "right";
                            cell2.innerHTML = '<span class="i18n">Athletes</span> ' + competition.numberOfAthletesWithResults + '/' + competition.numberOfAthletes;

                            var cell3 = row.insertCell(3);
                            cell3.style.textAlign = "right";

                            if (!competition.locked && user !== undefined && user !== null && isUserGranted(user.email, series)) {
                                var a_results = document.createElement("a");
                                a_results.href = "input/results.html?id=" + competition.id + "&space_id=" + series.space_id;
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
                        }
                    );
                }
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