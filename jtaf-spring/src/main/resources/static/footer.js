function Footer() {

    var footer = document.createElement("div");
    footer.id = "footer";
    footer.innerHTML = "&copy; 2018 by Simon Martinelli, <a href='http://www.72.services' target='_blank' style='color: white;'>72 Services LLC</a> - "
            + "<a href='https://github.com/simasch/jtaf' target='_blank' style='color: white;'>JTAF on GitHub</a>"
            + " - Version @project.version@";
    document.body.appendChild(footer);

}

var footer = new Footer();

(function(i, s, o, g, r, a, m) {
    i['GoogleAnalyticsObject'] = r;
    i[r] = i[r] || function() {
        (i[r].q = i[r].q || []).push(arguments)
    }, i[r].l = 1 * new Date();
    a = s.createElement(o),
            m = s.getElementsByTagName(o)[0];
    a.async = 1;
    a.src = g;
    m.parentNode.insertBefore(a, m)
})(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

ga('create', 'UA-7986785-1', 'jtaf-simas.rhcloud.com');
ga('send', 'pageview');