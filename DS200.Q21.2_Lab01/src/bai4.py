import collections

def get_age_group(age):
    if age <= 18:
        return '0-18'
    elif age <= 35:
        return '18-35'
    elif age <= 50:
        return '35-50'
    else:
        return '50+'

def mapper():
    users_age_group = {}
    with open('./data/users.txt', 'r', encoding='utf-8') as f:
        for line in f:
            if not line.strip() or line.startswith('['):
                continue
            parts = line.strip().split(', ')
            if len(parts) >= 3:
                user_id = parts[0]
                try:
                    age = int(parts[2]) 
                    users_age_group[user_id] = get_age_group(age)
                except ValueError:
                    continue

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

    grouped_age_ratings = collections.defaultdict(list)
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
                    
                    age_group = users_age_group.get(user_id)
                    title = movies_title.get(movie_id)
                    
                    if age_group and title:
                        grouped_age_ratings[title].append((age_group, rating))
                        
    return grouped_age_ratings

class AgeGroupReducer:
    def __init__(self, output_file):
        self.output_file = output_file
        self.age_categories = ['0-18', '18-35', '35-50', '50+']

    def reduce(self, movie_title, ratings):
        group_stats = {category: [] for category in self.age_categories}
        
        for age_group, rating in ratings:
            if age_group in group_stats:
                group_stats[age_group].append(rating)
        
        results = []
        for category in self.age_categories:
            category_ratings = group_stats[category]
            if category_ratings: 
                avg_rating = sum(category_ratings) / len(category_ratings)
                results.append(f"{category}: {avg_rating:.2f}")
            else: 
                results.append(f"{category}: N/A")

        formatted_result = ", ".join(results)
        self.output_file.write(f"{movie_title}: [{formatted_result}]\n")

if __name__ == "__main__":
    grouped_data = mapper()
    
    with open('./results/result_bai4.txt', 'w', encoding='utf-8') as out_f:
        reducer = AgeGroupReducer(out_f)
        
        for title, ratings_list in sorted(grouped_data.items()):
            reducer.reduce(title, ratings_list)