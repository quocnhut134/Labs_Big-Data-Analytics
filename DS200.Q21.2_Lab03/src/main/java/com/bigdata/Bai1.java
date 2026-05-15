package com.bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import scala.Tuple3;

import java.util.List;

public class Bai1 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Bai1").setMaster("local[*]");
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.setLogLevel("ERROR");

            JavaPairRDD<String, String> moviesRDD = sc.textFile("data/movies.txt")
                    .mapToPair(line -> {
                        String[] parts = line.split(",", 3);
                        return new Tuple2<>(parts[0], parts[1]);
                    });

            JavaRDD<String> rawRatings = sc.textFile("data/ratings_1.txt").union(sc.textFile("data/ratings_2.txt"));
            JavaPairRDD<String, Tuple2<Double, Integer>> ratingsRDD = rawRatings
                    .mapToPair(line -> {
                        String[] parts = line.split(",");
                        return new Tuple2<>(parts[1], new Tuple2<>(Double.parseDouble(parts[2]), 1));
                    });

            JavaPairRDD<String, Tuple2<Double, Integer>> aggregatedRatings = ratingsRDD
                    .reduceByKey((v1, v2) -> new Tuple2<>(v1._1 + v2._1, v1._2 + v2._2));

            JavaPairRDD<String, Tuple3<String, Double, Integer>> movieStats = aggregatedRatings
                    .mapValues(v -> new Tuple2<>(v._1 / v._2, v._2)) // (MovieID, (AvgRating, Count))
                    .join(moviesRDD) // (MovieID, ((AvgRating, Count), Title))
                    .mapValues(v -> new Tuple3<>(v._2, v._1._1, v._1._2)) // (MovieID, (Title, AvgRating, Count))
                    .filter(tuple -> tuple._2._3() >= 5); 

            List<Tuple2<String, Tuple3<String, Double, Integer>>> topMovie = movieStats
                    .mapToPair(t -> new Tuple2<>(t._2._2(), t)) 
                    .sortByKey(false) 
                    .values()
                    .take(1);

            System.out.println("Ket qua bai 1:");
            movieStats.collect().forEach(t -> System.out.printf("Phim: %s | Diem TB: %.2f | So luot: %d\n", t._2._1(), t._2._2(), t._2._3()));
            if (!topMovie.isEmpty()) {
                System.out.printf("\n--> Phim xuat sac nhat (>=5 danh gia): %s (Diem: %.2f)\n", topMovie.get(0)._2._1(), topMovie.get(0)._2._2());
            }
        }
    }
}