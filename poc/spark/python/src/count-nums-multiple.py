from pyspark import SparkConf, SparkContext
import shutil

conf = (SparkConf()
         .setMaster("local[4]")
         .setAppName("MyApp")
         .set("spark.executor.memory", "1g")
         .set('spark.local.dir', './target/tmp'))

sc = SparkContext(conf = conf)

for k in range(10):
    print 'try', k
    shutil.rmtree('target/result', True)
    shutil.rmtree('target/result2', True)
    shutil.rmtree('target/result3', True)

    words = sc.textFile("./gen/data/nums")
    #print 'count', words.count()

    root = words.map(lambda line: (int(line), 1))
    root.saveAsTextFile('./target/result3')

    root.reduceByKey(lambda a, b: a + b) \
      .saveAsTextFile('./target/result')

    root.reduceByKey(lambda a, b: a * b) \
      .saveAsTextFile('./target/result2')

sc.stop()
