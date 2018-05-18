package com.murerz.sandbox.spark;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class CountNums {

	public static void main(String[] args) throws Exception {
		SparkConf conf = new SparkConf().setAppName("MyApp").setMaster("local[4]");
		JavaSparkContext sc = new JavaSparkContext(conf);

		for (int i = 0; i < 10; i++) {
			System.out.println("Try: " + i);
			FileUtils.deleteDirectory(new File("./target/result"));
			FileUtils.deleteDirectory(new File("./target/result2"));
			FileUtils.deleteDirectory(new File("./target/result3"));

			JavaRDD<String> words = sc.textFile("../gen/data/nums");

			JavaPairRDD<Integer, Integer> root = words.mapToPair(line -> new Tuple2<>(Integer.parseInt(line), 1));
			root.saveAsTextFile("./target/result3");

			root.reduceByKey((a, b) -> a + b).saveAsTextFile("./target/result");
			root.reduceByKey((a, b) -> a * b).saveAsTextFile("./target/result2");
		}

		sc.stop();
	}

}
