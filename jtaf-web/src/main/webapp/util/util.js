function Util() {
    var self = this;
    var i18messages;

    this.searchMap = new (function(sSearch) {
        function buildValue(sValue) {
            var rNull = /^\s*$/, rBool = /^(true|false)$/i;
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

    this.xhrGet = function(url, func) {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", url, true);
        xhr.onload = function() {
            if (xhr.status === 200 || xhr.status === 204) {
                func(xhr.responseText);
            } else {
                self.error(xhr.status);
            }
        };
        xhr.send();
    };

    this.xhrGetSync = function(url, func) {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", url, false);
        xhr.onload = function() {
            if (xhr.status === 200) {
                func(xhr.responseText);
            } else {
                self.error(xhr.status);
            }
        };
        xhr.send();
    };

    this.xhrDelete = function(url, func) {
        var xhr = new XMLHttpRequest();
        xhr.open("DELETE", url, true);
        xhr.onload = function() {
            if (xhr.status === 204) {
                func(xhr.responseText);
            } else {
                self.error(xhr.status);
            }
        };
        xhr.send();
    };

    this.xhrPost = function(url, func, body) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url, true);
        xhr.onload = function() {
            if (xhr.status === 200 || xhr.status === 204) {
                func(xhr.responseText);
            } else {
                self.error(xhr.status);
            }
        };
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify(body));
    };

    this.createComparator = function(property) {
        return function(a, b) {
            if (a[property] < b[property]) {
                return -1;
            } else if (a[property] > b[property]) {
                return 1;
            } else {
                return 0;
            }
        };
    };

    this.i18n = function() {
        document.title = this.translate(document.title);

        var elements = document.getElementsByClassName("i18n");
        for (var i = 0; i < elements.length; ++i) {
            elements[i].innerHTML = this.translate(elements[i].innerHTML);
        }

        var elements = document.getElementsByTagName("input");
        for (var i = 0; i < elements.length; ++i) {
            try {
                elements[i].value = this.translate(elements[i].value);
            }
            catch (err) {
                // Do nothing. It's just I18n :-)
            }
        }
    };

    this.translate = function(field) {
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
    };

    this.info = function(message) {
        var div = document.createElement("div");
        div.setAttribute("id", "info");
        div.innerHTML = "<b>INFO</b><br />" + this.translate(message);
        document.body.appendChild(div);
        window.setTimeout("fade(750, document.getElementById('info'))", 5000);
    };

    this.error = function(message) {
        var div = document.createElement("div");
        div.setAttribute("id", "error");
        div.innerHTML = "<b>ERROR</b><br />" + this.translate(message);
        document.body.appendChild(div);
        window.setTimeout("fade(750, document.getElementById('error''))", 5000);
    };

    this.showMessage = function() {
        if (this.searchMap.message) {
            this.info(this.translate(this.searchMap.message));
        }
    };

    this.showLoading = function() {
        document.getElementById("loading").style.display = "block";
    };

    this.hideLoading = function() {
        document.getElementById("loading").style.display = "none";
    };

    function loadMessages() {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "/jtaf/res/i18n/messages", false);
        xhr.onload = function() {
            if (xhr.status === 200) {
                i18messages = JSON.parse(xhr.responseText);
            }
        };
        xhr.send();
    }
}

function fade(ms, el) {
    var opacity = 1;
    var interval = 50;
    var gap = interval / ms;

    function func() {
        opacity -= gap;
        el.style.opacity = opacity;

        if (opacity <= 0) {
            window.clearInterval(fading);
            el.style.display = 'none';
        }
    }
    var fading = window.setInterval(func, interval);
}