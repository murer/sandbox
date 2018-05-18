from pyspark import SparkConf, SparkContext
from pyspark.streaming import StreamingContext

conf = (SparkConf()
         .setMaster("local[4]")
         .setAppName("MyApp")
         .set("spark.executor.memory", "1g")
         .set('spark.local.dir', './target/tmp'))
sc = SparkContext(conf = conf)
ssc = StreamingContext(sc, 10)

words = ssc.textFileStream("./gen/data/nums")
#print 'count', words.count()

root = words.map(lambda line: (int(line), 1))

root.saveAsTextFile('./target/result3')

#root.reduceByKey(lambda a, b: a + b) \
#  .saveAsTextFile('./target/result')

#root.reduceByKey(lambda a, b: a * b) \
#  .saveAsTextFile('./target/result2')

ssc.stop()
