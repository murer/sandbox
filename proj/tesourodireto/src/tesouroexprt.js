(function ($) {

    function loadProtocolo(protocolo, cb) {
        $.ajax({
            method: 'GET',
            url: '/Protocolo/' + protocolo + '/1', 
            dataType: 'html', 
            success: function(result) {
                var r = $(result)
                var ret = {}
                ret.titulo = r.find('.td-protocolo-info-titulo').text().trim()
                ret.qtd = r.find('.td-protocolo-info:contains("Quantidade de") .td-protocolo-info--valor').text().trim()
                ret.valorUnitario = r.find('.td-protocolo-info:contains("Valor unit") .td-protocolo-info--valor').text().trim()
                ret.valorLiquido = r.find('.td-protocolo-info:contains("Valor l") .td-protocolo-info--valor').text().trim()
                ret.rent = r.find('.td-protocolo-info:contains("Rentabilidade") .td-protocolo-info--valor').text().trim()
                ret.taxaB3 = r.find('.td-protocolo-info:contains("Taxa da B3") .td-protocolo-info--valor').text().trim()
                ret.taxaInst = r.find('.td-protocolo-info:contains("Taxa da inst") .td-protocolo-info--valor').text().trim()
                ret.dia = r.find('.td-protocolo-info-base:contains("Data da op") .td-protocolo-info-base--blue').text().trim()
                ret.inst = r.find('.td-protocolo-info-base:contains("Institui") .td-protocolo-info-base--blue').text().trim()
                ret.valorBruto = r.find('.td-protocolo-info:contains("Valor bruto"):first .td-protocolo-info--valor').text().trim()
                cb(ret)
            }
        })
    }

    function showResults(prots) {
        var x = window.open()
        window.sandboxPopup = x
        x.document.write('<html><head><style>table, th, td {border: 1px solid black;}</style></head><body><table><thead><tr>' +
            '<th>dia</th>' + 
            '<th>titulo</th>' + 
            '<th>qtd</th>' + 
            '<th>valorUnitario</th>' + 
            '<th>inst</th>' + 
            '<th>taxaInst</th>' + 
            '<th>taxaB3</th>' + 
            '<th>valorLiquido</th>' + 
            '<th>valorBruto</th>' + 
            '</tr></thread></table></body></html>')
        $('table tbody', window.sandboxPopup.document).remove()
        $('table', window.sandboxPopup.document).append('<tbody/>')
        var tbody = $('table tbody', window.sandboxPopup.document)
        prots.forEach(element => {
            var tr = tbody.append('<tr/>').children('tr:last')
            tr.append('<td/>').find('td:last').text(element.dia)
            tr.append('<td/>').find('td:last').text(element.titulo.toLowerCase().replaceAll(' ', '-'))
            tr.append('<td/>').find('td:last').text(element.qtd)
            tr.append('<td/>').find('td:last').text(element.valorUnitario.replaceAll('R$', '').replaceAll('.', ''))
            tr.append('<td/>').find('td:last').text(element.inst)
            tr.append('<td/>').find('td:last').text(element.taxaInst.replaceAll('R$', '').replaceAll('.', ''))
            tr.append('<td/>').find('td:last').text(element.taxaB3.replaceAll('R$', '').replaceAll('.', ''))
            tr.append('<td/>').find('td:last').text(element.valorLiquido.replaceAll('R$', '').replaceAll('.', ''))
            tr.append('<td/>').find('td:last').text(element.valorBruto.replaceAll('R$', '').replaceAll('.', ''))
        });
    }

    var protocoloIds = $('a[href^="/Protocolo/"]').map((a, b) => $(b).attr('href').split('/')[2]).toArray();
    var protocolos = []

    function nextProtocol() {
        if(protocoloIds.length == 0) {
            console.log('redering', protocolos)
            showResults(protocolos)
            console.log('done')
            return
        }
        loadProtocolo(protocoloIds.shift(), function(protocolo) {
            console.log(protocolo)
            protocolos.push(protocolo)
            nextProtocol()
            return
        })
    }

    nextProtocol()

    // protocolos.forEach(p => {
    //     console.log('protocolo', p)
    // })
    
    //loadProtocolo('20905065', function(protocolo) {
    //    console.log(protocolo)
    //    showResults([protocolo])
    //})

})(jQuery)
