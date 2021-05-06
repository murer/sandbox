/**
 * Parse dices in formula.
 *
 * @param {string} text Formula to be parsed
 * @return array of parsed parts
 */
 function diceParse(text) {
    text = text.toString()
    var myregexp = /([0-9]*)[dD]([0-9]+)/g;
    var match = myregexp.exec(text);
    var ret = []
    var offset = 0
    while (match != null) {
        if (match.index > offset) {
          ret.push({type: "literal", text: text.substring(offset, match.index)})
        } 
        var n = match[1] || "1"
        ret.push({type:"dice", text: match[0], n: parseInt(n), f: parseInt(match[2])})
        offset = match.index + match[0].length
        match = myregexp.exec(text);
    }
    if (offset < text.length) {
      ret.push({type: "literal", text: text.substring(offset, text.length)})
    } 
    return ret
  }
  
  /**
   * Compute average results.
   *
   * @param {string} text Formula
   * @return {number} average number
   * @customfunction
   */
  function diceAvg(text) {
    var parsed = diceParse(text)
    var ret = parsed.map(part => {
      if (part.type == "literal") return part.text
      var v = ((part.f + 1) / 2) * part.n
      return v.toString()
    })
    return 0 + eval(ret.join(""))
  }
  
  /**
   * Roll one dice.
   *
   * @param {number} face dice faces
   * @param {number} n number of rolls
   * @return {number} random roll
   * @customfunction
   */
  function diceRoll(face, n) {
    return jsdices.rollDice(face, n)
  }
  
  /**
   * Random roll.
   *
   * @param {string} text roll formula
   * @return {number} random result
   * @customfunction
   */
  function diceRandom(text) {
    return jsdices.parse(text).rnd()
  }
  
  /**
   * Random multiple times.
   *
   * @param {string} text roll formula
   * @param {number} times Number of times
   * @return random results
   * @customfunction
   */
  function diceStats(text, times) {
    return jsdices.parse(text).stats()
  } 
  
  /**
   * Random multiple times.
   *
   * @param {number} times Number of times
   * @param {string[]} texts roll formula
   * @return random results
   * @customfunction
   */
  function diceDataset(times, texts) {
    if (times > 500000) throw 'too many times: ' + times
    texts = texts[0]
    var tuples = {}
    for (var i = 0; i < times; i++) {
      texts.forEach((text, j) => {
        var result = Math.round(diceRandom(text)).toString()
        if (!tuples[result]) {
          tuples[result] = []
          for(var k = 0; k < texts.length; k++) tuples[result].push(0)
        }
        tuples[result][j]++
      })
    }
    var ret = Object.entries(tuples).map(n => {
      return [parseInt(n[0])].concat(n[1])
    }).sort((a, b) => a[0] - b[0]).map(l => {
      for(var i = 1; i < l.length; i++) {
        l[i] = (l[i]/times)
      }
      return l
    })
    return ret
  } 
  
  /**
   * Random multiple times.
   *
   * @param {number} times Number of times
   * @param {string[]} texts roll formula
   * @return random results
   * @customfunction
   */
  function diceDatasetX(times, texts) {
    texts = texts[0]
    var objs = texts.map(text => diceResults(text, times)).reduce((total, current, idx) => {
      current.forEach(v => {
        if (!total[v[0].toString()]) {
          total[v[0].toString()] = []
          for(var i = 0; i < texts.length; i++) total[v[0].toString()].push(0)
        }
        total[v[0].toString()][idx] += v[1]
      })
      return total
    }, {})
    var ret = Object.entries(objs).map(x => [parseInt(x[0])].concat(x[1])).sort((a, b) => a[0] - b[0]).map(l => {
      for(var i = 1; i < l.length; i++) {
        l[i] = (l[i]/times)
      }
      return l
    })
    return ret
  } 
  
  
  
  function testParse() {
    Logger.log("parsed: %s = %s = %s", diceParse("1+2d6 +(d10+2D4) + D12+4"), diceAvg("1+2d6 +(d10+2D4) + D12+4"), diceRandom("1+2d6 +(d10+2D4) + D12+4"))
    Logger.log("parsed: %s = %s = %s", diceParse("2d6"), diceAvg("2d6"), diceRandom("2d6"))
    Logger.log("parsed: %s = %s = %s", diceParse("10"), diceAvg("10"), diceRandom("10"))
  
    Logger.log("diceResults: %s", diceDataset(10, [["1d10+4+1d10+4+1d4+4", "2d6", "10"]]))
    Logger.log("diceResults: %s", diceResults("2d4", 10))
    Logger.log("diceResults: %s", diceResults("10", 10))
    Logger.log("diceResults: %s", diceResults(10, 10))
  }
  