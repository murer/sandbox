(function ($) {

    $.ajax({
        method: 'POST',
        url: 'Consulta/LoadOperacoes', 
        dataType: 'text', 
        contentType: 'application/json', 
        data: JSON.stringify({
            Operacao: "1", 
            InstituicaoFinanceira: "selecione", 
            dataInicial: "", 
            dataFinal: ""
        }), 
        success: function(result) {
            console.log('result', result)
        }
    })

})(jQuery)