package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private static StudentService instance;

    private final List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul '" + name + "' există deja.");
            }
        }

        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }

        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost găsit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu există studenți în sistem.");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.println((i + 1) + ". " + student);

            if (student.getGrades().isEmpty()) {
                System.out.println("   Fără note.");
            } else {
                for (Map.Entry<Subject, Double> entry : student.getGrades().entrySet()) {
                    System.out.println("   " + entry.getKey().name() + " = " + entry.getValue());
                }
            }
        }
    }

    public void printTopStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu există studenți în sistem.");
            return;
        }

        List<Student> sortedStudents = new ArrayList<>(students);

        for (int i = 0; i < sortedStudents.size() - 1; i++) {
            for (int j = i + 1; j < sortedStudents.size(); j++) {
                if (sortedStudents.get(j).getAverage() > sortedStudents.get(i).getAverage()) {
                    Student temp = sortedStudents.get(i);
                    sortedStudents.set(i, sortedStudents.get(j));
                    sortedStudents.set(j, temp);
                }
            }
        }

        System.out.println("=== Top studenți ===");
        for (int i = 0; i < sortedStudents.size(); i++) {
            Student student = sortedStudents.get(i);
            System.out.printf("%d. %s - media: %.2f%n", i + 1, student.getName(), student.getAverage());
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sums = new HashMap<>();
        Map<Subject, Integer> counts = new HashMap<>();
        Map<Subject, Double> averages = new HashMap<>();

        for (Student student : students) {
            for (Map.Entry<Subject, Double> entry : student.getGrades().entrySet()) {
                Subject subject = entry.getKey();
                double grade = entry.getValue();

                sums.put(subject, sums.getOrDefault(subject, 0.0) + grade);
                counts.put(subject, counts.getOrDefault(subject, 0) + 1);
            }
        }

        for (Subject subject : sums.keySet()) {
            double sum = sums.get(subject);
            int count = counts.get(subject);
            averages.put(subject, sum / count);
        }

        return averages;
    }
}
