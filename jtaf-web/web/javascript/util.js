function sendRequest(method, url, status, func, contenttype, body) {
    var xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === status) {
                func(xhr.response);
            } else {
                error(xhr.status);
            }
        }
    };
    if (contenttype !== undefined) {
        xhr.setRequestHeader("Content-Type", contenttype);
    }
    xhr.send(body);
}

function param() {
    var sequence = window.location.search;
    map = new Object();
    if (sequence.length > 1) {
        p = sequence.substr(1).split("&");
        for (var i = 0; i < p.length; i++) {
            kv = p[i].split("=");
            key = decodeURIComponent(kv[0]);
            if (key.match(/\W/)) {
                throw "Error: key \"" + key
                        + "\" has non alphanumeric characters";
            }
            if (key.charAt(0).match(/\d/)) {
                throw "Error: key \"" + key
                        + "\" has a leading numeric character";
            }
            value = decodeURIComponent(kv[1]);
            if (eval("map." + key + "!=undefined")) {
                throw "Error: key \"" + key + "\" set multiple times";
            }
            try {
                eval("map." + key + "=value");
            } catch (err) {
            }
        }
    }
    return map;
}
function el(name) {
    return document.getElementById(name);
}

function createComparator(property) {
    return function(a, b) {
        if (a[property] < b[property]) {
            return -1;
        } else if (a[property] > b[property]) {
            return 1;
        } else {
            return 0;
        }
    };
}

function info(message) {
    var div = document.createElement("div");
    div.setAttribute("id", "info");
    div.innerHTML = "<b>INFO</b><br />" + message;
    document.body.appendChild(div);
    window.setTimeout("fade(el('info'))", 5000);
}

function error(message) {
    var div = document.createElement("div");
    div.setAttribute("id", "error");
    div.innerHTML = "<b>ERROR</b><br />" + message;
    document.body.appendChild(div);
    window.setTimeout("fade(el('error''))", 5000);
}

function fade(element) {
    var opacity = 1;
    var timer = setInterval(function() {
        if (opacity <= 0.1) {
            clearInterval(timer);
            element.style.display = "none";
        }
        element.style.opacity = opacity;
        element.style.filter = "alpha(opacity=" + opacity * 100 + ")";
        opacity -= opacity * 0.1;
    }, 50);
}