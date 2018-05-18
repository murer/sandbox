from pyspark import SparkConf, SparkContext
conf = (SparkConf()
         .setMaster("local[4]")
         .setAppName("MyApp")
         .set("spark.executor.memory", "1g")
         .set('spark.local.dir', './target/tmp'))
sc = SparkContext(conf = conf)

def test(a):
    print 'a', a

words = sc.binaryRecords("./gen/data/nums", 3)
words = words.map(test)
words.saveAsTextFile('./target/result3')

sc.stop()
