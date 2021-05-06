class JSDicesRoll {

  constructor(compiled) {
    this.compiled = compiled
  }

  avg() {
    let ret = this.compiled.map(part => {
      if (part.type == "literal") return part.template
      let v = ((part.f + 1) / 2) * part.n
      return v.toString()
    })
    return 0 + eval(ret.join(""))
  }

  rnd() {
    let a = this.compiled.map(part => {
      if (part.type == "literal") return part.template
      return me.rollDice(part.f, part.n).toString()
    })
    let f = a.join("")
    let ret = 0 + eval(f)
    return ret
  }

  stats(times) {
    return null
  }

  toString() {
    return this.compiled.map((c) => c.template).join('')
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
    match = myregexp.exec(template);
  }
  if (offset < template.length) {
    ret += template.substring(offset, template.length)
  }
  return ret
}

const parseDices = (template) => {
  let myregexp = /([0-9]*)[dD]([0-9]+)/g;
  let match = myregexp.exec(template);
  let ret = []
  let offset = 0
  while (match != null) {
    if (match.index > offset) {
      ret.push({ type: "literal", template: template.substring(offset, match.index) })
    }
    let n = match[1] || "1"
    ret.push({ type: "dice", template: match[0], n: parseInt(n), f: parseInt(match[2]) })
    offset = match.index + match[0].length
    match = myregexp.exec(template);
  }
  if (offset < template.length) {
    ret.push({ type: "literal", template: template.substring(offset, template.length) })
  }
  return ret
}

class JSDices {

  parse(template, nameds, ...binds) {
    template = template.toString()
    template = parseNameds(template, nameds)
    template = parseBinds(template, binds)
    let compiled = parseDices(template)
    return new JSDicesRoll(compiled)
  }

  rollDice(face, n) {
    n = n || 1
    let ret = 0
    for (let i = 0; i < n; i++) {
      let v = Math.floor(Math.random() * face) + 1
      ret += v
    }
    return ret
  }
}

const me = new JSDices()

module.exports = me


