function ResultsController() {
    var util = new Util();

    var athlete;
    var athletes;
    var competition;
    var clubs;
    var space_id;

    this.loadData = function() {
        util.showMessage();

        space_id = util.searchMap.space_id;
        var id = util.searchMap.id;

        util.xhrGet("/jtaf/res/competitions/" + id, function(response) {
            competition = JSON.parse(response);
            document.getElementById("title").innerHTML = competition.name;
        });

        util.xhrGetSync("/jtaf/res/clubs?space_id=" + space_id, function(response) {
            clubs = JSON.parse(response);
        });

        document.getElementById("search_term").focus();

        util.i18n();
    };

    this.search = function() {
        clear();
        hideOutput();

        var searchterm = document.getElementById("search_term").value;
        var number = parseInt(searchterm);
        if (number !== undefined && !isNaN(number) && typeof number === "number") {
            util.xhrGet("/jtaf/res/athletes/" + number, function(response) {
                parseAndFill(response);
            });
        } else {
            util.xhrGet("/jtaf/res/athletes/search?series_id=" + competition.series_id + "&query=" + searchterm, function(response) {
                parseAndFillAthletes(response);
            });
        }
    };

    this.save = function() {
        fillAthlete();
        if (athlete.club === undefined || athlete.club === null) {
            util.error("Club must be choosen.");
        }
        else {
            util.xhrPost("/jtaf/res/athletes/", function(response) {
                parseAndFill(response);
                util.info("Athlete saved");
                document.getElementById("search_term").focus();
                document.getElementById("search_term").select();
            }, athlete);
        }
    };

    this.selectAthlete = function(id) {
        util.xhrGet("/jtaf/res/athletes/" + id, function(response) {
            document.getElementById("athlete_list").className = "invisible";
            parseAndFill(response);
        });
    };

    this.clearResults = function() {
        if (confirm(util.translate("Are you sure?"))) {
            athlete.results = [];
            this.save();
        }
    };

    this.calculatePoints = function(i) {
        var ev = athlete.category.events[i];
        var result = document.getElementById("result" + i).value;
        var points = 0;
        if (result > 0) {
            if (ev.type === "run") {
                points = ev.a * Math.pow((ev.b - result * 100) / 100, ev.c);
            } else if (ev.type === "run_long") {
                var parts = result.split(".");
                var time = parts[0] * 6000 + parts[1] * 100;
                points = ev.a * Math.pow((ev.b - time) / 100, ev.c);
            } else if (ev.type === "jump_throw") {
                points = ev.a * Math.pow((result * 100 - ev.b) / 100, ev.c);
            }
        }
        points = Math.round(points);
        if (isNaN(points) || points < 0) {
            points = 0;
        }
        athlete.results[i] = {result: result, points: points, event: ev, competition: competition};
        document.getElementById("points" + i).value = points;
    };


    this.addAthlete = function() {
        clear();
        document.getElementById("input_form").className = "";
    };

    function hideOutput() {
        document.getElementById("input_form").className = "invisible";
        document.getElementById("athlete_list").className = "invisible";
    }

    function fillClubSelect() {
        var select = document.getElementById("athlete_club");
        while (select.firstChild) {
            select.removeChild(select.firstChild);
        }

        var option = document.createElement("option");
        option.innerHTML = "";
        select.appendChild(option);

        for (var i in clubs) {
            var club = clubs[i];
            option = document.createElement("option");
            option.value = club.id;
            option.innerHTML = club.abbreviation;
            select.appendChild(option);
            if (athlete.club !== undefined && athlete.club !== null &&
                    athlete.club.id === club.id) {
                option.selected = true;
            }
        }
    }

    function parseAndFill(response) {
        athlete = JSON.parse(response);
        fillForm();
    }

    function fillForm() {
        document.getElementById("input_form").className = "";

        document.getElementById("athlete_id").value = athlete.id;
        document.getElementById("athlete_lastName").value = athlete.lastName;
        document.getElementById("athlete_lastName").focus();
        document.getElementById("athlete_firstName").value = athlete.firstName;
        document.getElementById("athlete_year").value = athlete.year;
        if (athlete.gender !== undefined && athlete.gender !== null) {
            document.getElementById("athlete_gender_" + athlete.gender).checked = true;
        } else {
            document.getElementById("athlete_gender_m").checked = false;
            document.getElementById("athlete_gender_f").checked = false;
        }
        if (athlete.category !== undefined && athlete.category !== null) {
            document.getElementById("athlete_category").value = athlete.category.abbreviation;
            document.getElementById("results").setAttribute("style", "visibility: visible;");
            fillEventsTable();
        } else {
            document.getElementById("athlete_category").value = "";
            document.getElementById("results").setAttribute("style", "visibility: hidden;");
        }
        fillClubSelect();
    }

    function fillEventsTable() {
        var table = document.getElementById("athlete_events");
        table.innerHTML = "";
        if (athlete.category !== undefined) {
            var i = 0;
            athlete.category.events.forEach(function(aevent) {
                var row = table.insertRow(i);
                var cellName = row.insertCell(0);
                cellName.innerHTML = aevent.name;
                var cellResult = row.insertCell(1);
                var result = document.createElement("input");
                result.setAttribute("type", "text");
                result.id = "result" + i;
                result.setAttribute("pattern", "\\d+\\.\\d{2}");
                result.setAttribute("onblur", "resultsController.calculatePoints(" + i + ")");
                cellResult.appendChild(result);
                var cellPoints = row.insertCell(2);
                var points = document.createElement("input");
                points.id = "points" + i;
                points.setAttribute("type", "text");
                points.setAttribute("readonly", "true");
                points.setAttribute("disabled", "true");
                points.setAttribute("style", "width: 100px");
                cellPoints.appendChild(points);
                if (athlete.results[i] !== undefined) {
                    result.value = athlete.results[i].result;
                    points.value = athlete.results[i].points;
                }
                i++;
            });
            document.getElementById("result0").focus();
        }
    }

    function fillAthlete() {
        athlete.firstName = document.getElementById("athlete_firstName").value;
        athlete.lastName = document.getElementById("athlete_lastName").value;
        athlete.year = document.getElementById("athlete_year").value;
        if (document.getElementById("athlete_gender_m").checked) {
            athlete.gender = "m";
        } else {
            athlete.gender = "f";
        }
        var select = document.getElementById("athlete_club");
        var id = select.options[select.selectedIndex].value;
        if (id !== null) {
            clubs.forEach(function(club) {
                if (club.id === parseInt(id)) {
                    athlete.club = club;
                    return;
                }
            });
        }
        athlete.series_id = competition.series_id;
    }

    function clear() {
        var error_div = document.getElementById("error");
        if (error_div !== null) {
            document.body.removeChild(error_div);
        }
        athletes = null;
        athlete = {id: null, lastName: "", firstName: "", gender: null, club: null, category: null};
        var table = document.getElementById("athlete_events");
        table.innerHTML = "";
        fillForm();
    }

    function parseAndFillAthletes(response) {
        athletes = JSON.parse(response);
        if (athletes.length === 1) {
            athlete = athletes[0];
            fillForm();
        }
        else {
            document.getElementById("athlete_list").className = "";

            var table = document.getElementById("athlete_table");
            table.innerHTML = "";
            if (athletes === undefined || athletes.length === 0) {
                var row = table.insertRow(0);
                var cellName = row.insertCell(0);
                cellName.innerHTML = util.translate("No athletes found");
                cellName.setAttribute("colspan", 7);
            } else {
                var i = 0;
                athletes.forEach(function(athlete) {
                    var row = table.insertRow(i);
                    var onclickEdit = "resultsController.selectAthlete(" + athlete.id + ")";
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
                    cellYear.innerHTML = athlete.year;
                    cellYear.setAttribute("onclick", onclickEdit);
                    var cellGender = row.insertCell(4);
                    cellGender.className = "edit";
                    cellGender.innerHTML = athlete.gender;
                    cellGender.setAttribute("onclick", onclickEdit);
                    var cellCategory = row.insertCell(5);
                    cellCategory.className = "edit";
                    cellCategory.innerHTML = athlete.category !== null
                            ? athlete.category.abbreviation : "";
                    cellCategory.setAttribute("onclick", onclickEdit);
                    var cellClub = row.insertCell(6);
                    cellClub.className = "edit";
                    cellClub.innerHTML = athlete.club !== undefined
                            ? athlete.club.name : "";
                    cellClub.setAttribute("onclick", onclickEdit);
                    i++;
                });
                document.getElementById("search_term").focus();
            }
        }
    }

}
