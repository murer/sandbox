class JSDicesRoll {

  constructor(roll) {
    this.roll = roll
  }

  avg() {
    return 1
  }

  rnd() {
    return 1
  }

  toString() {
    return this.roll
  }

}

const parseNameds = (template, nameds) => {
  var myregexp = /\$\{([A-Za-z0-9_]+)\}/g;
  var match = myregexp.exec(template);
  var ret = ""
  var offset = 0
  while (match != null) {
    if (match.index > offset) {
      ret += template.substring(offset, match.index)
    }
    let name = match[1]
    let value = nameds[name]
    ret += value
    offset = match.index + match[0].length
    console.log('aaa: ', template, name, value, match.index, template.substr(match.index, match[0].length), ret)
    match = myregexp.exec(template);
  }
  if (offset < template.length) {
    ret += template.substring(offset, template.length)
  }
  return ret
}

class JSDices {

  parse(template, nameds, ...binds) {
    template = template.toString()
    template = parseNameds(template, nameds)
    return new JSDicesRoll(template)
  }

}


module.exports = new JSDices()


