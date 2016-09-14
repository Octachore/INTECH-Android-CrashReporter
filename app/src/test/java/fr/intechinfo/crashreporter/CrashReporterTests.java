package fr.intechinfo.crashreporter;

import android.app.Application;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Nicolas on 14/09/2016.
 */
public class CrashReporterTests {
    @Test
    public void initialize_should_create_database(){
        // Arrange
        Application app = new Application();
        int initialDatabasesCount = app.databaseList().length;

        // Act
        CrashReporter.initialize(app);

        // Assert
        assertEquals(app.databaseList().length, initialDatabasesCount + 1);
    }
}
