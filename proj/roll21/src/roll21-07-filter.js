(function($) {

    function addRow(options, roll21try, resp, status, jqXHR) {
        var req = JSON.parse(options.data)
        roll21.addRow(options.roll21id, roll21try, req, resp, {
            'M5': function() {
                for(var i = 0; i < 5; i++) hack(options)
            }
        }, {
            'use': function() {
                options.success(resp, status, jqXHR)
                roll21.removeRollTable(options.roll21id)
            },
            'dec': function() {
                findResult(options, resp, 'lower')
            },
            'inc': function() {
                findResult(options, resp, 'higher')
            }
        })
    }

    function compareTotals(t1, t2) {
        var ret = 'empty'
        for (var k in t1) {
            console.log('inner', t1[k], t2[k])
            if(t1[k] < t2[k]) {
                if (ret == 'empty') ret = 'lower'
                if (ret != 'lower') return 'both'
            }
            if(t1[k] > t2[k]) {
                if (ret == 'empty') ret = 'higher'
                if (ret != 'higher') return 'both'
            }
            if(t1[k] == t2[k]) {
                if (ret == 'empty') ret = 'same'
                if (ret != 'same') return 'both'
            }
        }
        return ret
    }

    function extractTotals(resp) {
        var ret = {}
        Object.keys(resp).forEach(k => {
            ret[k] = JSON.parse(resp[k].json).total
        })
        return ret
    }

    function findResult(options, oresp, direction) {
        console.log('inc', oresp)
        var totals = extractTotals(oresp)
        console.log('rrrr', totals)

        var retry = 10
        function callback(roll21try, resp, status, jqXHR) {
            if(retry <= 0) {
                roll21.msgRollTable(options.roll21id, 'I cannot find better results :(')
                return
            }
            retry--
            var nt = extractTotals(resp)
            var comp = compareTotals(nt, totals)
            console.log('comp', comp, nt)
            if (comp != direction) {
                hack(options, callback)
                return
            }
            console.log('found')
            addRow(options, roll21try, resp, status, jqXHR)
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
            addRow(options, roll21try, resp, status, jqXHR)
        })
    }

})(jQuery);