(function($) {
    
    console.clear()

    function hack(resp) {
        var keys = Object.keys(resp)
        if(keys.length != 1) return resp
        var key = keys[0]
        console.log(key)
        var aa = resp[key]
        console.log(aa)
        var nkey = window.prompt('h',key)
        var ret = {}
        ret[nkey] = aa
        return ret
    }

    function roll21filter(options, originalOptions, jqXHR) {
        if (!options.url) return
        if (!options.url.endsWith('/doroll')) return
        if (!options.type == 'POST') return
        if (options.complete) return
        var originalSuccess = options.success
        options.success = function(resp, status, jqXHR) {
            var nr = hack(resp)
            console.log(nr)
            originalSuccess(nr, status, jqXHR)
        }
    }

    window.roll21filter = roll21filter

})(jQuery)
