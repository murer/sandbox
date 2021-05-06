class JSDicesRoll {

  avg() {
    return 1
  }

  rnd() {
    return 1
  }

  toString() {
    return "aaa"
  }

}

class JSDices {

  parse(template, nameds, ...binds) {
    return new JSDicesRoll()
  } 

} 


module.exports = new JSDices()


