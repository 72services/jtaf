var footer = new Footer();

function Footer() {

    var footer = document.createElement("div");
    footer.id = "footer";
    footer.innerHTML = "&copy; 2014 by Simon Martinelli - "
            + "<a href='https://github.com/simasch/jtaf' target='_blank' style='color: white;   '>JTAF on GitHub</a>"
            + " - Version ${project.version}";
    document.body.appendChild(footer);

}