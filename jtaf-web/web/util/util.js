function xhrGet(url, func) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onload = function() {
        if (xhr.status === 200 || xhr.status === 204) {
            func(xhr.response);
        } else {
            error(xhr.status);
        }
    };
    xhr.send();
}

function xhrGetSync(url, func) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, false);
    xhr.onload = function() {
        if (xhr.status === 200) {
            func(xhr.response);
        } else {
            error(xhr.status);
        }
    };
    xhr.send();
}

function xhrDelete(url, func) {
    var xhr = new XMLHttpRequest();
    xhr.open("DELETE", url, true);
    xhr.onload = function() {
        if (xhr.status === 204) {
            func(xhr.response);
        } else {
            error(xhr.status);
        }
    };
    xhr.send();
}

function xhrPost(url, func, body) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.onload = function() {
        if (xhr.status === 200 || xhr.status === 204) {
            func(xhr.response);
        } else {
            error(xhr.status);
        }
    };
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(body));
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

function mask(inputName, mask, evt) {
    try {
        var text = document.getElementById(inputName);
        var value = text.value;
        // If user pressed DEL or BACK SPACE, clean the value
        try {
            var e = (evt.which) ? evt.which : event.keyCode;
            if (e == 46 || e == 8) {
                text.value = "";
                return;
            }
        } catch (e1) {
        }
        var literalPattern = /[0\*]/;
        var numberPattern = /[0-9]/;
        var newValue = "";
        for (var vId = 0, mId = 0; mId < mask.length; ) {
            if (mId >= value.length) {
                break;
            }
            // Number expected but got a different value, store only the valid portion
            if (mask[mId] == '0' && value[vId].match(numberPattern) == null) {
                break;
            }
            // Found a literal
            while (mask[mId].match(literalPattern) == null) {
                if (value[vId] == mask[mId]) {
                    break;
                }
                newValue += mask[mId++];
            }
            newValue += value[vId++];
            mId++;
        }
        text.value = newValue;
    } catch (e) {
    }
}
