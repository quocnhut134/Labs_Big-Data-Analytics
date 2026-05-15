package com.bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Bai3 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Bai3").setMaster("local[*]");
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.setLogLevel("ERROR");

            // Map (UserID -> Gender)
            JavaPairRDD<String, String> userGender = sc.textFile("data/users.txt")
                    .mapToPair(line -> {
                        String[] p = line.split(",");
                        return new Tuple2<>(p[0], p[1]);
                    });

            // Map (UserID -> (MovieID, Rating))
            JavaPairRDD<String, Tuple2<String, Double>> ratings = sc.textFile("data/ratings_1.txt").union(sc.textFile("data/ratings_2.txt"))
                    .mapToPair(line -> {
                        String[] p = line.split(",");
                        return new Tuple2<>(p[0], new Tuple2<>(p[1], Double.parseDouble(p[2])));
                    });

            ratings.join(userGender) // (UserID, ((MovieID, Rating), Gender))
                    .mapToPair(t -> {
                        String movieId = t._2._1._1;
                        Double rating = t._2._1._2;
                        String gender = t._2._2;
                        return new Tuple2<>(movieId + " - Gioi tinh: " + gender, new Tuple2<>(rating, 1));
                    })
                    .reduceByKey((v1, v2) -> new Tuple2<>(v1._1 + v2._1, v1._2 + v2._2))
                    .mapValues(v -> Math.round((v._1 / v._2) * 100.0) / 100.0)
                    .sortByKey()
                    .take(20) 
                    .forEach(t -> System.out.println("Phim " + t._1 + " | Diem TB: " + t._2));
        }
    }
}