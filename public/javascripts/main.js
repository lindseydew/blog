document.addEventListener("DOMContentLoaded", function() {
    var links = document.getElementsByTagName('a');
    for(i=0; i<links.length; i++) {
       var host = links[i].hostname;
       var domain = document.domain;
       if(host !== domain) {
           links[i].setAttribute("target", "blank");
       }
    }
});