# CrashReporter

Members of this project:

[Nicolas Maurice](https://github.com/Octachore)

[Thomas Kerbrat](https://github.com/ThomasKerbrat)

## Architecture choices

The library is situated inside the `/crashreporterlibrary` top-level directory.
It is comprised of two main classes, `CrashReport` and `CrashReporter`.

A testing application is created inside the `/myapplication` top-level directory.
The demo application uses our library to initialize the reporter.
When launched, it displays a button that will perform a division by zero, thus throwing a run time Exception.
Just for this demo, we left a `sendExceptions` call inside the Thread Default Uncaught Exception Handler.
So when you click on the "Crash!" button, the application will crash and prompt you with the Email App Chooser.

### CrashReport

This class represent a single crash report inside the system.
An instance is created for each crash encountered.

### CrashReporter

This class exposes static methods to interact with the reporter.
Those methods are `initialize`, `getExceptions`, `sendExceptions`, and `reset`.
Each one does what the requirements expect them to do (based on our own manual testing).

During the initialization phase, the reporter creates a `CrashReports` table in a SQLite database with the following schema:

- id             INTEGER PRIMARY KEY AUTOINCREMENT
- message        TEXT
- stack trace    TEXT
- date           TEXT

It also registers a handler on the current `Thread` with `setDefaultUncaughtExceptionHandler`.
The handler inserts a new record in the application database.

When the developer calls `getExceptions`, an `ArrayList` of `CrashReport` instances is returned.
The reporter creates those instances after it has read the database for all records.

## Encountered difficulties

SQLite was not verbose on its errors.
It lead to a lot of time loss trying to re-write SQL statements.

Formatting dates with the `Date` and `DateFormat` classes was not easy to grasp as C# developers.

`String.format` that does not take `{0}` handlebars patterns but `%s %d` patterns.

## Advancement

We did not implement a `ContentProvider` to expose the database content.
We read the docs and we grasp the main concepts, but we got caught by time.
