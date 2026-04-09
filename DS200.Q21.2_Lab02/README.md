# DS200.Q21.2 Lab 02: Hotel Review Analysis with Apache Pig

**Student: Duong Quoc Nhut - ID: 23521132**

This repository contains the solution for Lab 02, focusing on processing and analyzing a dataset of hotel reviews using **Apache Pig**.

## Dataset
The analysis is performed on data located in the `data/` directory:
* `hotel-review.csv`: The main dataset containing hotel reviews. Each row represents a review segment with details such as ID, comment text, category, aspect, and sentiment.
* `stopwords.txt`: A dictionary of Vietnamese stop words used to filter out noise during the text preprocessing phase.

## Tasks Overview
Based on the lab requirements, this lab is divided into 5 sequential tasks:

* **Task 1: Data Preprocessing (`task01_preprocess.pig`)**
  Normalizes the dataset by converting all text to lowercase, removing punctuation, tokenizing sentences into individual words, and filtering out non-essential stop words.
* **Task 2: Basic Statistics (`task02_statistics.pig`)**
  Calculates overall word frequencies (retaining only words appearing > 500 times) and aggregates the total number of comments based on their `category` and `aspect`.
* **Task 3: Aspect Sentiment Analysis (`task03_aspect_sentiment.pig`)**
  Analyzes sentiments to determine which specific hotel aspects received the highest number of positive reviews and the highest number of negative reviews.
* **Task 4: Top Sentiment Words by Category (`task04_sentiment_words.pig`)**
  Extracts the top 5 most frequently used positive and negative words within each specific review category.
* **Task 5: Top General Words by Category (`task05_category_words.pig`)**
  Identifies the top 5 most frequently occurring words (regardless of sentiment) for each category to find the most relevant keywords.

## Prerequisites
To execute these scripts locally, ensure your system meets the following requirements:
* **Java 8** (OpenJDK 8 is highly recommended for Pig compatibility).
* **Apache Pig** (Version 0.17.0).
* A Unix-like environment (Linux, macOS, or Windows via **WSL**).

## How to Run

### Option 1: Automated Execution (Recommended)
You can run the entire analysis pipeline sequentially using a bash script. This script automatically cleans up old output directories and executes all tasks. 

Open your terminal, navigate to the `DS200.Q21.1_Lab02` root directory, and run:
```bash
bash scripts/run_pig_local.sh
```

### Option 2: Manual Execution
If you prefer to run the tasks individually to debug or observe the process, execute the following commands. Warning: You must manually delete the corresponding output/ folders before rerunning a script to avoid "Directory already exists" errors.

```bash
pig -x local pig/task01_preprocess.pig
pig -x local pig/task02_statistics.pig
pig -x local pig/task03_aspect_sentiment.pig
pig -x local pig/task04_sentiment_words.pig
pig -x local pig/task05_category_words.pig
```