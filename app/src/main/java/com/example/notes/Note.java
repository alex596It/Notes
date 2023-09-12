package com.example.notes;

public class Note {
    private String title;
    private String description;
    private int priority;
    private String dayOfWeek;

    public Note(String title, String description, int priority, String dayOfWeek) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dayOfWeek = dayOfWeek;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
}
