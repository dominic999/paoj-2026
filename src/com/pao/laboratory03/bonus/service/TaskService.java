package com.pao.laboratory03.bonus.service;

import com.pao.laboratory03.bonus.exception.DuplicateTaskException;
import com.pao.laboratory03.bonus.exception.InvalidTransitionException;
import com.pao.laboratory03.bonus.exception.TaskNotFoundException;
import com.pao.laboratory03.bonus.model.Priority;
import com.pao.laboratory03.bonus.model.Status;
import com.pao.laboratory03.bonus.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskService {
    private static TaskService instance;

    private final Map<String, Task> tasksById;
    private final Map<Priority, List<Task>> tasksByPriority;
    private final List<String> auditLog;
    private int nextId;

    private TaskService() {
        tasksById = new HashMap<>();
        tasksByPriority = new HashMap<>();
        auditLog = new ArrayList<>();
        nextId = 1;

        for (Priority priority : Priority.values()) {
            tasksByPriority.put(priority, new ArrayList<>());
        }
    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", nextId++);
        Task task = new Task(id, title, priority);
        return addTask(task);
    }

    public Task addTask(Task task) {
        if (tasksById.containsKey(task.getId())) {
            throw new DuplicateTaskException("Task-ul '" + task.getId() + "' există deja");
        }

        tasksById.put(task.getId(), task);
        tasksByPriority.get(task.getPriority()).add(task);
        auditLog.add("[ADD] " + task.getId() + ": '" + task.getTitle() + "' (" + task.getPriority() + ")");
        return task;
    }

    public Task findById(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");
        }
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = findById(taskId);
        task.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " → " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = findById(taskId);
        Status oldStatus = task.getStatus();

        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }

        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + oldStatus + " → " + newStatus);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return new ArrayList<>(tasksByPriority.get(priority));
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new HashMap<>();

        for (Status status : Status.values()) {
            summary.put(status, 0L);
        }

        for (Task task : tasksById.values()) {
            Status status = task.getStatus();
            summary.put(status, summary.get(status) + 1);
        }

        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> result = new ArrayList<>();

        for (Task task : tasksById.values()) {
            if (task.getAssignee() == null) {
                result.add(task);
            }
        }

        return result;
    }

    public void printAuditLog() {
        if (auditLog.isEmpty()) {
            System.out.println("Audit log gol.");
            return;
        }

        for (String entry : auditLog) {
            System.out.println(entry);
        }
    }

    public double getTotalUrgencyScore(int baseDays) {
        double total = 0;

        for (Task task : tasksById.values()) {
            if (task.getStatus() != Status.DONE && task.getStatus() != Status.CANCELLED) {
                total += task.getPriority().calculateScore(baseDays);
            }
        }

        return total;
    }
}
