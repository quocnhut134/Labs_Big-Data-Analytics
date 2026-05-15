package com.bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.time.Instant;
import java.time.ZoneId;

public class Bai6 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Bai6").setMaster("local[*]");
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.setLogLevel("ERROR");

            JavaPairRDD<Integer, Tuple2<Double, Integer>> yearRatings = sc.textFile("data/ratings_1.txt").union(sc.textFile("data/ratings_2.txt"))
                    .mapToPair(line -> {
                        String[] parts = line.split(",");
                        Double rating = Double.parseDouble(parts[2]);
                        long timestamp = Long.parseLong(parts[3]);

                        int year = Instant.ofEpochSecond(timestamp)
                                .atZone(ZoneId.systemDefault())
                                .getYear();

                        return new Tuple2<>(year, new Tuple2<>(rating, 1));
                    });

            yearRatings.reduceByKey((v1, v2) -> new Tuple2<>(v1._1 + v2._1, v1._2 + v2._2))
                    .mapValues(v -> new Tuple2<>(Math.round((v._1 / v._2) * 100.0) / 100.0, v._2)) // (Avg, Count)
                    .sortByKey()
                    .collect()
                    .forEach(t -> System.out.printf("Nam: %d | Luot danh gia: %d | Diem TB: %.2f\n", t._1, t._2._2, t._2._1));
        }
    }
}