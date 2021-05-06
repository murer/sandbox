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
  let myregexp = /\$\{([A-Za-z0-9_]+)\}/g;
  let match = myregexp.exec(template);
  let ret = ""
  let offset = 0
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

const parseBinds = (template, binds) => {
  let myregexp = /\?/g;
  let match = myregexp.exec(template);
  let ret = ""
  let offset = 0
  let idx = 0
  while (match != null) {
    if (match.index > offset) {
      ret += template.substring(offset, match.index)
    }
    let value = binds[idx++]
    ret += value
    offset = match.index + match[0].length
    console.log('bbb: ', template, value, match.index, template.substr(match.index, match[0].length), ret)
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
    template= parseBinds(template, binds)
    return new JSDicesRoll(template)
  }

}


module.exports = new JSDices()


