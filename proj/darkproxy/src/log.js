
const target = console;

class Logger {

  static create(name) {
    let ret = new Logger();
    ret.name = name;
    return ret;
  }

  log(level, args) {
    target[level.toLowerCase()](`${new Date().toISOString()} ${this.name} [${level}]`, ...args);
  }

  debug(...args) { return this.log('DEBUG', args); }
  info(...args) { return this.log('INFO', args); }
  error(...args) { return this.log('ERROR', args); }

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
