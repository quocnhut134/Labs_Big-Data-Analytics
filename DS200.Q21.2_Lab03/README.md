# DS200.Q21.2 Lab 03

**Student: Duong Quoc Nhut - ID: 23521132**

## Introduction
This is a practical assignment for Big Data Analytics Course using the **Apache Spark RDD API** and **Java**. It performs 6 data analysis tasks on a dataset consisting of movie information, user ratings, and user demographics.

## Project Structure
The project follows the standard Maven directory structure:


```
SparkMovieAnalysis/
├── pom.xml                  
├── data/                    
│   ├── movies.txt
│   ├── ratings_1.txt
│   ├── ratings_2.txt
│   ├── users.txt
│   └── occupation.txt
└── src/
    └── main/
        └── java/
            └── com/
                └── bigdata/
                    ├── Bai1.java  # Avg rating and total count per movie
                    ├── Bai2.java  # Genre-based analysis
                    ├── Bai3.java  # Gender-based analysis
                    ├── Bai4.java  # Age Group analysis
                    ├── Bai5.java  # Occupation-based analysis
                    └── Bai6.java  # Time-based analysis (Yearly)

```

## Prerequisites

To run this project locally (especially on Windows), you will need:

1. **Java Development Kit (JDK):** JDK 11 or JDK 17 is recommended.
2. **Apache Maven:** For dependency management.
3. **IDE:** Visual Studio Code (VSCode) with the *Extension Pack for Java* and *Maven for Java*.
4. **Hadoop Winutils (Windows only):** Spark requires Hadoop binary emulators to run on Windows.

## Setup Instructions

**Step 1: Configure Hadoop Winutils (For Windows Users)**

1. Download a distribution containing `winutils.exe` and `hadoop.dll` (e.g., for Hadoop 3.0.0).
2. Extract it to a folder, e.g., `C:\\hadoop`.
3. Set Environment Variables in Windows:
* Create `HADOOP_HOME` pointing to `C:\\hadoop`.
* Add `%HADOOP_HOME%\\bin` to your `Path` variable.


4. Restart VSCode or your computer.

**Step 2: Load Maven Dependencies**
Open the `SparkMovieAnalysis` folder in VSCode. Wait for the "Syncing projects" notification in the bottom right to complete. Maven will automatically download Apache Spark and its dependencies as defined in `pom.xml`.

## How to Run

There are two primary ways to execute the tasks:

### Option 1: Using VSCode UI (Run/Debug)

*Important: If you are using **Java 16 or later (e.g., Java 17)**, Spark will encounter an `IllegalAccessError` due to Java's strong encapsulation.*

To fix this, configure `launch.json`:

1. Go to the **Run and Debug** tab (`Ctrl + Shift + D`).
2. Click **"create a launch.json file"** -> select **Java**.
3. Update `.vscode/launch.json` to include the `--add-opens` flags in `vmArgs`:
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Run Spark Apps",
            "request": "launch",
            "mainClass": "${file}",
            "vmArgs": "--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/sun.nio.cs=ALL-UNNAMED --add-opens=java.base/sun.security.action=ALL-UNNAMED --add-opens=java.base/sun.util.calendar=ALL-UNNAMED --add-opens=java.security.jgss/sun.security.krb5=ALL-UNNAMED"
        }
    ]
}

```

4. Open the specific task file (e.g., `Bai1.java`) and press **F5** or click the **Run** button.

### Option 2: Using the Terminal (Maven)

If the VSCode Java server is unresponsive, use Maven directly:

1. Open the VSCode Terminal (`Ctrl + ~`).
2. Run the following command (replace the class name for different tasks):
```bash
mvn clean compile exec:java -D"exec.mainClass"="com.bigdata.Bai1"

```

## Task Descriptions

* **Task 1:** Processes `movies.txt` and `ratings.txt` to compute the average rating and total review count per movie. Identifies the top-rated movie with at least 5 reviews.
* **Task 2:** Parses the `Genres` field to calculate the average user rating for each individual movie genre.
* **Task 3:** Joins rating data with `users.txt` to compare average ratings between Male and Female users.
* **Task 4:** Categorizes users into age groups (Under 18, 18-35, 36-50, Over 50) and analyzes rating trends for each group per movie.
* **Task 5:** Integrates `occupation.txt` and `users.txt` to determine the average rating provided by different professional groups.
* **Task 6:** Converts `Unix Timestamps` in the rating files to analyze yearly rating counts and averages.
"""