(function($) {

    window.roll21 = {}
    
    $.ajaxPrefilter(function() {
        if (!window.roll21 || !window.roll21.filter) return 
        window.roll21.filter.apply(window, arguments) 
    })

})(jQuery);
