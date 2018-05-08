
process.on('unhandledRejection', (reason, p) => {
  console.log('FAIL', reason);
  process.exit(1);
});

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
    this.testcases = [];
  }
  add(testcase) {
    let proto = Object.getPrototypeOf(testcase);
    testcase.testMethodNames = TestUtil.getTestMethodNames(proto);
    this.testcases.push(testcase);
  }
  async execute() {
    for(let testcase of this.testcases) {
      await testcase.execute();
    }
    console.log('Suite SUCCESS');
  }
}

class TestCase {
  async execute() {
    console.log(`TestCase [${this.constructor.name}] started`);
    for(let testMethodName of this.testMethodNames) {
      console.log(`TestMethod [${this.constructor.name}.${testMethodName}] started`);
      await this[testMethodName]();
    }
  }
}

exports.TestCase = TestCase;
exports.TestSuite = TestSuite;
exports.suite = new TestSuite();
