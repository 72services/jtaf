xhrGet("/jtaf/navigation.html", function(response) {
    var table = document.createElement("table");
    table.setAttribute("style", "width: 100%;");
    var tr = document.createElement("tr");
    table.appendChild(tr);
    var td = document.createElement("td");
    td.innerHTML = response;
    tr.appendChild(td);
    var competition = JSON.parse(localStorage.getItem("competition"));

    td = document.createElement("td");
    td.setAttribute("style", "text-align: right;");
    if (competition !== undefined && competition !== null) {
        td.innerHTML = competition.name;
    }
    tr.appendChild(td);

    document.getElementById("navigation").appendChild(table);
});

