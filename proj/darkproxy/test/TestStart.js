
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
  test('./http/FileHttpServerTest');
  test('./io/IOTest');

  await suite.execute();
}

if (require.main === module) {
    main();
}
