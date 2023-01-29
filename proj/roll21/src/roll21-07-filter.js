(function($) {

    function addRow(options, roll21try, resp, status, jqXHR) {
        var req = JSON.parse(options.data)
        var rollcallbacks = {
            'use': function() {
                options.success(resp, status, jqXHR)
                roll21.removeRollTable(options.roll21id)
            },
            'decAll': function() {
                findResult(options, resp, function(options, newresp, oldresp) {
                    var nt = extractTotals(newresp)
                    var ot = extractTotals(oldresp)
                    return compareTotals(nt, ot) == 'lower'
                })
            },
            'incAll': function() {
                findResult(options, resp, function(options, newresp, oldresp) {
                    var nt = extractTotals(newresp)
                    var ot = extractTotals(oldresp)
                    return compareTotals(nt, ot) == 'higher'
                })
            }
        }
        for (var i = 0; i < req.rolls.length; i++) {
            function createFind(index, compare) {
                return function() {
                    findResult(options, resp, function(options, newresp, oldresp) {
                        var nt = extractTotals(newresp)
                        var ot = extractTotals(oldresp)
                        var nv = nt[index]
                        var ov = ot[index]
                        if(nv.id != ov.id) throw 'wrong: ' + nv.id + ', ' + nv.id
                        return compare(nv.total, ov.total) > 0
                    })
                }
            }
            rollcallbacks['inc' + i] = createFind(i, (a, b) => a - b)
            rollcallbacks['dec' + i] = createFind(i, (a, b) => b - a)
        }
        roll21.addRow(options.roll21id, roll21try, req, resp, {
            'more5': function() {
                for(var i = 0; i < 5; i++) hack(options, function(roll21try, resp, status, jqXHR) {
                    addRow(options, roll21try, resp, status, jqXHR)
                })
            }
        }, rollcallbacks)
    }

    function compareTotals(t1, t2) {
        var ret = 'empty'
        t1.forEach(function(r1, index) {
            var r2 = t2[index]
            if(r1.id != r2.id) {
                throw 'wrong: ' + r1.id + ', ' + r2.id
            }
            if(r1.total < r2.total) {
                if (ret == 'empty') ret = 'lower'
                if (ret != 'lower') return 'both'
            }
            if(r1.total > r2.total) {
                if (ret == 'empty') ret = 'higher'
                if (ret != 'higher') return 'both'
            }
            if(r1.total == r1.total) {
                if (ret == 'empty') ret = 'same'
                if (ret != 'same') return 'both'
            }
        })
        return ret
    }

    function extractTotals(resp) {
        var ret = []
        Object.keys(resp).forEach(k => {
            ret.push({ id: k, total: JSON.parse(resp[k].json).total})
        })
        return ret
    }

    function findResult(options, oresp, strategy) {
        var retry = 10
        function callback(roll21try, resp, status, jqXHR) {
            if(retry <= 0) {
                roll21.msgRollTable(options.roll21id, 'I cannot find better results :(')
                return
            }
            retry--
            var valid = strategy(options, resp, oresp)
            if (!valid) {
                hack(options, callback)
                return
            }
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