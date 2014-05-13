function SeriesController() {
    var util = new Util();

    var series;
    var athletes;
    var ascending = true;
    var space_id;
    var series_id;

    this.loadData = function() {
        util.showMessage();

        space_id = util.searchMap.space_id;
        series_id = util.searchMap.id;

        if (series_id === undefined) {
            series = new Object();
            document.getElementById("series_name").focus();
            util.i18n();
        } else {
            util.xhrGet("/jtaf/res/series/" + series_id, function(response) {
                parseAndFillSeries(response);
                util.i18n();

                util.xhrGet("/jtaf/res/categories?series_id=" + series_id, function(response) {
                    parseAndFillCategories(response);
                    util.i18n();
                });
                util.xhrGet("/jtaf/res/events?series_id=" + series_id, function(response) {
                    parseAndFillEvents(response);
                    util.i18n();
                });
                util.xhrGet("/jtaf/res/athletes?series_id=" + series_id, function(response) {
                    parseAndFillAthletes(response);
                    util.i18n();
                });
            });
        }
        var logoimage = document.getElementById("logoimage");
        logoimage.src = "/jtaf/res/series/logo/" + series_id;

        var active_tab = localStorage.getItem("active_tab");
        if (active_tab !== undefined && active_tab !== null) {
            this.switchTo(active_tab);
        }
    };

    this.save = function() {
        fillSeries();
        util.xhrPost("/jtaf/res/series/", function(response) {
            parseAndFillSeries(response);
            util.info("Series saved");
        }, series);
    };

    this.uploadLogo = function() {
        var iframe = document.createElement("iframe");
        iframe.name = "myTarget";
        iframe.addEventListener("load", function() {
            util.info("Logo replaced");
            window.location.reload();
        });
        iframe.style.display = "none";
        document.body.appendChild(iframe);

        var form = document.getElementById("logo_upload");
        form.target = iframe.name;
        form.setAttribute("action", "/jtaf/res/series/upload/" + series_id);
        form.submit();
    };

    this.refreshSeries = function() {
        util.xhrGet("/jtaf/res/series/" + series_id, function(response) {
            series = JSON.parse(response);
            util.info("Logo updated");
        });
    };

    this.addCompetition = function() {
        window.location = "competition.html?series_id=" + series_id + "&space_id=" + space_id;
    };

    this.addEvent = function() {
        window.location = "event.html?series_id=" + series_id + "&space_id=" + space_id;
    };

    this.addCategory = function() {
        window.location = "category.html?series_id=" + series_id + "&space_id=" + space_id;
    };

    this.addAthlete = function() {
        window.location = "athlete.html?series_id=" + series_id + "&space_id=" + space_id;
    };

    this.filter = function(property) {
        var filteredAthletes = new Array();
        var searchString = document.getElementById(property).value;
        if (searchString !== "") {
            var j = 0;
            athletes.forEach(function(athlete) {
                var valueToCompare = athlete[property];
                if (property === 'yearOfBirth') {
                    valueToCompare = "" + valueToCompare;
                }
                if (valueToCompare !== null && valueToCompare.toLowerCase().indexOf(searchString.toLowerCase()) !== -1) {
                    filteredAthletes[j] = athlete;
                    j++;
                }
            });
            fillAthletesTable(filteredAthletes);
        } else {
            fillAthletesTable(athletes);
        }
    }
    ;

    this.sortBy = function(property) {
        if (event.srcElement.type === undefined) {
            athletes.sort(util.createComparator(property));
            if (ascending) {
                athletes.reverse();
            }
            ascending = !ascending;
            fillAthletesTable(athletes);
        }
    };

    this.deleteCompetition = function(id) {
        if (confirm(util.translate("Are you sure?"))) {
            util.xhrDelete("/jtaf/res/competitions/" + id, function() {
                seriesController.loadData();
                util.info("Competition deleted");
            });
        }
    };

    this.deleteEvent = function(id) {
        if (confirm(util.translate("Are you sure?"))) {
            util.xhrDelete("/jtaf/res/events/" + id, function() {
                seriesController.loadData();
                util.info("Event deleted");
            });
        }
    };

    this.deleteAthlete = function(id) {
        if (confirm(util.translate("Are you sure?"))) {
            util.xhrDelete("/jtaf/res/athletes/" + id, function() {
                seriesController.loadData();
                util.info("Athlete deleted");
            });
        }
    };

    this.deleteCategory = function(id) {
        if (confirm(util.translate("Are you sure?"))) {
            util.xhrDelete("/jtaf/res/categories/" + id, function() {
                seriesController.loadData();
                util.info("Category deleted");
            });
        }
    };

    this.switchTo = function(div) {
        var els = document.getElementsByClassName("visible");
        for (var i in els) {
            if (els[i] !== undefined) {
                els[i].className = "invisible";
            }
        }
        document.getElementById(div).className = "visible";

        document.getElementById("a_competitions").className = "tab_inactive";
        document.getElementById("a_events").className = "tab_inactive";
        document.getElementById("a_categories").className = "tab_inactive";
        document.getElementById("a_athletes").className = "tab_inactive";

        document.getElementById("a_" + div).className = "tab_active";
        localStorage.setItem("active_tab", div);
    };

    this.back = function() {
        localStorage.removeItem("active_tab");
        window.location = "space.html?id=" + space_id;
    };

    this.recalculateCategories = function() {
        if (confirm(util.translate("All results of this series will be deleted!\nAre you sure?"))) {
            util.xhrPost("/jtaf/res/series/recalculateCategories", function(response) {
                seriesController.loadData();
                util.info("Categories recalulated for all athletes");
            }, series);
        }
    };

    function parseAndFillSeries(response) {
        series = JSON.parse(response);

        fillForm();
        fillCompetitionTable();
    }

    function fillForm() {
        document.getElementById("series_id").value = series.id;
        document.getElementById("series_name").value = series.name;
        document.getElementById("series_locked").checked = series.locked;
        document.getElementById("series_name").focus();
    }

    function fillCompetitionTable() {
        var table = document.getElementById("competition_table");
        table.innerHTML = "";
        if (series.competitions === undefined || series.competitions.length === 0) {
            var row = table.insertRow(0);
            var cellName = row.insertCell(0);
            cellName.innerHTML = "No competitions found";
            cellName.setAttribute("colspan", 3);
        }
        else {
            var i = 0;
            series.competitions.forEach(function(competition) {
                var row = table.insertRow(i);
                var onclickEdit = "window.location = 'competition.html?id="
                        + competition.id + "&series_id=" + series_id
                        + "&space_id=" + space_id + "&readonly=" + series.locked + "'";

                var cellName = row.insertCell(0);
                cellName.className = "edit";
                cellName.innerHTML = competition.name;
                cellName.setAttribute("onclick", onclickEdit);

                var cellDate = row.insertCell(1);
                cellDate.className = "edit";
                cellDate.innerHTML = competition.competitionDate;
                cellDate.setAttribute("onclick", onclickEdit);

                var sheet = document.createElement("a");
                sheet.setAttribute("href", "/jtaf/res/reports/sheets?competitionid=" + competition.id);
                sheet.setAttribute("target", "_blank");
                sheet.appendChild(document.createTextNode(util.translate("Sheets")));

                var sheetOrderByClub = document.createElement("a");
                sheetOrderByClub.setAttribute("href", "/jtaf/res/reports/sheets?orderby=club&competitionid=" + competition.id);
                sheetOrderByClub.setAttribute("target", "_blank");
                sheetOrderByClub.appendChild(document.createTextNode(util.translate("(order by club)")));

                var numbers = document.createElement("a");
                numbers.setAttribute("href", "/jtaf/res/reports/numbers?competitionid=" + competition.id + "&orderby=club");
                numbers.setAttribute("target", "_blank");
                numbers.appendChild(document.createTextNode(util.translate("Numbers")));

                var del = document.createElement("a");
                del.setAttribute("href", "#");
                del.setAttribute("onclick", "seriesController.deleteCompetition(" +
                        competition.id + ")");

                var delSpan = document.createElement("span");
                delSpan.className = "i18n";
                delSpan.innerHTML = "Delete";
                del.appendChild(delSpan);

                var cellFunction = row.insertCell(2);
                cellFunction.style.textAlign = "right";
                cellFunction.appendChild(sheet);
                cellFunction.appendChild(document.createTextNode(" "));
                cellFunction.appendChild(sheetOrderByClub);
                cellFunction.appendChild(document.createTextNode("  "));
                cellFunction.appendChild(numbers);
                if (!series.locked) {
                    cellFunction.appendChild(document.createTextNode(" "));
                    cellFunction.appendChild(del);
                }
                cellFunction.appendChild(document.createTextNode(" "));
                var lock = document.createElement("img");
                if (competition.locked) {
                    lock.src = "../images/locked.png";
                }
                else {
                    lock.src = "../images/unlocked.png";
                }
                cellFunction.appendChild(lock);
                i++;
            });
        }
    }

    function parseAndFillCategories(response) {
        var categories = JSON.parse(response);
        var table = document.getElementById("category_table");
        table.innerHTML = "";
        if (categories === undefined || categories.length === 0) {
            var row = table.insertRow(0);
            var cellName = row.insertCell(0);
            cellName.innerHTML = util.translate("No categories found");
            cellName.setAttribute("colspan", 6);
        }
        else {
            var i = 0;
            categories.forEach(function(category) {
                var row = table.insertRow(i);
                var onclickEdit = "window.location = 'category.html?id="
                        + category.id + "&series_id=" + series_id
                        + "&space_id=" + space_id + "&readonly=" + series.locked + "'";
                var cellAbbr = row.insertCell(0);
                cellAbbr.className = "edit";
                cellAbbr.innerHTML = category.abbreviation;
                cellAbbr.setAttribute("onclick", onclickEdit);
                var cellName = row.insertCell(1);
                cellName.className = "edit";
                cellName.innerHTML = category.name;
                cellName.setAttribute("onclick", onclickEdit);
                var cellYearFrom = row.insertCell(2);
                cellYearFrom.className = "edit";
                cellYearFrom.innerHTML = category.yearFrom;
                cellYearFrom.setAttribute("onclick", onclickEdit);
                var cellYearTo = row.insertCell(3);
                cellYearTo.className = "edit";
                cellYearTo.innerHTML = category.yearTo;
                cellYearTo.setAttribute("onclick", onclickEdit);
                var cellGender = row.insertCell(4);
                cellGender.className = "edit";
                cellGender.innerHTML = category.gender;
                cellGender.setAttribute("onclick", onclickEdit);
                var sheet = document.createElement("a");
                sheet.setAttribute("href", "/jtaf/res/reports/sheet?categoryid=" + category.id);
                sheet.setAttribute("target", "_blank");
                var sheetSpan = document.createElement("span");
                sheetSpan.className = "i18n";
                sheetSpan.innerHTML = "Sheet";
                sheet.appendChild(sheetSpan);
                var del = document.createElement("a");
                del.setAttribute("href", "#");
                del.setAttribute("onclick", "seriesController.deleteCategory(" +
                        category.id + ")");
                var delSpan = document.createElement("span");
                delSpan.className = "i18n";
                delSpan.innerHTML = "Delete";
                del.appendChild(delSpan);
                var cellFunction = row.insertCell(5);
                cellFunction.setAttribute("style", "width: 150px; text-align: right;");
                cellFunction.appendChild(sheet);
                if (!series.locked) {
                    cellFunction.appendChild(document.createTextNode(" "));
                    cellFunction.appendChild(del);
                }
                i++;
            });
        }
    }

    function parseAndFillEvents(response) {
        var events = JSON.parse(response);
        var table = document.getElementById("event_table");
        table.innerHTML = "";
        if (events === undefined || events.length === 0) {
            var row = table.insertRow(0);
            var cellName = row.insertCell(0);
            cellName.innerHTML = util.translate("No events found");
            cellName.setAttribute("colspan", 8);
        }
        else {
            var i = 0;
            events.forEach(function(event) {
                var row = table.insertRow(i);
                var onclickEdit = "window.location = 'event.html?id="
                        + event.id + "&series_id=" + series_id + "&space_id="
                        + space_id + "&readonly=" + series.locked + "'";
                var cellName = row.insertCell(0);
                cellName.className = "edit";
                cellName.innerHTML = event.name;
                cellName.setAttribute("onclick", onclickEdit);
                var cellLongName = row.insertCell(1);
                cellLongName.className = "edit";
                cellLongName.innerHTML = event.longName;
                cellLongName.setAttribute("onclick", onclickEdit);
                var cellType = row.insertCell(2);
                cellType.className = "edit";
                cellType.innerHTML = event.type;
                cellType.setAttribute("onclick", onclickEdit);
                var cellGender = row.insertCell(3);
                cellGender.className = "edit";
                cellGender.innerHTML = event.gender;
                cellGender.setAttribute("onclick", onclickEdit);
                var cellA = row.insertCell(4);
                cellA.className = "edit";
                cellA.innerHTML = event.a;
                cellA.setAttribute("onclick", onclickEdit);
                var cellB = row.insertCell(5);
                cellB.className = "edit";
                cellB.innerHTML = event.b;
                cellB.setAttribute("onclick", onclickEdit);
                var cellC = row.insertCell(6);
                cellC.className = "edit";
                cellC.innerHTML = event.c;
                cellC.setAttribute("onclick", onclickEdit);
                var del = document.createElement("a");
                del.setAttribute("href", "#");
                del.setAttribute("onclick", "seriesController.deleteEvent(" + event.id + ")");
                var delSpan = document.createElement("span");
                delSpan.className = "i18n";
                delSpan.innerHTML = "Delete";
                del.appendChild(delSpan);
                var cellFunction = row.insertCell(7);
                cellFunction.setAttribute("style", "text-align: right;");
                if (!series.locked) {
                    cellFunction.appendChild(del);
                }
                i++;
            });
        }
    }

    function parseAndFillAthletes(response) {
        athletes = JSON.parse(response);
        if (athletes === undefined || athletes.length === 0) {
            var table = document.getElementById("athlete_table");
            table.innerHTML = "";
            var row = table.insertRow(0);
            var cellName = row.insertCell(0);
            cellName.innerHTML = util.translate("No athletes found");
            cellName.setAttribute("colspan", 8);
        }
        else {
            fillAthletesTable(athletes);
        }
    }

    function fillAthletesTable(athletes) {
        var table = document.getElementById("athlete_table");
        table.innerHTML = "";
        var i = 0;
        athletes.forEach(function(athlete) {
            var row = table.insertRow(i);
            var onclickEdit = "window.location = 'athlete.html?id=" + athlete.id
                    + "&series_id=" + series_id + "&space_id=" + space_id
                    + "&readonly=" + series.locked + "'";
            var cellId = row.insertCell(0);
            cellId.className = "edit";
            cellId.innerHTML = athlete.id;
            cellId.setAttribute("onclick", onclickEdit);
            var cellLastName = row.insertCell(1);
            cellLastName.className = "edit";
            cellLastName.innerHTML = athlete.lastName;
            cellLastName.setAttribute("onclick", onclickEdit);
            var cellFirstName = row.insertCell(2);
            cellFirstName.className = "edit";
            cellFirstName.innerHTML = athlete.firstName;
            cellFirstName.setAttribute("onclick", onclickEdit);
            var cellYear = row.insertCell(3);
            cellYear.className = "edit";
            cellYear.innerHTML = athlete.yearOfBirth;
            cellYear.setAttribute("onclick", onclickEdit);
            var cellGender = row.insertCell(4);
            cellGender.className = "edit";
            cellGender.innerHTML = athlete.gender;
            cellGender.setAttribute("onclick", onclickEdit);
            var cellCategory = row.insertCell(5);
            cellCategory.className = "edit";
            cellCategory.innerHTML = athlete.category;
            cellCategory.setAttribute("onclick", onclickEdit);
            var cellClub = row.insertCell(6);
            cellClub.className = "edit";
            cellClub.innerHTML = athlete.club;
            cellClub.setAttribute("onclick", onclickEdit);
            var del = document.createElement("a");
            del.setAttribute("href", "#");
            del.setAttribute("onclick", "seriesController.deleteAthlete(" + athlete.id + ")");
            var delSpan = document.createElement("span");
            delSpan.className = "i18n";
            delSpan.innerHTML = "Delete";
            del.appendChild(delSpan);
            var cellFunction = row.insertCell(7);
            cellFunction.style.textAlign = "right";
            if (!series.locked && athlete.numberOfResults == 0) {
                cellFunction.appendChild(del);
            }
            i++;
        });
    }

    function fillSeries() {
        series.name = document.getElementById("series_name").value;
        series.locked = document.getElementById("series_locked").checked;
        series.space_id = space_id;
    }

}