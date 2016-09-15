package fr.intechinfo.crashreporterlibrary;

import java.util.Date;

/**
 * Represents crash report.
 */
public class CrashReport {
    private final int id;
    private final String message;
    private final String stacktrace;
    private final String date;

    public CrashReport(int id, String message, String stacktrace, String date){

        this.id = id;
        this.message = message;
        this.stacktrace = stacktrace;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public String getDate() {
        return date;
    }

    public String render() {
        return String.format("%s : %s\n\n%s\n\n\n", date, message, stacktrace);
    }
}
