        (function($, $d) {

            function hack(options) {
                var roll21try = 'r21try' + Math.random().toString().replace('.', '')
                $.ajax({
                    type: options.type,
                    url: options.url,
                    data: options.data,
                    dataType: options.dataType,
                    contentType: options.contentType,
                    roll21try: roll21try,
                    roll21id: options.roll21id, 
                    roll21: true,
                    success: function(resp, status, jqXHR) {
                        var req = JSON.parse(options.data)
                        roll21.addRow(options.roll21id, roll21try, req, resp, {
                            'use': function() {
                                options.success(resp, status, jqXHR)
                                roll21.removeRollTable(options.roll21id)
                            }
                        })
                    }
                })
            }

            window.roll21.filter = function(options, originalOptions, jqXHR) {
                if (!options.url) return
                if (!options.url.endsWith('/doroll')) return
                if (!options.type == 'POST') return
                if (options.complete) return
                if (options.roll21) return
                console.log('data', JSON.parse(options.data))
                console.log('original', options)
                jqXHR.abort()
                options.roll21id = 'r21id' + Math.random().toString().replace('.', '')
                hack(options)
            }

        })(jQuery, jQuery('body', window.roll21.window.document))