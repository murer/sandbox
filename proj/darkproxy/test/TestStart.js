
const suite = require('./TestCase').suite;

const test = (name) => {
  let classes = require(name);
  for(let className in classes) {
    let clazz = classes[className];
    suite.add(new clazz());
  }
}

const main = async () => {

  test('./SimpleTest');

  await suite.execute();
}

if (require.main === module) {
    main();
}
