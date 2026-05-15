package com.bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Bai4 {
    private static String getAgeGroup(int age) {
        if (age < 18) return "Duoi 18";
        if (age <= 35) return "18-35";
        if (age <= 50) return "36-50";
        return "Tren 50";
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Bai4").setMaster("local[*]");
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.setLogLevel("ERROR");

            JavaPairRDD<String, String> userAgeGroups = sc.textFile("data/users.txt")
                    .mapToPair(line -> {
                        String[] p = line.split(",");
                        return new Tuple2<>(p[0], getAgeGroup(Integer.parseInt(p[2])));
                    });

            JavaPairRDD<String, Tuple2<String, Double>> ratings = sc.textFile("data/ratings_1.txt").union(sc.textFile("data/ratings_2.txt"))
                    .mapToPair(line -> {
                        String[] p = line.split(",");
                        return new Tuple2<>(p[0], new Tuple2<>(p[1], Double.parseDouble(p[2])));
                    });

            ratings.join(userAgeGroups)
                    .mapToPair(t -> new Tuple2<>(t._2._1._1 + " - Nhom tuoi: " + t._2._2, new Tuple2<>(t._2._1._2, 1)))
                    .reduceByKey((v1, v2) -> new Tuple2<>(v1._1 + v2._1, v1._2 + v2._2))
                    .mapValues(v -> Math.round((v._1 / v._2) * 100.0) / 100.0)
                    .sortByKey()
                    .take(20)
                    .forEach(t -> System.out.println("Phim " + t._1 + " | Diem TB: " + t._2));
        }
    }
}