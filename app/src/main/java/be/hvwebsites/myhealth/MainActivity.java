package be.hvwebsites.myhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static final int INTENT_REQUEST_CODE = 1;
    public static final String BELLY_FILE = "buikomtrek.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView latestMeasurementsView = findViewById(R.id.resumeinfo);

        // Ophalen belly measurements
        String baseDir = getBaseContext().getExternalFilesDir(null).getAbsolutePath();
        File bellyFile = new File(baseDir, BELLY_FILE);
        String[] fileLines = new String[1000];
        if (bellyFile.exists()) {
            try {
                Scanner inFile = new Scanner(bellyFile);
                int i = 0;
                if (inFile.hasNext()) {
                    fileLines[i] = inFile.nextLine();
                }
                inFile.close();
                latestMeasurementsView.setText("laatste meting: " + fileLines[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            latestMeasurementsView.setText("er zijn nog geen buikomtrek metingen !");
        }
    }

    // Als op knop Buikomtrek wordt gedrukt
    public void startBellyAct(View view) {
        // Ongeacht of er bellies zijn, ga naar BellyActivity. Indien er bellies zijn worden ze getoond anders wordt er naar NewBellyActivity gegaan
        Intent intent = new Intent(this, BellyActivity.class);
        startActivity(intent);
    }
}