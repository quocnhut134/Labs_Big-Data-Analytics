import collections

def mapper():
    users_gender = {}
    with open('./data/users.txt', 'r', encoding='utf-8') as f:
        for line in f:
            if not line.strip() or line.startswith('['):
                continue
            parts = line.strip().split(', ')
            if len(parts) >= 2:
                user_id = parts[0]
                gender = parts[1]
                users_gender[user_id] = gender

    movies_title = {}
    with open('./data/movies.txt', 'r', encoding='utf-8') as f:
        for line in f:
            if not line.strip() or line.startswith('['):
                continue
            parts = line.strip().split(', ')
            if len(parts) >= 2:
                movie_id = parts[0]
                title = parts[1]
                movies_title[movie_id] = title

    grouped_gender_ratings = collections.defaultdict(list)
    for filename in ['./data/ratings_1.txt', './data/ratings_2.txt']:
        with open(filename, 'r', encoding='utf-8') as f:
            for line in f:
                if not line.strip() or line.startswith('['):
                    continue
                
                parts = line.strip().split(', ')
                if len(parts) >= 3:
                    user_id = parts[0]
                    movie_id = parts[1]
                    try:
                        rating = float(parts[2])
                    except ValueError:
                        continue
                    
                    gender = users_gender.get(user_id)
                    title = movies_title.get(movie_id)
                    
                    if gender and title:
                        grouped_gender_ratings[title].append((gender, rating))
                        
    return grouped_gender_ratings

class GenderAnalyzerReducer:
    def __init__(self, output_file):
        self.output_file = output_file

    def reduce(self, movie_title, gender_ratings):
        male_ratings = [rating for gender, rating in gender_ratings if gender == 'M']
        female_ratings = [rating for gender, rating in gender_ratings if gender == 'F']
        
        male_avg = sum(male_ratings) / len(male_ratings) if male_ratings else None
        female_avg = sum(female_ratings) / len(female_ratings) if female_ratings else None
        
        m_str = f"{male_avg:.2f}" if male_avg is not None else "N/A"
        f_str = f"{female_avg:.2f}" if female_avg is not None else "N/A"
        
        self.output_file.write(f"{movie_title}: Male: {m_str}, Female: {f_str}\n")

if __name__ == "__main__":
    grouped_data = mapper()
    
    with open('./results/result_bai3.txt', 'w', encoding='utf-8') as out_f:
        reducer = GenderAnalyzerReducer(out_f)
        
        for title, ratings_list in sorted(grouped_data.items()):
            reducer.reduce(title, ratings_list)