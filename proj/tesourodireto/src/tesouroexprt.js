(function ($) {

    function loadProtocolo(protocolo) {
        $.ajax({
            method: 'GET',
            url: '/Protocolo/' + protocolo + '/1', 
            dataType: 'html', 
            success: function(result) {
                console.log('result', result)
            }
        })
    }

})(jQuery)
