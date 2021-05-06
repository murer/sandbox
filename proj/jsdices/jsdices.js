class JSDicesRoll {

  avg() {
    return 1
  }

  rnd() {
    return 1
  }

}

class JSDices {

  parse(template, nameds, ...binds) {
    return new JSDicesRoll()
  } 

} 


module.exports = new JSDices()


