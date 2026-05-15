package com.bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Bai5 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Bai5").setMaster("local[*]");
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.setLogLevel("ERROR");

            // (OccID -> OccName)
            JavaPairRDD<String, String> occRDD = sc.textFile("data/occupation.txt")
                    .mapToPair(line -> {
                        String[] p = line.split(",");
                        return new Tuple2<>(p[0], p[1]);
                    });

            // (UserID -> OccID)
            JavaPairRDD<String, String> userOccID = sc.textFile("data/users.txt")
                    .mapToPair(line -> {
                        String[] p = line.split(",");
                        return new Tuple2<>(p[0], p[3]); 
                    });

            JavaPairRDD<String, String> userOccName = userOccID
                    .mapToPair(t -> new Tuple2<>(t._2, t._1)) // (OccID, UserID)
                    .join(occRDD) // (OccID, (UserID, OccName))
                    .mapToPair(t -> new Tuple2<>(t._2._1, t._2._2)); // (UserID, OccName)

            // (UserID -> Rating)
            JavaPairRDD<String, Double> ratings = sc.textFile("data/ratings_1.txt").union(sc.textFile("data/ratings_2.txt"))
                    .mapToPair(line -> new Tuple2<>(line.split(",")[0], Double.parseDouble(line.split(",")[2])));

            // Join (UserID -> (Rating, OccName))
            ratings.join(userOccName)
                    .mapToPair(t -> new Tuple2<>(t._2._2, new Tuple2<>(t._2._1, 1))) // (OccName, (Rating, 1))
                    .reduceByKey((v1, v2) -> new Tuple2<>(v1._1 + v2._1, v1._2 + v2._2))
                    .mapValues(v -> Math.round((v._1 / v._2) * 100.0) / 100.0)
                    .collect()
                    .forEach(t -> System.out.println("Nghe nghiep: " + t._1 + " | Diem TB: " + t._2));
        }
    }
}