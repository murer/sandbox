package com.murerz.sandbox.spark;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.murerz.sandbox.spark.xml.XmlInputFormat;

import scala.Tuple2;

public class CountXmls {

	public static void main(String[] args) throws Exception {
		JobConf jobConf = new JobConf();
		SparkConf conf = new SparkConf().setAppName("MyApp").setMaster("local[4]");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// JavaPairRDD<LongWritable, Text> records = sc.hadoopRDD(jobConf,
		// XmlInputFormat.class, LongWritable.class, Text.class);

		FileUtils.deleteDirectory(new File("./target/result"));

		// JavaRDD<String> words = sc.textFile("../gen/data/xmls");

		JavaPairRDD<LongWritable, Text> words = sc.newAPIHadoopFile("../gen/data/xmls", XmlInputFormat.class,
				LongWritable.class, Text.class, new Configuration());

		JavaPairRDD<Integer, Integer> root = words.mapToPair(line -> {
//			System.out.println("line: " + line);
			return new Tuple2<>(1, 1);
		});

		// JavaPairRDD<Integer, Integer> root = words.mapToPair(line -> new
		// Tuple2<>(Integer.parseInt(line), 1));
		// root.saveAsTextFile("./target/result3");

		root.reduceByKey((a, b) -> a + b).saveAsTextFile("./target/result");
		// root.reduceByKey((a, b) -> a * b).saveAsTextFile("./target/result2");

		sc.stop();
	}

}
