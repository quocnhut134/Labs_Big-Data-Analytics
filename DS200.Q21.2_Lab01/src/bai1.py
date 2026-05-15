import collections

def mapper():
    movies = {}
    with open('./data/movies.txt', 'r', encoding='utf-8') as f:
        for line in f:
            parts = line.strip().split(', ')
            if len(parts) >= 2:
                movie_id = parts[0]
                title = parts[1]
                movies[movie_id] = title

    ratings_data = collections.defaultdict(list)
    for filename in ['./data/ratings_1.txt', './data/ratings_2.txt']:
        with open(filename, 'r', encoding='utf-8') as f:
            for line in f:
                parts = line.strip().split(', ')
                if len(parts) >= 3:
                    movie_id = parts[1]
                    rating = float(parts[2])
                    ratings_data[movie_id].append(rating)
                    
    return movies, ratings_data

class MovieReducer:
    def __init__(self, output_file):
        self.maxMovie = ""
        self.maxRating = -1.0
        self.output_file = output_file

    def reduce(self, movie_title, ratings):
        total_ratings = len(ratings)
        if total_ratings == 0:
            return
            
        avg_rating = sum(ratings) / total_ratings

        self.output_file.write(f"{movie_title} AverageRating: {avg_rating:.2f} (TotalRatings: {total_ratings})\n")

        if total_ratings >= 5:
            if avg_rating > self.maxRating:
                self.maxRating = avg_rating
                self.maxMovie = movie_title

    def cleanup(self):
        self.output_file.write("-" * 50 + "\n")
        if self.maxMovie != "":
            self.output_file.write(f"{self.maxMovie} is the highest rated movie with an average rating of {self.maxRating:.2f} among movies with at least 5 ratings.\n")
        else:
            self.output_file.write("No movie has at least 5 ratings in the current dataset.\n")

if __name__ == "__main__":
    movies_dict, grouped_ratings = mapper()
    
    with open('./results/result_bai1.txt', 'w', encoding='utf-8') as out_f:
        reducer = MovieReducer(out_f)
        
        for movie_id, ratings_list in grouped_ratings.items():
            title = movies_dict.get(movie_id, f"Unknown Movie ({movie_id})")
            reducer.reduce(title, ratings_list)
            
        reducer.cleanup()
        