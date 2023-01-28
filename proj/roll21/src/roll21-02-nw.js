(function($) {
    
    function loadScript(d, url) {
        var element = d.createElement('script')
        element.setAttribute('src', url)
        d.body.appendChild(element)
    }

    function prepare(w, d) {
        d.head.innerHTML = '<html><head><title>R21</title>' + 
            '</head>'
        d.body.innerHTML = '<body>' +
            '<div id="wrapper"><h1>Roll21</h1></div>' +
            '</body></html>'

        loadScript(d, "https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js")
        
    }

    if(!roll21.window) {
        roll21.window = window.open()
        prepare(roll21.window, roll21.window.document)
    }

})(jQuery);
