# Fitness Progress Tracker Application

The Fitness Progress Tracker Application is a comprehensive solution designed to help users monitor and enhance their fitness journey. This repository contains the backend services, while the frontend interface is available [here](https://github.com/Peczenko/FitnessProgressFrontend).

## Features

- **User Authentication:** Secure user registration and login functionalities.
- **Workout Logging:** Record and track various workouts and exercises.
- **Progress Visualization:** Graphical representation of fitness progress over time.
- **Goal Setting:** Define and monitor personal fitness goals.
- **Data Analysis:** Insights and statistics based on logged workouts.

## Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/Peczenko/FitnessProgressTrackerApplication.git
   ```
2. **Navigate to the Project Directory:**
    ```bash
    cd FitnessProgressTrackerApplication
    ```
3. **Build the Project:**
    ```bash
    ./gradlew build
    ```
4. **Run the Application:**
    ```bash
    ./gradlew bootRun
    ```
## API Endpoints


| Method | URL                                       | Description                             |
|--------|-------------------------------------------|-----------------------------------------|
| GET    | `/workouts/{id}`                          | Retrieve a specific workout by ID       |
| PUT    | `/workouts/{id}`                          | Update a workout by ID                  |
| DELETE | `/workouts/{id}`                          | Delete a workout by ID                  |
| POST   | `/workouts/add`                           | Create a new workout                    |
| GET    | `/workouts`                               | Retrieve all workouts                   |
| GET    | `/workouts/get/custom-type`               | Get all custom exercise types           |
| GET    | `/goals/{id}`                             | Retrieve a specific goal by ID          |
| PUT    | `/goals/{id}`                             | Update a goal by ID                     |
| DELETE | `/goals/{id}`                             | Delete a goal by ID                     |
| POST   | `/goals/add`                              | Create a new goal                       |
| GET    | `/goals`                                  | Retrieve all goals                      |
| GET    | `/goals/exercise-type/{exerciseType}`     | Retrieve goal by exercise type          |
| GET    | `/goals/custom-type`                      | Get all custom goal descriptions        |
| GET    | `/goals/custom-type/{customDescription}`  | Get goal by custom description          |
| POST   | `/auth/register`                          | Register a new user                     |
| POST   | `/auth/login`                             | Authenticate and return token           |
| POST   | `/auth/logout`                            | Logout user                             |
| POST   | `/auth/refresh_token`                     | Refresh JWT token                       |
| GET    | `/auth/me`                                | Get user profile info                   |
| POST   | `/auth/me`                                | Set user profile info                   |
| GET    | `/progress/goal/{goalId}`                 | Get progress data for a goal            |
| GET    | `/progress/goal/{goalId}/timeline`        | Get timeline data for goal progress     |
| GET    | `/progress/common`                        | Get common progress data                |





