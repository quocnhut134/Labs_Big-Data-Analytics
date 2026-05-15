package com.bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class Bai2 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Bai2").setMaster("local[*]");
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.setLogLevel("ERROR");

            JavaPairRDD<String, String[]> movieGenres = sc.textFile("data/movies.txt")
                    .mapToPair(line -> {
                        String[] parts = line.split(",", 3);
                        String[] genres = parts.length > 2 ? parts[2].split("\\|") : new String[0];
                        return new Tuple2<>(parts[0], genres);
                    });

            JavaPairRDD<String, Double> ratings = sc.textFile("data/ratings_1.txt").union(sc.textFile("data/ratings_2.txt"))
                    .mapToPair(line -> {
                        String[] parts = line.split(",");
                        return new Tuple2<>(parts[1], Double.parseDouble(parts[2]));
                    });

            JavaPairRDD<String, Tuple2<Double, Integer>> genreRatings = ratings.join(movieGenres)
                    .flatMapToPair(t -> {
                        Double rating = t._2._1;
                        String[] genres = t._2._2;
                        return Arrays.stream(genres)
                                .map(g -> new Tuple2<>(g, new Tuple2<>(rating, 1)))
                                .iterator();
                    });

            genreRatings.reduceByKey((v1, v2) -> new Tuple2<>(v1._1 + v2._1, v1._2 + v2._2))
                    .mapValues(v -> Math.round((v._1 / v._2) * 100.0) / 100.0)
                    .sortByKey()
                    .collect()
                    .forEach(t -> System.out.println("The loai: " + t._1 + " | Diem TB: " + t._2));
        }
    }
}