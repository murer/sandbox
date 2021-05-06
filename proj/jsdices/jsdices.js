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

class JSDices {

  parse(template, nameds, ...binds) {
    template = template.toString()
    return new JSDicesRoll(template)
  }

}


module.exports = new JSDices()


