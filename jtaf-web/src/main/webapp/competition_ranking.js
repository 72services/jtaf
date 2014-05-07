function CompetitionsRankingController() {
    var util = new Util();
    var ranking;
    var competition_id;

    this.loadData = function() {
        util.showMessage();
        util.showLoading();

        competition_id = util.searchMap.id;
        if (competition_id === undefined) {
        } else {
            util.xhrGet("/jtaf/res/rankings/competition/" + competition_id, function(response) {
                parseAndFill(response);
                util.i18n();
                util.hideLoading();
            });
        }
    };

    this.openAsPdf = function() {
        var newtab = window.open();
        newtab.location = "/jtaf/res/rankings/competition/pdf/" + ranking.competition.id;
    };


    this.openAsCsv = function() {
        var newtab = window.open();
        newtab.location = "/jtaf/res/reports/export.csv?competitionid=" + ranking.competition.id;
    };

    this.createEventsRanking = function() {
        var newtab = window.open();
        newtab.location = "/jtaf/res/rankings/events/" + ranking.competition.id;
    };

    function parseAndFill(response) {
        ranking = JSON.parse(response);

        var table = document.createElement("table");
        table.style.width = "100%";
        var row = table.insertRow(0);
        var left = row.insertCell(0);
        var hleft = document.createElement("h1");
        hleft.innerHTML = '<span class="i18n">Ranking</span>';
        left.appendChild(hleft);

        var middle = row.insertCell(1);
        middle.style.textAlign = "center";
        var hmiddle = document.createElement("h1");
        hmiddle.innerHTML = ranking.competition.name;
        middle.appendChild(hmiddle);

        var right = row.insertCell(2);
        right.style.textAlign = "right";
        var hright = document.createElement("h1");
        var d = ranking.competition.competitionDate;
        hright.innerHTML = d.substring(8, 10) + "." + d.substring(5, 7) + "." + d.substring(0, 4);
        right.appendChild(hright);

        document.getElementById("title").appendChild(table);

        createTable();
    }

    function createTable() {
        ranking.categories.forEach(function(category) {
            var table = document.createElement("table");
            table.className = "ranking";
            var row = document.createElement("tr");
            var cell = document.createElement("td");
            cell.setAttribute("colspan", 6);

            var title = document.createElement("h3");
            title.innerHTML = createCategoryTitle(category.category);
            cell.appendChild(title);
            row.appendChild(cell);
            table.appendChild(row);

            var numberOfMedals = calculateNumberOfMedals(category);
            var rank = 1;
            category.athletes.forEach(function(athlete) {
                row = document.createElement("tr");

                cell = document.createElement("td");
                cell.style.width = "40px";
                if (rank <= numberOfMedals) {
                    cell.innerHTML = "* " + rank + ".";
                } else {
                    cell.innerHTML = rank + ".";
                }
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.style.width = "200px";
                cell.innerHTML = athlete.lastName;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.style.width = "200px";
                cell.innerHTML = athlete.firstName;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.style.width = "50px";
                cell.innerHTML = athlete.year;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.style.width = "150px";
                if (athlete.club !== undefined && athlete.club !== null) {
                    cell.innerHTML = athlete.club.abbreviation;
                }
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.style.textAlign = "right";
                cell.innerHTML = calculateTotalPoints(athlete);
                row.appendChild(cell);

                table.appendChild(row);

                row = document.createElement("tr");

                cell = document.createElement("td");
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.className = "small";
                cell.setAttribute("colspan", 5);
                cell.innerHTML = createResultRow(athlete);
                row.appendChild(cell);

                table.appendChild(row);

                rank++;
            });
            document.getElementById("ranking").appendChild(table);
        });

    }

    function calculateNumberOfMedals(category) {
        var numberOfMedals = 0;
        if (ranking.competition.medalPercentage !== null
                && ranking.competition.medalPercentage > 0) {
            var percentage = ranking.competition.medalPercentage;
            numberOfMedals = category.athletes.length * (percentage / 100);
            if (numberOfMedals < 3) {
                numberOfMedals = 3;
            }
        }
        return numberOfMedals;
    }

    function calculateTotalPoints(athlete) {
        var total = 0;
        athlete.results.forEach(function(result) {
            if (result.competition.id === competition_id) {
                total += result.points;
            }
        });
        return total;
    }

    function createResultRow(athlete) {
        var text = "";
        var first = true;
        athlete.results.forEach(function(result) {
            if (!first) {
                text += "&nbsp;&nbsp;";
            }
            text += result.event.name + ": " + result.result + " (" + result.points + ")";
            first = false;
        });
        return text;
    }

    function createCategoryTitle(category) {
        return category.abbreviation + " " + category.name + " " +
                category.yearFrom + " - " + category.yearTo;
    }

}
