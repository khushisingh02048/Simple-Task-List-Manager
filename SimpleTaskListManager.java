import java.io.*;
import java.util.*;

class Task {
    String id;
    String title;
    String priority;
    String status;

    Task(String id, String title, String priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = "Pending";
    }

    public String toString() {
        return id + " | " + title + " | " + priority + " | " + status;
    }
}

public class SimpleTaskListManager {

    static LinkedList<Task> tasks = new LinkedList<>();
    static Scanner sc = new Scanner(System.in);

    static int taskNumber = 101;
    static final String FILE_NAME = "tasks.txt";

    // Add a new task
    static void addTask() {
        System.out.print("Enter task title: ");
        String title = sc.nextLine();

        System.out.print("Enter priority (High/Medium/Low): ");
        String priority = sc.nextLine();

        if (!isValidPriority(priority)) {
            System.out.println("Invalid priority. Choose High, Medium or Low.");
            return;
        }

        priority = formatPriority(priority);

        String id = "TK" + taskNumber++;
        Task task = new Task(id, title, priority);

        tasks.add(task);

        System.out.println("Task added. ID: " + id);
        System.out.println("Task: " + task);
    }

    // Display all tasks
    static void displayAll() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }

        System.out.println("\n----- ALL TASKS -----");

        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    // Sort tasks according to priority
    static void sortByPriority() {
        tasks.sort((t1, t2) ->
                priorityValue(t1.priority) - priorityValue(t2.priority));

        System.out.println("\nTasks sorted by priority:");
        displayAll();
    }

    // Return priority value for sorting
    static int priorityValue(String priority) {
        if (priority.equalsIgnoreCase("High"))
            return 1;
        else if (priority.equalsIgnoreCase("Medium"))
            return 2;
        else
            return 3;
    }

    // Mark task as completed
    static void markDone() {
        System.out.print("Enter task ID: ");
        String id = sc.nextLine();

        for (Task task : tasks) {
            if (task.id.equalsIgnoreCase(id)) {
                task.status = "Done";
                System.out.println("Task " + id + " marked Done.");
                return;
            }
        }

        System.out.println("Task not found.");
    }

    // Delete a task
    static void deleteTask() {
        System.out.print("Enter task ID: ");
        String id = sc.nextLine();

        Iterator<Task> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            Task task = iterator.next();

            if (task.id.equalsIgnoreCase(id)) {
                iterator.remove();
                System.out.println("Task " + id + " deleted.");
                return;
            }
        }

        System.out.println("Task not found.");
    }

    // Validate priority
    static boolean isValidPriority(String priority) {
        return priority.equalsIgnoreCase("High")
                || priority.equalsIgnoreCase("Medium")
                || priority.equalsIgnoreCase("Low");
    }

    // Format priority
    static String formatPriority(String priority) {
        if (priority.equalsIgnoreCase("High"))
            return "High";
        else if (priority.equalsIgnoreCase("Medium"))
            return "Medium";
        else
            return "Low";
    }

    // Save tasks to file
    static void saveToFile() {
        try {
            FileWriter fw = new FileWriter(FILE_NAME);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Task task : tasks) {
                bw.write(task.id + "|" +
                         task.title + "|" +
                         task.priority + "|" +
                         task.status);
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            System.out.println("Error while saving tasks.");
        }
    }

    // Load tasks from file
    static void loadFromFile() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|");

                if (data.length == 4) {
                    Task task = new Task(
                            data[0],
                            data[1],
                            data[2]
                    );

                    task.status = data[3];

                    tasks.add(task);

                    // Update task number
                    String numberPart =
                            data[0].substring(2);

                    int number = Integer.parseInt(numberPart);

                    if (number >= taskNumber) {
                        taskNumber = number + 1;
                    }
                }
            }

            br.close();

        } catch (Exception e) {
            System.out.println("Error while loading tasks.");
        }
    }

    // Display menu
    static void showMenu() {
        System.out.println("\n===== SIMPLE TASK LIST MANAGER =====");
        System.out.println("1. Add Task");
        System.out.println("2. Display All");
        System.out.println("3. Sort by Priority");
        System.out.println("4. Mark Done");
        System.out.println("5. Delete Task");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    public static void main(String[] args) {

        loadFromFile();

        while (true) {

            showMenu();

            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    addTask();
                    saveToFile();
                    break;

                case "2":
                    displayAll();
                    break;

                case "3":
                    sortByPriority();
                    saveToFile();
                    break;

                case "4":
                    markDone();
                    saveToFile();
                    break;

                case "5":
                    deleteTask();
                    saveToFile();
                    break;

                case "6":
                    saveToFile();
                    System.out.println("Tasks saved. Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}