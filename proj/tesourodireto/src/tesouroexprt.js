(function ($) {

    function loadProtocolo(protocolo, cb) {
        $.ajax({
            method: 'GET',
            url: '/Protocolo/' + protocolo + '/1', 
            dataType: 'html', 
            success: function(result) {
                console.log('result', result)
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
                cb(ret)
            }
        })
    }

    function showResults(prots) {
        x = window.open()
        x.document.write('<html><body><table><thead><tr><th>aa</th><th>bbb</th></tr></thread></tbody></tbody></table></body></html>')
    }

    var protocolos = $('a[href^="/Protocolo/"]').map((a, b) => $(b).attr('href').split('/')[2]).toArray();
    // protocolos.forEach(p => {
    //     console.log('protocolo', p)
    // })
    
    loadProtocolo('20905065', function(protocolo) {
        console.log(protocolo)
        showResults([protocolo])
    })

})(jQuery)
