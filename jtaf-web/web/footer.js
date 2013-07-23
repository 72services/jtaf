var footer = new Footer();

function Footer() {

    var footer = document.createElement("div");
    footer.id = "footer";
    footer.innerHTML = "&copy; 2013 by Simon Martinelli - "
            + "<a href='https://github.com/simasch/jtaf' target='_blank' style='color: white;   '>JTAF on GitHub</a>"
            + " - Version 0.4";
    document.body.appendChild(footer);

}