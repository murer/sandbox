(function($, $d) {

    $d.find('#wrapper').html('')

    function createRollTable(id) {
        var rt = $d.find('#' + id)
        if(!rt.length) {
            console.log('aa', $d.find('#wrapper'))
            $d.find('#wrapper').append('<div id="' + id + '"><h3>' + id + '</h3><table></table></div>')
        }
    }

    createRollTable('aa')
    createRollTable('aa')

})(jQuery, jQuery('body', window.roll21.window.document))