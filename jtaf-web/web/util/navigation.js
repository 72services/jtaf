xhrGet("/jtaf/navigation.html", function(response) {
    var table = document.createElement("table");
    table.setAttribute("style", "width: 100%;");
    var tr = document.createElement("tr");
    table.appendChild(tr);
    var td = document.createElement("td");
    td.innerHTML = response;
    tr.appendChild(td);

    document.getElementById("navigation").appendChild(table);
});

