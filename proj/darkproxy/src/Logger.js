const { Console } = require('console');
const path = require('path');

const target = new Console(process.stderr);

class Logger {

  static create(name) {
    let ret = new Logger();
    ret.name = name;
    ret.simpleName = path.basename(ret.name).replace(/.js$/, '');
    return ret;
  }

  log(level, args) {
    target[level.toLowerCase()](`${new Date().toISOString()} ${level} [${this.simpleName}]`, ...args);
  }

  debug(...args) { return this.log('DEBUG', args); }
  info(...args) { return this.log('INFO', args); }
  error(...args) { return this.log('ERROR', args); }

}

const create = (name) => {
  return Logger.create(name);
}

const main = async () => {
  let log = Logger.create('abc');
  log.debug('xxxxx', {a:1});
}

if (require.main === module) {
  process.on('unhandledRejection', (reason, p) => {
    console.log('FAIL', reason);
    process.exit(1);
  });
  main(process.argv);
}

module.exports = create;
