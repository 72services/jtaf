xhrGet("/jtaf/navigation.html", function(response) {
    document.getElementById("navigation").innerHTML = response;
});
