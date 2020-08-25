package java1.storage;

import java1.tasklist.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Storage class can (i) load the data from duke.txt, (ii) save a task onto duke.txt,
 *  (iii) rewrite duke.txt when a task is removed or updated.
 */

public class Storage {
    protected File f;

    /**
     * Creates and initialises a Storage object
     * @param filePath the path of the file to be read
     */
    public Storage(String filePath) {
        this.f = new File(filePath);
    }

    /**
     * Loads the data on duke.txt.
     * @return An ArrayList of tasks from duke.txt.
     * @throws FileNotFoundException if there is no file found to load from.
     */
    public ArrayList<Task> load() throws FileNotFoundException {
        Scanner s = new Scanner(this.f);
        ArrayList<Task> list = new ArrayList<>();

        while (s.hasNext()) {
            String[] taskInfo = s.nextLine().split(",", 4);
            if (taskInfo[0].equals("T")) {
                if (taskInfo[1].equals("1")) {
                    Todos todo = new Todos(taskInfo[2], true);
                    list.add(todo);
                } else {
                    Todos todo = new Todos(taskInfo[2], false);
                    list.add(todo);
                }
            } else if (taskInfo[0].equals("D")) {
                if (taskInfo[1].equals("1")) {
                    Deadlines deadline = new Deadlines(taskInfo[2], taskInfo[3], true);
                    list.add(deadline);
                } else {
                    Deadlines deadline = new Deadlines(taskInfo[2], taskInfo[3], false);
                    list.add(deadline);
                }
            } else {
                if (taskInfo[1].equals("1")) {
                    Events event = new Events(taskInfo[2], taskInfo[3], true);
                    list.add(event);
                } else {
                    Events event = new Events(taskInfo[2], taskInfo[3], false);
                    list.add(event);
                }
            }
        }
        return list;
    }

    /**
     * Saves a task onto duke.txt
     * @param path The file path of duke.txt.
     * @param todoList The current task ArrayList
     * @param task The task to be saved
     */
    public static void saveTask(String path, ArrayList<Task> todoList, Task task) {
        try {
            String s = formatString(task);

            if (todoList.size() == 0) {
                appendToFile(path, s);
            } else {
                appendToFile(path, System.lineSeparator() + s);
            }
        } catch (IOException e) {
            System.out.println("exception");
        }
    }


    /**
     * Rewrites duke.txt with the tasks from the todoList
     * @param path The file path of duke.txt
     * @param todoList The current task ArrayList
     */
    public static void rewriteList(String path, ArrayList<Task> todoList ) {
        try {
            String allTasks = formatString(todoList.get(0));
            for (int i = 1; i < todoList.size(); i++) {
                Task t = todoList.get(i);
                String s = formatString(t);
                allTasks = allTasks + "\n" + s;
            }
            writeToFile(path, allTasks);
        } catch (IOException e) {
            System.out.println("exception");
        }
    }

    private static String formatString(Task task) {
        if (task instanceof Todos) {
            if (task.checkDone()) {
                String s = "T,1," + task.getDescription();
                return s;
            } else {
                String s = "T,0," + task.getDescription();
                return s;
            }
        } else if (task instanceof Deadlines) {
            if (task.checkDone()) {
                String s = "D,1," + task.getDescription() + "," + ((Deadlines) task).getDeadline();
                return s;
            } else {
                String s = "D,0," + task.getDescription() + "," + ((Deadlines) task).getDeadline();
                return s;
            }
        } else {
            if (task.checkDone()) {
                String s = "E,1," + task.getDescription() + "," + ((Events) task).checkAt();
                return s;
            } else {
                String s = "E,0," + task.getDescription() + "," + ((Events) task).checkAt();
                return s;
            }
        }
    }

    private static void appendToFile(String filePath, String textToAppend) throws IOException {
        FileWriter fw = new FileWriter(filePath, true); // create a FileWriter in append mode
        fw.write(textToAppend);
        fw.close();
    }

    private static void writeToFile(String filePath, String textToAdd) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(textToAdd);
        fw.close();
    }
}
