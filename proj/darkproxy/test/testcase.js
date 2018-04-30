
class TestUtil {

  static getTestMethodNames(clazz) {
    let names = {};
    do {
      Object.getOwnPropertyNames(clazz).forEach(name => {
        if(name.startsWith('test')) {
          names[name] = true;
        }
      });
      clazz = Object.getPrototypeOf(clazz);
    } while(clazz instanceof TestCase);
    return Object.keys(names);
  }

}

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
    this.current = null;
    this.running = [].concat(this.cases);
    this._next(() => {
      console.log('SUCCESS')
    });
  }

  _next(end) {
    if(!this.running.length) {
      end();
      return;
    }
    this.current = this.running.shift();
    //console.log(`TestCase [${this.current.constructor.name}] started`);
    this.current.start(() => {
      //console.log(`TestCase [${this.current.constructor.name}] ended`);
      this._next(end);
    });
  }

}

class TestCase {

  start(end) {
    this.current = null;
    let proto = Object.getPrototypeOf(this);
    this.running = TestUtil.getTestMethodNames(proto);
    this._next(end);
  }

  _next(end) {
    if(!this.running.length) {
      end();
      return;
    }
    this.current = this.running.shift();
    console.log(`Test [${this.constructor.name}.${this.current}] started`);
    this[this.current](() => {
      //console.log(`Test [${this.constructor.name}.${this.current}] ended`);
      this._next(() => {
        end();
      });
    });
  }

}

exports.TestCase = TestCase;
exports.TestSuite = TestSuite;
exports.suite = new TestSuite();
