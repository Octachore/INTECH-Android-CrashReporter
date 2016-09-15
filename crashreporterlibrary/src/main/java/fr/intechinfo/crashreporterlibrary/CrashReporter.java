package fr.intechinfo.crashreporterlibrary;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CrashReporter {

    private static Application _application;

    private static final String tableName = "CrashReports";
    private static final String col_id = "id";
    private static final String col_message = "message";
    private static final String col_stacktrace = "stacktrace";
    private static final String col_date = "date";

    /**
     * Initializes the CrashReporter with the specified application. Starts to listen for crashes of the application.
     *
     * @param application The application to watch.
     */
    public static void initialize(Application application) {

        _application = application;

        // TODO: check if already registered default handler.

        SQLiteDatabase db = null;
        // Create a database to save the crash reports into
        try {
            db = getDB();
            db.execSQL("CREATE TABLE IF NOT EXISTS CrashReports(id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT, stacktrace TEXT, date TEXT)");
        }
        catch (Exception e){

        }
        finally {
            if(db != null) db.close();
        }

        // Sets an exception handler for the current thread
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                HandleExceptionLogging(e);
            }
        });
    }

    /**
     * Gets the database associated with the currently registered application.
     * @return The database.
     * @throws NullPointerException
     */
    private static SQLiteDatabase getDB() throws NullPointerException {
        Guard.Requires(_application != null, new NullPointerException("The application must not be null."));
        return _application.openOrCreateDatabase("CrashReporter", Context.MODE_PRIVATE, null);
    }

    /**
     * Logs an exception to the database.
     * @param e The exception to log.
     */
    private static void HandleExceptionLogging(Throwable e) {

        Date date = new Date();

        SQLiteDatabase db = getDB();

        String query = String.format(
                "INSERT INTO %s (%s, %S, %s) " +
                        "VALUES ('%s', '%s', '%s')"
                , tableName, col_message, col_stacktrace, col_date, e.getMessage(), Arrays.toString(e.getStackTrace()), date.toString());

        db.execSQL(query);

        sendExceptions("test@example.com");
    }

    /**
     * Gets the logged crash reports from the database.
     * If there is no database, a empty list is returned.
     * @return The reports list.
     */
    public static ArrayList<CrashReport> getExceptions() {

        List<String> dbList = Arrays.asList(_application.databaseList());

        if (!dbList.contains("CrashReporter")) {
            return new ArrayList<>();
        }

        Cursor cursor = null;
        SQLiteDatabase db = null;
        ArrayList<CrashReport> list = new ArrayList<>();

        try {
            db = getDB();
            cursor = db.rawQuery("SELECT id, message, stacktrace, date FROM CrashReports", new String[]{

            });

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String message = cursor.getString(1);
                    String stacktrace = cursor.getString(2);
                    // DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
                    // Date date = format.parse(cursor.getString(3));
                    String date = cursor.getString(3);

                    list.add(new CrashReport(id, message, stacktrace, date));

                } while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            return list;
        }
    }

    public static void sendExceptions(String email) {

        ArrayList<CrashReport> reports = getExceptions();

        File fileDir = _application.getExternalFilesDir(null);
        File file = new File(fileDir, "crash-reports.log");
        PrintWriter writer = null;

        // Write the crash reports to a file
        try {
            writer = new PrintWriter(file);

            for (CrashReport report : reports) {
                writer.write(String.format("%s : %s\n%s\n\n", report.getDate(), report.getMessage(), report.getStacktrace()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }

        // Create email intent with the reports file attached
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, String.format("The app %s crashed.", _application.getPackageName()));
        intent.putExtra(Intent.EXTRA_TEXT, "See reports in attached file.");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        // Send the email
        try {
            Intent chooserIntent = Intent.createChooser(intent, "Send mail...");
            chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _application.startActivity(chooserIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reset() {

        SQLiteDatabase db = null;

        try {
            db = getDB();
            db.execSQL("DELETE * FROM ?", new String[]{tableName});
        }
        finally {
            if(db != null) db.close();
        }
    }
}
