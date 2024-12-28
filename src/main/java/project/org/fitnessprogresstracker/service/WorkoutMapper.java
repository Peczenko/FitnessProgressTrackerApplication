package project.org.fitnessprogresstracker.service;

import project.org.fitnessprogresstracker.dto.ExerciseDto;
import project.org.fitnessprogresstracker.dto.SetDto;
import project.org.fitnessprogresstracker.dto.WorkoutDto;
import project.org.fitnessprogresstracker.entities.Exercise;
import project.org.fitnessprogresstracker.entities.Set;
import project.org.fitnessprogresstracker.entities.User;
import project.org.fitnessprogresstracker.entities.Workout;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WorkoutMapper {

    public static Workout convertToEntity(WorkoutDto dto, User user) {
        Workout workout = Workout.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : new Date())
                .user(user)
                .build();

        List<Exercise> exercises = dto.getExercises().stream()
                .map(exerciseDto -> convertExerciseToEntity(exerciseDto, workout))
                .collect(Collectors.toList());

        workout.setExercises(exercises);
        return workout;
    }

    public static WorkoutDto convertWorkoutToDto(Workout workout) {
        List<ExerciseDto> exercises = workout.getExercises().stream()
                .map(WorkoutMapper::convertExerciseToDto)
                .collect(Collectors.toList());


        return WorkoutDto.builder()
                .id(workout.getId())
                .name(workout.getName())
                .description(workout.getDescription())
                .duration(workout.getDuration())
                .createdAt(workout.getCreatedAt())
                .exercises(exercises)
                .build();
    }

    public static Exercise convertExerciseToEntity(ExerciseDto dto, Workout workout) {
        Exercise exercise = Exercise.builder()
                .id(dto.getId())
                .exerciseType(dto.getExerciseType())
                .customDescription(dto.getCustomDescription())
                .workout(workout)
                .build();

        List<Set> sets = dto.getSets().stream()
                .map(setDto -> convertSetToEntity(setDto, exercise))
                .collect(Collectors.toList());

        exercise.setSets(sets);
        return exercise;
    }

    public static ExerciseDto convertExerciseToDto(Exercise exercise) {
        List<SetDto> sets = exercise.getSets().stream()
                .map(WorkoutMapper::convertSetToDto)
                .collect(Collectors.toList());

        return ExerciseDto.builder()
                .id(exercise.getId())
                .exerciseType(exercise.getExerciseType())
                .customDescription(exercise.getCustomDescription())
                .sets(sets)
                .build();
    }

    public static Set convertSetToEntity(SetDto dto, Exercise exercise) {
        return Set.builder()
                .id(dto.getId())
                .reps(dto.getReps())
                .weight(dto.getWeight())
                .exercise(exercise)
                .build();
    }

    public static SetDto convertSetToDto(Set set) {
        return SetDto.builder()
                .id(set.getId())
                .reps(set.getReps())
                .weight(set.getWeight())
                .build();
    }

    public static Exercise updateExerciseFromDto(ExerciseDto exerciseDto, Workout workout, List<Exercise> existingExercises) {
        if (exerciseDto.getId() == null) {
            return convertExerciseToEntity(exerciseDto, workout);
        } else {
            Exercise existingExercise = existingExercises.stream()
                    .filter(e -> e.getId().equals(exerciseDto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Exercise not found"));

            existingExercise.setExerciseType(exerciseDto.getExerciseType());
            existingExercise.setCustomDescription(exerciseDto.getCustomDescription());

            List<Set> updatedSets = exerciseDto.getSets().stream()
                    .map(setDto -> updateSetFromDto(setDto, existingExercise, existingExercise.getSets()))
                    .collect(Collectors.toList());

            existingExercise.getSets().clear();
            existingExercise.getSets().addAll(updatedSets);

            return existingExercise;
        }
    }

    public static Set updateSetFromDto(SetDto setDto, Exercise exercise, List<Set> existingSets) {
        if (setDto.getId() == null) {
            // New Set
            return convertSetToEntity(setDto, exercise);
        } else {
            Set existingSet = existingSets.stream()
                    .filter(s -> s.getId().equals(setDto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Set not found"));

            existingSet.setReps(setDto.getReps());
            existingSet.setWeight(setDto.getWeight());
            return existingSet;
        }
    }



}

