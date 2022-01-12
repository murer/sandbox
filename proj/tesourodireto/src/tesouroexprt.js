(function ($) {

    function loadProtocolo(protocolo) {
        $.ajax({
            method: 'GET',
            url: '/Protocolo/34575289/1', 
            dataType: 'html', 
            success: function(result) {
                console.log('result', result)
            }
        })
    }

})(jQuery)
