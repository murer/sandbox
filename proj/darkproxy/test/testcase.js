
class TestSuite {

  constructor() {
    this.cases = [];
    this.running = [];
    this.current = null;
  }

  add(testCase) {
    this.cases.push(testCase);
  }

  start() {
    this.running = [].concat(this.cases);
    if(this.running.length) {
      this.current = this.running.shift();
      this.current.start();
    }
  }

}

class TestCase {

  start() {
    let proto = Object.getPrototypeOf(this);
    Object.getOwnPropertyNames(proto).forEach(name => {
      if(name.startsWith('test')) {
        this[name](() => {

        });
      }
    })
  }

}

exports.TestCase = TestCase;
exports.TestSuite = TestSuite;
exports.suite = new TestSuite();
