package fr.intechinfo.crashreporter;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import  android.database.sqlite.SQLiteDatabase;
import android.os.Looper;

/**
 * Created by Nicolas on 14/09/2016.
 */
public class CrashReporter {

    private static Application _application;
    private static SQLiteDatabase _db;

    private final String tableName = "id";
    private final String col_id = "id";
    private final String col_message = "message";
    private final String col_stacktrace = "stacktrace";
    private final String col_date = "date";

    /**
     * Initializes the CrashReporter with the specified application. Starts to listen for crashes of the application.
     * @param application The application to watch.
     */
    public static void initialize(Application application) {

        _application = application;

        // TODO: check if already registered default handler.

        // Create a database to save the crash reports into
        _db = _application.openOrCreateDatabase("CrashReporter", Context.MODE_PRIVATE, null);
        _db.execSQL("CREATE table CrashReports(id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT, stacktrace TEXT, date TEXT)");

        // Sets an exception handler for the current thread
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                LogException(e);
            }
        });
    }

    private static void LogException(Throwable e) {

        Date date = new Date();

        _db.execSQL(String.format(
            "INSERT INTO {0} ({1}, {2}, {3}) " +
            "VALUES ({5}, {6}, {7})"
        ), e.getMessage(), e.getStackTrace(), date.toString());

        throw new Error("Not implemented");
    }

    public static ArrayList<Throwable> getExceptions() {
        throw new Error("Not implemented");
    }

    public static void sendExceptions(String email) {
        throw new Error("Not implemented");
    }

    public static void reset() {
        throw new Error("Not implemented");
    }
}
