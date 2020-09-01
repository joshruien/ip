package parser;

import tasklist.*;
import exception.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The Parser class is responsible for handling input commands.
 */
public class Parser {

    /**
     * Handles commands regarding tasks(ie. todos, deadlines, events).
     * @param input The command input.
     * @param newList The current TaskList.
     * @throws DukeException if there is an error with the input or input is not given in the correct format.
     */
    public static String commandTask(String input, TaskList newList) throws DukeException {

        String[] splitString = input.split(" ",2);

        if (splitString[0].equals("todo")) {
            if (splitString.length == 1) {
                throw new DukeException("☹ OOPS!!! The description of a todo cannot be empty.");
            } else {
                String info = splitString[1];
                Todo newTodo = new Todo(info);
                return newList.addTask(newTodo);
            }

        } else if (splitString[0].equals("deadline")) {
            if (splitString.length == 1) {
                throw new DukeException("☹ OOPS!!! The description of a deadline cannot be empty.");
            } else {
                String info = splitString[1];
                String[] information = info.split(" /by ");
                if (information.length == 1) {
                    throw new DukeException("☹ OOPS!!! Please specify the deadline time");
                } else {
                    String description = information[0];
                    String by = information[1];

                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        LocalDateTime dateTime = LocalDateTime.parse(by, formatter);

                        Deadline newDeadline = new Deadline(description, dateTime);

                        return newList.addTask(newDeadline);
                    } catch (DateTimeException ex) {
                        return "Please specify the date and time in this format dd/MM/yyy HH:mm";
                    }
                }
            }

        } else if (splitString[0].equals("event")) {
            if (splitString.length == 1) {
                throw new DukeException("☹ OOPS!!! The description of an event cannot be empty.");
            } else {
                String info = splitString[1];
                String[] information = info.split(" /at ");

                if (information.length == 1) {
                    throw new DukeException("☹ OOPS!!! Please state the event time");
                } else {
                    String description = information[0];
                    String at = information[1];
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        LocalDateTime dateTime = LocalDateTime.parse(at, formatter);

                        Event newEvent = new Event(description, dateTime);

                        return newList.addTask(newEvent);
                    } catch (DateTimeException ex) {
                        return "Please specify the date and time in this format dd/MM/yyy HH:mm";
                    }
                }
            }

        } else {
            throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Handles all non-task related commands(commands that do not begin with a task type).
     * @param input The input command.
     * @param newList The current TaskList.
     * @throws DukeException if there is an error with the input or input is not given in the correct format.
     */
    public static String commandParser(String input, TaskList newList) throws DukeException {
        String[] splitInput = input.split(" ");

        if (splitInput[0].equals("list")) {
            return newList.listTasks();

        } else if (splitInput[0].equals("done")) {
            if (splitInput.length == 1) {
                throw new DukeException("☹ OOPS!!! Please specify which task is done");
            } else {
                String taskNumberString = splitInput[1];
                try {
                    int taskNumberInt = Integer.parseInt(taskNumberString) - 1;

                    if (taskNumberInt + 1 > newList.getLength()) {
                        throw new DukeException("☹ OOPS!!! Your task number is out of bounds");
                    } else {
                        Task t = newList.get(taskNumberInt);
                        String text = t.markDone();
                        newList.updateDone();
                        return text;
                    }
                } catch (NumberFormatException e) {
                    throw new DukeException("☹ OOPS!!! Invalid task number.");

                }
            }
        } else if (splitInput[0].equals("delete")) {
            if (splitInput.length == 1) {
                throw new DukeException("☹ OOPS!!! Please specify which task you want to delete");
            } else {
                String taskNumberString = splitInput[1];
                try {
                    int taskNumberInt = Integer.parseInt(taskNumberString) - 1;

                    if (taskNumberInt + 1 > newList.getLength()) {
                        throw new DukeException("☹ OOPS!!! Your task number is out of bounds");
                    } else {
                        return newList.removeTask(taskNumberInt);
                    }
                } catch (NumberFormatException e) {
                    throw new DukeException("☹ OOPS!!! Invalid task number.");
                }
            }
        } else if (splitInput[0].equals("find")) {
            if (splitInput.length == 1) {
                throw new DukeException("☹ OOPS!!! Please specify the keyword to find");
            } else {
                String[] findArray = input.split(" ", 2);
                String keywords = findArray[1];
                ArrayList<Task> temp = newList.findTasks(keywords);
                String text = "Here are the tasks in your list:";
                //System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < temp.size(); i++) {
                    int listNumber = i + 1;
                    Task currentTask = temp.get(i);

                    text += "\n" + "     " + listNumber + "." + currentTask.toString();
//                    System.out.println("     " + listNumber + "." + currentTask.toString());
                }
                return text;
            }
        } else {
            try {
                return commandTask(input,newList);
            } catch (DukeException e) {
                return e.getMessage();
            }
        }
    }
}
