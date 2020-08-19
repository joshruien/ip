package main.java;

public class Deadlines extends Task {
    protected String deadline;

    public Deadlines(String task, String deadline) {
        super(task);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + deadline + ")";
    }

}