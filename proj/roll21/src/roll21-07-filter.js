(function($) {

    function addRow(options, roll21try, req, resp, status, jqXHR) {
        roll21.addRow(options.roll21id, roll21try, req, resp, {
            'M5': function() {
                for(var i = 0; i < 5; i++) hack(options)
            }
        }, {
            'use': function() {
                options.success(resp, status, jqXHR)
                roll21.removeRollTable(options.roll21id)
            },
            'inc': function() {
                inc(options, resp)
            }
        })
    }

    function compareTotals(t1, t2) {
        for (var k in t1) {
            console.log('inner', t1[k], t2[k])
            if(t1[k] < t2[k]) return -1
            if(t1[k] > t2[k]) return 1
        }
        return 0
    }

    function extractTotals(resp) {
        var ret = {}
        Object.keys(resp).forEach(k => {
            ret[k] = JSON.parse(resp[k].json).total
        })
        return ret
    }

    function inc(options, oresp) {
        console.log('inc', oresp)
        var totals = extractTotals(oresp)
        console.log('rrrr', totals)

        function callback(roll21try, resp, status, jqXHR) {
            var nt = extractTotals(resp)
            var comp = compareTotals(totals, nt)
            console.log('comp', comp, nt)
            if (comp <= 0) {
                hack(options, callback)
                return
            }
            console.log('found')
        }

        hack(options, callback)
    }

    function hack(options, callback) {
        var roll21try = 'r21try' + Math.random().toString().replace('.', '')
        $.ajax({
            type: options.type,
            url: options.url,
            data: options.data,
            dataType: options.dataType,
            contentType: options.contentType,
            roll21try: roll21try,
            roll21id: options.roll21id, 
            roll21: true,
            success: function(resp, status, jqXHR) {
                callback(roll21try, resp, status, jqXHR)
            }
        })
    }

    window.roll21.filter = function(options, originalOptions, jqXHR) {
        if (!options.url) return
        if (!options.url.endsWith('/doroll')) return
        if (!options.type == 'POST') return
        if (options.complete) return
        if (options.roll21) return
        // console.log('data', JSON.parse(options.data))
        // console.log('original', options)
        jqXHR.abort()
        options.roll21id = 'r21id' + Math.random().toString().replace('.', '')
        hack(options, function(roll21try, resp, status, jqXHR) {
            var req = JSON.parse(options.data)
            addRow(options, roll21try, req, resp, status, jqXHR)
        })
    }

})(jQuery);