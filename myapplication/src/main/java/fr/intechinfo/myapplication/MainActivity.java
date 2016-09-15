package fr.intechinfo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.intechinfo.crashreporterlibrary.CrashReporter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register app in crash reporter
        CrashReporter.initialize(this.getApplication());

        // Set button on-click event
        Button button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int i = 1/0;
            }
        });
    }
}
