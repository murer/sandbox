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
            '<div id="wrapper"><h1>Loading</h1></div>' +
            '</body></html>'

        loadScript(d, "https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js")
        
    }


    // '<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>' +
    // '<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>' +


    if(!roll21.window) {
        roll21.window = window.open()
    }
    prepare(roll21.window, roll21.window.document)

})(jQuery)


// <!-- Latest compiled and minified CSS -->

// <!-- Optional theme -->
// <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

// <!-- Latest compiled and minified JavaScript -->
// <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>