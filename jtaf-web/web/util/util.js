var i18messages;

var searchMap = new (function(sSearch) {
    var rNull = /^\s*$/, rBool = /^(true|false)$/i;
    function buildValue(sValue) {
        if (rNull.test(sValue)) {
            return null;
        }
        if (rBool.test(sValue)) {
            return sValue.toLowerCase() === "true";
        }
        if (isFinite(sValue)) {
            return parseFloat(sValue);
        }
        if (isFinite(Date.parse(sValue))) {
            return new Date(sValue);
        }
        return sValue;
    }
    if (sSearch.length > 1) {
        for (var aItKey, nKeyId = 0, aCouples = sSearch.substr(1).split("&"); nKeyId < aCouples.length; nKeyId++) {
            aItKey = aCouples[nKeyId].split("=");
            this[unescape(aItKey[0])] = aItKey.length > 1 ? buildValue(unescape(aItKey[1])) : null;
        }
    }
})(window.location.search);

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
    div.innerHTML = "<b>INFO</b><br />" + translate(message);
    document.body.appendChild(div);
    window.setTimeout("fade(document.getElementById('info'))", 5000);
}

function error(message) {
    var div = document.createElement("div");
    div.setAttribute("id", "error");
    div.innerHTML = "<b>ERROR</b><br />" + translate(message);
    document.body.appendChild(div);
    window.setTimeout("fade(document.getElementById('error''))", 5000);
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

function i18n() {
    document.title = translate(document.title);

    var elements = document.getElementsByClassName("i18n");
    for (var i = 0; i < elements.length; ++i) {
        elements[i].innerHTML = translate(elements[i].innerHTML);
    }

    var elements = document.getElementsByTagName("input");
    for (var i = 0; i < elements.length; ++i) {
        elements[i].value = translate(elements[i].value);
    }
}

function translate(field) {
    if (i18messages === undefined) {
        loadMessages();
    }
    if (i18messages !== undefined) {
        var message = i18messages[field];
        if (message !== undefined) {
            return i18messages[field];
        }
    }
    return field;
}

function loadMessages() {
    var lang = window.navigator.language;
    if (lang === undefined) {
        lang = window.navigator.browserLanguage;
        if (lang !== undefined) {
            lang = lang.substring(0, 2);
        }
    }
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/jtaf/i18n/messages_" + lang + ".json", false);
    xhr.onload = function() {
        if (xhr.status === 200) {
            i18messages = JSON.parse(xhr.response);
        }
    };
    xhr.send();
}
