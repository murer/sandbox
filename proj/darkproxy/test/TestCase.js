
const LOG = require('../src/log.js')(__filename);

process.on('unhandledRejection', (reason, p) => {
  LOG.info('FAIL', reason);
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
    LOG.info('Suite SUCCESS');
  }
}

class TestCase {
  async execute() {
    LOG.info(`TestCase [${this.constructor.name}] started`);
    for(let testMethodName of this.testMethodNames) {
      LOG.info(`TestMethod [${this.constructor.name}.${testMethodName}] started`);
      await this[testMethodName]();
    }
  }
}

exports.TestCase = TestCase;
exports.TestSuite = TestSuite;
exports.suite = new TestSuite();
