from pyspark import SparkConf, SparkContext
conf = (SparkConf()
         .setMaster("local[4]")
         .setAppName("MyApp")
         .set("spark.executor.memory", "1g")
         .set('spark.local.dir', './target/tmp'))
sc = SparkContext(conf = conf)

words = sc.textFile("./gen/data/nums")
#print 'count', words.count()

root = words.map(lambda line: (int(line), 1))

root.reduceByKey(lambda a, b: a + b) \
  .saveAsTextFile('./target/result')

root.reduceByKey(lambda a, b: a * b) \
  .saveAsTextFile('./target/result2')

sc.stop()
