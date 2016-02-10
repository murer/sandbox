var fs = require('fs');

var base = 'config/containers';

exports.all = function(callback) {
  fs.readdir(base, function(err, items) {
    if(err) throw err;
    var ret = {};
    var total = 0;
    items.forEach(function(file) {
      var name = file.replace(/\.json$/g, '');
      total++;
      ret[name] = null;
      fs.readFile(base + '/' + file, 'utf-8', function(err, content) {
        if(err) throw err;
        ret[name] = JSON.parse(content);
        total--;
        if(total == 0) {
          callback(ret);
        }
      });
    });
  });
}
