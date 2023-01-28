(function($) {
    
    console.clear()

    function dummy() {
        $.ajax({
            url: "https://dice.roll20.net/doroll",
            data: '{"cid":"campaign-11896033-axRZK6G08g4r9r-Czt5xHA","fbnum":"https://roll20-99960.firebaseio.com/","authkey":"eyJhbGciOiJSUzI1NiIsImtpZCI6ImQwNWI0MDljNmYyMmM0MDNlMWY5MWY5ODY3YWM0OTJhOTA2MTk1NTgiLCJ0eXAiOiJKV1QifQ.eyJjdXJyZW50Y2FtcGFpZ24iOiJjYW1wYWlnbi0xMTg5NjAzMy1heFJaSzZHMDhnNHI5ci1DenQ1eEhBIiwiaXNfZ20iOmZhbHNlLCJwbGF5ZXJpZCI6Ii1NbzFMbjZJazA0WnpPNVZ4a01XIiwidXNlcmlkIjo5ODIyNDMsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9yb2xsMjAtZGV2IiwiYXVkIjoicm9sbDIwLWRldiIsImF1dGhfdGltZSI6MTY3NDkwOTY3NywidXNlcl9pZCI6Ijk4MjI0MyIsInN1YiI6Ijk4MjI0MyIsImlhdCI6MTY3NDkwOTY3NywiZXhwIjoxNjc0OTEzMjc3LCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7fSwic2lnbl9pbl9wcm92aWRlciI6ImN1c3RvbSJ9fQ.LOXNEq7caXdbcKhL_Mhcj50732UQc0ulBt4MZTJcrW6Q9tSFxB25zuaGKKJnsB4l8KUbAC7X3bkPqMNnuUAJqvPSH0czN7XHYkR2TjJmEDMNMXql-cdJLJzyv94Owgynj9KpPiY5X3tKl6xjwQ2hzZrMF70y0QHm0_yLT6O6nqbK1cyQ5zKWLkQ5C3nvnZwbbijKhqypykV1s9pP-NW7XANfDLPPZQS4Vt0ErI1lUfSsm-p2LIni6W4OOLjAK9FgBdgqAUr_SS7BfdaLIlysGcgjJvsV5_2OFXHuSiTb4BFua_D33HLLxoWdvSxkvWLfR4p-xhjbdp9o5uK2Q2_scQ","pid":"-Mo1Ln6Ik04ZzO5VxkMW","rolls":[{"vre":{"type":"V","rolls":[{"type":"R","dice":1,"sides":20,"mods":{}}],"resultType":"sum"},"rollid":"-NMsOa7nwE1vphczIK7z","rolltype":"rollresult"}],"use3d":false}',
            dataType: "json",
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            success: function(resp, status, jqXHR) {
                console.log('a', resp)
                // options.success(resp, status, jqXHR)
            }
        })
    }

    function hack(options, callback) {
        $.ajax({
            type: options.type,
            url: options.url,
            data: options.data,
            dataType: options.dataType,
            contentType: options.contentType,
            roll21: true,
            success: callback
        })
    }

    function roll21filter(options, originalOptions, jqXHR) {
        if (!options.url) return
        if (!options.url.endsWith('/doroll')) return
        if (!options.type == 'POST') return
        if (options.complete) return
        if (options.roll21) return
        console.log('original', options)
        jqXHR.abort()
        hack(options, function(r1, status, jqXHR) {
            console.log('r1', r1)
            hack(options, function(r2, status, jqXHR) {
                console.log('r2', r2)
                options.success(r2, status, jqXHR)
            })
            
        })  
        // var originalSuccess = options.success
        // jqXHR.abort()
        // hack(options)
        // hack(options)   
        // options.success = function(resp, status, jqXHR) {
        //     var nr = hack(resp)
        //     console.log(nr)
        //     console.log(JSON.parse(options.data))
            //originalSuccess(resp, status, jqXHR)
        // }
        
    }

    window.roll21filter = roll21filter

})(jQuery)
