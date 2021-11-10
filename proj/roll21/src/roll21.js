
(function($) {

    var superadv = 1

    function rndbest(sides, n) {
        for (var i = 0; i < superadv; i++) {
            r = Math.randomInt(sides)+1
            if (r > n) {
                n = r
            }
        }
        return n
    }

    function hackRoll(original) {
        var ojson = JSON.parse(original.json)
        console.log('resultType', ojson.resultType)
        if (ojson.resultType != 'sum') {
            return original
        }
        var ototal = ojson.total
        console.log('roll', ototal)
        ojson.rolls.forEach(function(element, idx) {
            if (!element.sides) return
            element.results.forEach(function(result, ridx) {
                var ov = result.v
                var v = rndbest(element.sides, ov)
                element.results[ridx].v = v
                ototal = ototal + v - ov
                console.log('hack from', ov, 'to', v, 'total', ototal)
            })
        })
        ojson.total = ototal
        original.json = JSON.stringify(ojson)
        return original
    }

    function roll21filter(options, originalOptions, jqXHR) {
        if (!options.url) return
        if (!options.url.endsWith('/doroll')) return
        if (!options.type == 'POST') return
        if (options.complete) return
        var originalSuccess = options.success
        options.success = function(resp, status, jqXHR) {
            for (var k in resp) {
                var v = resp[k]
                hv = hackRoll(v)
                resp[k] = hv
            }
            originalSuccess(resp, status, jqXHR)
        }
    }

    window.roll21filter = roll21filter

    // $.ajaxPrefilter(function() { window.roll21filter.apply(window, arguments) })

})(jQuery)

