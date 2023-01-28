(function($, $d) {

    
    function addRow(id, rid, req, resp, tablecallbacks, rollcallbacks) {
        var rt = $d.find('#' + id)
        var timeoutRunning = true
        if(!rt.length) {
            $d.find('#wrapper').append('<div id="' + id + '"/>')
            rt = $d.find('#' + id)
            rt.append('<h3/>').find('h3:last').text(id)
            rt.append('<div/>').find('div:last').addClass('rolltableaction')
            rt.append('<div/>').find('div:last').addClass('rolldefault')
            rt.append('<table><thead><tr><th>roll</th><th>action</th></tr></thead><tbody></tbody></table>')
            rt.find('table').attr('border', '1').attr('cellpadding', '10')
            req.rolls.forEach(v => {
                rt.find('thead tr').append('<th/>').find('th:last').append(v.rollid) 
            })
            rt.find('.rolldefault').html('<span>Automatic:</span> <span class="rolltime">4</span>')

            function countdown() {
                if(!timeoutRunning) {
                    rt.find('.rolltime').text('timeout disabled')
                    return
                }
                var time = parseInt(rt.find('.rolltime').text())
                if(time <= 0) {
                    rt.find('tbody tr:first .rowaction_use').click()
                    return
                }
                time = time - 1
                rt.find('.rolltime').text('' + time)
                setTimeout(countdown, 1000)
            }
            setTimeout(countdown, 1000)
        }
        var tr = rt.find('tbody').append('<tr><td class="rollid"/><td class="action"/></tr>').find('tr:last')
        tr.find('td.rollid').text(rid)
        req.rolls.forEach(v => {
            tr.append('<td/>').find('td:last').addClass('roll21' + v.rollid).text(v.rollid)
        })
        Object.keys(resp).forEach(function(k) {
            var rolldata = JSON.parse(resp[k].json)
            var col = tr.find('td.roll21' + k)
            col.html('<div/>').find('div:last').text('' + rolldata.total)
            var desc = rolldata.rolls.map(function(roll) {
                // console.log('roll', roll)
                ret = ''
                // if(roll.type == 'M') ret += roll.expr
                if(roll.type == 'R') ret += roll.dice + 'd' + roll.sides + ': ' + roll.results.map(v => v.v).join(', ')
                return ret
            }).join(' ')
            col.append('<div/>').find('div:last').text(desc)
        })
        rt.find('.rolltableaction').text('')
        Object.keys(tablecallbacks).forEach(function(k) {
            rt.find('.rolltableaction').append('<button type="button"/>').find('button:last').text(k).click(function() {
                timeoutRunning = false
                tablecallbacks[k]()
            })
        })
        Object.keys(rollcallbacks).forEach(function(k) {
            tr.find('td.action').append('<button type="button"/>').find('button:last').addClass('rowaction_' + k).text(k).click(function() {
                timeoutRunning = false
                rollcallbacks[k]()
            })
        })
    }

    function removeRollTable(id) {
        $d.find('#' + id).remove()
    }

    roll21.addRow = addRow
    roll21.removeRollTable = removeRollTable

    console.clear()
    $d.find('#wrapper').html('')
//     addRow('aa', 'bb', {
//     "cid": "campaign-11896033-axRZK6G08g4r9r-Czt5xHA",
//     "fbnum": "https://roll20-99960.firebaseio.com/",
//     "authkey": "eyJhbGciOiJSUzI1NiIsImtpZCI6ImQwNWI0MDljNmYyMmM0MDNlMWY5MWY5ODY3YWM0OTJhOTA2MTk1NTgiLCJ0eXAiOiJKV1QifQ.eyJjdXJyZW50Y2FtcGFpZ24iOiJjYW1wYWlnbi0xMTg5NjAzMy1heFJaSzZHMDhnNHI5ci1DenQ1eEhBIiwiaXNfZ20iOmZhbHNlLCJwbGF5ZXJpZCI6Ii1NbzFMbjZJazA0WnpPNVZ4a01XIiwidXNlcmlkIjo5ODIyNDMsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9yb2xsMjAtZGV2IiwiYXVkIjoicm9sbDIwLWRldiIsImF1dGhfdGltZSI6MTY3NDk0MDI3NiwidXNlcl9pZCI6Ijk4MjI0MyIsInN1YiI6Ijk4MjI0MyIsImlhdCI6MTY3NDk0MDI3NiwiZXhwIjoxNjc0OTQzODc2LCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7fSwic2lnbl9pbl9wcm92aWRlciI6ImN1c3RvbSJ9fQ.JpEJ_4SC4on-pl4MRU0HmQyg9kZ_n1gG-PXnpg5KrngY1VEx8Ncl-EHSZdJ2e3bt0qIoWBkASi6qvz2mkKc4Mxj0-cMN_-65p6CNM4buRxVjYoSr-SXKG6_sbENiopLS25fFlXBDiAiJXtGVa1AvhW--y-_-fErPpUDm_UxNHGujDn13kHKHI3QGaQeruepRU1J5XpoFW6Ln6p1PFggCrCf7b7V9sHXo-Lr2Zv_0Zu6N3taS81KflTk-vuItuPWLiP6nP_V0vcB1tfP9dSh7l08FZ1lKqjwPD71FAkxb5tPSzg7MmI2TSP3I60VyUvieFeKGKYvI3P9F3uj6EppN5A",
//     "pid": "-Mo1Ln6Ik04ZzO5VxkMW",
//     "rolls": [
//         {
//             "vre": {
//                 "type": "V",
//                 "rolls": [
//                     {
//                         "type": "R",
//                         "dice": 1,
//                         "sides": 20,
//                         "mods": {
//                             "customCrit": [
//                                 {
//                                     "comp": ">=",
//                                     "point": 20
//                                 }
//                             ]
//                         }
//                     },
//                     {
//                         "type": "M",
//                         "expr": "+4"
//                     },
//                     {
//                         "type": "L",
//                         "text": "STR"
//                     },
//                     {
//                         "type": "M",
//                         "expr": "+2"
//                     },
//                     {
//                         "type": "L",
//                         "text": "PROF"
//                     }
//                 ],
//                 "resultType": "sum"
//             },
//             "rollid": "-NMuDCWZ5zw47KEH-AVs",
//             "rolltype": "rollresult"
//         },
//         {
//             "vre": {
//                 "type": "V",
//                 "rolls": [
//                     {
//                         "type": "R",
//                         "dice": 1,
//                         "sides": 20,
//                         "mods": {
//                             "customCrit": [
//                                 {
//                                     "comp": ">=",
//                                     "point": 20
//                                 }
//                             ]
//                         }
//                     },
//                     {
//                         "type": "M",
//                         "expr": "+4"
//                     },
//                     {
//                         "type": "L",
//                         "text": "STR"
//                     },
//                     {
//                         "type": "M",
//                         "expr": "+2"
//                     },
//                     {
//                         "type": "L",
//                         "text": "PROF"
//                     }
//                 ],
//                 "resultType": "sum"
//             },
//             "rollid": "-NMuDCWZ5zw47KEH-AVt",
//             "rolltype": "rollresult"
//         }
//     ],
//     "use3d": false
// }, {
//         "-NMuDCWZ5zw47KEH-AVs": {
//             "json": "{\"type\":\"V\",\"rolls\":[{\"type\":\"R\",\"dice\":1,\"sides\":20,\"mods\":{\"customCrit\":[{\"comp\":\">=\",\"point\":20}]},\"results\":[{\"v\":6}]},{\"type\":\"M\",\"expr\":\"+4\"},{\"type\":\"L\",\"text\":\"STR\"},{\"type\":\"M\",\"expr\":\"+2\"},{\"type\":\"L\",\"text\":\"PROF\"}],\"resultType\":\"sum\",\"total\":12}",
//             "signature": "3b3d9631da5e43b1ec42ca0157251b420c6d6791851ea7870798f24d236c19017faeba8100edef75ce59cc7f8a3208c05e9976704126acf1da78710b08e2cc08",
//             "cid": "campaign-11896033-axRZK6G08g4r9r-Czt5xHA"
//         },
//         "-NMuDCWZ5zw47KEH-AVt": {
//             "json": "{\"type\":\"V\",\"rolls\":[{\"type\":\"R\",\"dice\":1,\"sides\":20,\"mods\":{\"customCrit\":[{\"comp\":\">=\",\"point\":20}]},\"results\":[{\"v\":17}]},{\"type\":\"M\",\"expr\":\"+4\"},{\"type\":\"L\",\"text\":\"STR\"},{\"type\":\"M\",\"expr\":\"+2\"},{\"type\":\"L\",\"text\":\"PROF\"}],\"resultType\":\"sum\",\"total\":23}",
//             "signature": "79e7c07946c5c72427c707b1e59cc9440038ff5108a1301a8067c0a8c09ef79915c8ad5016282484b74b223004b446f9e959ecfdd34feb66c01af2db5033836c",
//             "cid": "campaign-11896033-axRZK6G08g4r9r-Czt5xHA"
//         }
//     },{},{})

})(jQuery, jQuery('body', window.roll21.window.document));