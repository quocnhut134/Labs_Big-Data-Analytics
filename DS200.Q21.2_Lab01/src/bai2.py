import collections

def mapper():
    movie_genres = {}
    with open('./data/movies.txt', 'r', encoding='utf-8') as f:
        for line in f:
            if not line.strip() or line.startswith('['):
                continue
                
            parts = line.strip().split(', ')
            if len(parts) >= 3:
                movie_id = parts[0]
                genres_string = parts[-1]
                genres_list = genres_string.split('|')
                movie_genres[movie_id] = genres_list

    genre_ratings_map = collections.defaultdict(list)
    for filename in ['./data/ratings_1.txt', './data/ratings_2.txt']:
        with open(filename, 'r', encoding='utf-8') as f:
            for line in f:
                if not line.strip() or line.startswith('['):
                    continue
                    
                parts = line.strip().split(', ')
                if len(parts) >= 3:
                    movie_id = parts[1]
                    try:
                        rating = float(parts[2])
                    except ValueError:
                        continue 
                    
                    if movie_id in movie_genres:
                        for genre in movie_genres[movie_id]:
                            genre_ratings_map[genre].append(rating)
                            
    return genre_ratings_map

class GenreReducer:
    def __init__(self, output_file):
        self.output_file = output_file

    def reduce(self, genre, ratings):
        total_ratings = len(ratings)
        if total_ratings == 0:
            return
            
        avg_rating = sum(ratings) / total_ratings
        
        self.output_file.write(f"{genre} Avg: {avg_rating:.2f}, Count: {total_ratings}\n")

if __name__ == "__main__":
    grouped_genre_ratings = mapper()
    
    with open('./results/result_bai2.txt', 'w', encoding='utf-8') as out_f:
        reducer = GenreReducer(out_f)
        
        for genre, ratings_list in sorted(grouped_genre_ratings.items()):
            reducer.reduce(genre, ratings_list)