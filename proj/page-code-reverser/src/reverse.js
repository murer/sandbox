(function($) {
  
  var readPage = function() {
     $('.status').text('reading');
     //chrome.tabs.executeScript(null, {code:"alert('x ' + $('pre').length);"})
     //chrome.tabs.executeScript(null, {code:"chrome.extension.connect().postMessage({message: 'myCustomEvent'});"});
     //chrome.extension.connect().postMessage({message: 'myCustomEvent'});

     chrome.extension.onMessage.addListener(function(msg) {
       var html = msg.message;
       var array = html.split('');
       array.reverse();
       array = array.join('');
       $('.result').val(html + ' \n ' + array);
     });

     chrome.tabs.executeScript(null, { code: "chrome.extension.sendMessage(null, {message:'' + location + ' \\n\\n ' + $('body').html()});" });

     $('.status').text('sent');
     $('.status').hide();


  }

  $(document).ready(function() {

    $('.wtf').click(function() {
       var result = $('.result');
       var wtfPane = $('.wtfPane');
       var wtf = $(this);
       if(result.is(':visible')) {
          result.hide();
          wtfPane.show();
          wtf.text('Back');
       } else {
         wtfPane.hide();
         result.show();
         wtf.text('WTF?');
       }
    });

    $('.status').text('loading');

    //chrome.tabs.executeScript(null, {code:"document.body.style.backgroundColor='#777'"});
    chrome.tabs.executeScript(null, {file:"jquery.min.js"}, function() {
        readPage();
    });

  });
})(jQuery);