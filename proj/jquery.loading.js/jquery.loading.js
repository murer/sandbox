(function($) {

	function panel() {
		var loading = $('.loading');
		if (!loading.length) {
			loading = $('<div class="loading"><div class="loading-overlay" /><div class="loading-panel"><ul/></div></div>').hide().appendTo('body');
		}
		return loading;
	}

	function stick() {
		// var top = $(window).scrollTop()
		// $('.loading').css('top', top + 'px');
		// var height= $('.loading').css('height', ($('body').height() - top -
		// 2) + 'px').height();
		// var top = $(window).scrollTop();
		// if ($.loading.stick) {
		// setTimeout($.loading.stick, 1);
		// }
	}

	// ********************************************
	$.loading = {
		push : function(opts) {
			var p = panel();
			var li = $('<li/>').appendTo(p.find('ul')).hide().data('loading', opts);
			var msg = opts.loadingMessage;
			if (opts.url && !msg && typeof (msg) != 'string') {
				msg = opts.url.replace(/_=\d+/g, '');
				msg = msg.replace(/[\?\&]$/, '');
			}
//			if (msg) {
//				li.text(msg).show();
//			}
			if (p.find('li').length == 1 && jQuery.isReady) {
				p.show();
				$.loading.stick = stick;
				$.loading.stick();
			}
		},

		pop : function(opts) {
			var p = panel();
			var list = p.find('li');
			if (list.length < 1) {
				throw 'wrong: ' + stack.length;
			}
			list.filter(function() {
				return $(this).data('loading') === opts;
			}).addClass('loading-loaded');
			if (list.filter(':not(.loading-loaded)').length == 0 && jQuery.isReady) {
				$.loading.stick = null;
				p.hide();
				p.find('ul').html('');
			}
		}
	}

	// ********************************************
	$(document).ajaxSend(function(evt, xhr, ajax) {
		if (ajax.loading) {
			$.loading.push(ajax);
		}

	});
	$(document).ajaxComplete(function(evt, xhr, ajax) {
		if (ajax.loading) {
			$.loading.pop(ajax);
		}
	});

	$.ajaxSetup({
		loading : true
	});

//	$(window).ready(function() {
//		var s = $.service.loading('aaa');
//		setTimeout(function() {
//			s.done()
//		}, 5000);
//	})

})(jQuery);