package project.org.fitnessprogresstracker.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.org.fitnessprogresstracker.entities.Exercise;
import project.org.fitnessprogresstracker.entities.Goal;
import project.org.fitnessprogresstracker.entities.Set;
import project.org.fitnessprogresstracker.entities.Workout;
import project.org.fitnessprogresstracker.repository.GoalRepository;
import project.org.fitnessprogresstracker.repository.WorkoutRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProgressService {

    private final WorkoutRepository workoutRepository;
    private final GoalRepository goalRepository;

    /**
     * Calculates progress for a specific Goal by filtering Exercises that match the Goal's
     * exerciseType or customDescription AND occurred after the Goal's creation date.
     * Returns a map of statistics
     */
    public Map<String, Object> calculateProgressForGoal(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + goalId));
        String username = goal.getUser().getUsername();
        Date goalCreatedAt = goal.getCreatedAt();

        List<Workout> allWorkouts = workoutRepository.findAllByUserUsername(username);

        List<Exercise> matchingExercises = allWorkouts.stream()
                .filter(workout -> workout.getCreatedAt().after(goalCreatedAt))
                .flatMap(workout -> workout.getExercises().stream())
                .filter(exercise -> matchesGoal(exercise, goal))
                .collect(Collectors.toList());

        Map<String, Object> stats = calculateStatistics(matchingExercises);

        Double targetWeight = goal.getTargetWeight();
        Integer targetReps = goal.getTargetReps();

        double weightProgress = 0.0;
        double repsProgress = 0.0;

        if (targetWeight != null && targetWeight > 0) {
            Double maxWeightLifted = (Double) stats.getOrDefault("maxWeightLifted", 0.0);
            weightProgress = Math.min((maxWeightLifted / targetWeight) * 100, 100);
            stats.put("weightGoalProgressPct", weightProgress);
        } else {
            stats.put("weightGoalProgressPct", 0.0);
        }

        if (targetReps != null && targetReps > 0) {
            Integer bestSetReps = (Integer) stats.getOrDefault("maxRepsInSingleSet", 0);
            repsProgress = Math.min((bestSetReps.doubleValue() / targetReps) * 100, 100);
            stats.put("repsGoalProgressPct", repsProgress);
        } else {
            stats.put("repsGoalProgressPct", 0.0);
        }

        if (goal.getDeadline() != null) {
            long daysToDeadline = ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    goal.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            );
            stats.put("daysToDeadline", daysToDeadline >= 0 ? daysToDeadline : 0);
            stats.put("deadlinePassed", daysToDeadline < 0);
        }

        double combinedProgress = (0.7 * weightProgress) + (0.3 * repsProgress);
        stats.put("combinedGoalProgressPct", combinedProgress);

        return stats;
    }



    /**
     * Provides an overview of the user's progress across all Exercises, grouped by
     * either ExerciseType or customDescription, then calculates advanced statistics for each group.
     */
    public Map<String, Map<String, Object>> calculateCommonProgress(String username) {
        List<Workout> workouts = workoutRepository.findAllByUserUsername(username);

        Map<String, List<Exercise>> groupedExercises = workouts.stream()
                .flatMap(workout -> workout.getExercises().stream())
                .collect(Collectors.groupingBy(exercise -> {
                    if (exercise.getExerciseType() != null) {
                        return exercise.getExerciseType().name();
                    } else {
                        return exercise.getCustomDescription() != null
                                ? exercise.getCustomDescription()
                                : "UNSPECIFIED";
                    }
                }));

        Map<String, Map<String, Object>> progressSummary = new HashMap<>();

        groupedExercises.forEach((key, exercises) -> {
            Map<String, Object> stats = calculateStatistics(exercises);
            progressSummary.put(key, stats);
        });

        return progressSummary;
    }


    /**
     * A helper function that calculates a range of advanced workout stats from a list of Exercises:
     * - totalVolume: sum of (weight * reps) across all sets
     * - totalReps: sum of all reps
     * - totalSets: total sets performed
     * - maxWeightLifted: heaviest weight found in any set
     * - maxRepsInSingleSet: the largest number of reps found in a single set
     * - averageWeightPerSet: totalVolume / totalSets
     * - e1RM (Epley 1RM) for each set, and the best (highest) 1RM across all sets
     * - distinctWorkoutCount: how many distinct workouts contributed to these exercises
     */
    private Map<String, Object> calculateStatistics(List<Exercise> exercises) {
        Map<String, Object> stats = new HashMap<>();

        if (exercises == null || exercises.isEmpty()) {
            stats.put("totalVolume", 0.0);
            stats.put("totalReps", 0);
            stats.put("totalSets", 0);
            stats.put("maxWeightLifted", 0.0);
            stats.put("maxRepsInSingleSet", 0);
            stats.put("averageWeightPerSet", 0.0);
            stats.put("bestE1RM", 0.0);
            stats.put("distinctWorkoutCount", 0);
            return stats;
        }

        // Flatten all sets from all exercises
        List<Set> allSets = exercises.stream()
                .filter(ex -> ex.getSets() != null) // safety check
                .flatMap(ex -> ex.getSets().stream())
                .collect(Collectors.toList());

        double totalVolume = 0.0;
        int totalReps = 0;
        double maxWeightLifted = 0.0;
        int maxRepsInSingleSet = 0;
        double bestE1RM = 0.0;

        for (Set s : allSets) {
            double setVolume = s.getReps() * s.getWeight();
            totalVolume += setVolume;
            totalReps += s.getReps();
            maxWeightLifted = Math.max(maxWeightLifted, s.getWeight());
            maxRepsInSingleSet = Math.max(maxRepsInSingleSet, s.getReps());

            double e1RM = s.getWeight() * (1 + (s.getReps() / 30.0));
            bestE1RM = Math.max(bestE1RM, e1RM);
        }

        int totalSets = allSets.size();
        double averageWeightPerSet = totalSets > 0 ? totalVolume / totalSets : 0.0;

        java.util.Set<Long> distinctWorkoutIds = exercises.stream()
                .map(ex -> ex.getWorkout().getId())
                .collect(Collectors.toSet());
        int distinctWorkoutCount = distinctWorkoutIds.size();

        stats.put("totalVolume", totalVolume);
        stats.put("totalReps", totalReps);
        stats.put("totalSets", totalSets);
        stats.put("maxWeightLifted", maxWeightLifted);
        stats.put("maxRepsInSingleSet", maxRepsInSingleSet);
        stats.put("averageWeightPerSet", averageWeightPerSet);
        stats.put("bestE1RM", bestE1RM);
        stats.put("distinctWorkoutCount", distinctWorkoutCount);

        return stats;
    }


    /**
     * Utility function to decide if a given Exercise matches the Goal's type or customDescription.
     */
    private boolean matchesGoal(Exercise exercise, Goal goal) {
        if (goal.getExerciseType() != null) {
            return goal.getExerciseType() == exercise.getExerciseType();
        }
        if (goal.getCustomDescription() != null && exercise.getCustomDescription() != null) {
            return goal.getCustomDescription().equalsIgnoreCase(exercise.getCustomDescription());
        }
        return false;
    }

    public Map<LocalDate, Map<String, Object>> calculateGoalProgressOverTime(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + goalId));

        String username = goal.getUser().getUsername();
        Date goalCreatedAt = goal.getCreatedAt();

        List<Workout> allWorkouts = workoutRepository.findAllByUserUsername(username);

        List<Exercise> matchingExercises = allWorkouts.stream()
                .filter(workout -> workout.getCreatedAt().after(goalCreatedAt))
                .flatMap(workout -> workout.getExercises().stream())
                .filter(exercise -> matchesGoal(exercise, goal))
                .collect(Collectors.toList());

        Map<LocalDate, List<Exercise>> exercisesByDate = matchingExercises.stream()
                .collect(Collectors.groupingBy(exercise -> exercise.getWorkout()
                        .getCreatedAt().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()));

        Map<LocalDate, Map<String, Object>> progressTimeline = new TreeMap<>();

        exercisesByDate.forEach((date, exercises) -> {
            Map<String, Object> stats = calculateStatistics(exercises);
            progressTimeline.put(date, stats);
        });

        return progressTimeline;
    }
}

