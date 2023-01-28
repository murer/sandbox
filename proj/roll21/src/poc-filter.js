(function($) {
    
    console.clear()

    $.ajaxPrefilter(function() {
        if (!window.roll21filter) return 
        window.roll21filter.apply(window, arguments) 
    })

})(jQuery)
