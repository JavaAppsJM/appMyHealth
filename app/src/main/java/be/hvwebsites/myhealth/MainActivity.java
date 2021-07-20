package be.hvwebsites.myhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import be.hvwebsites.myhealth.repositories.MeasurementRepository;

public class MainActivity extends AppCompatActivity {
    private MeasurementRepository repository = new MeasurementRepository();
    public static final String BELLY_FILE = "buikomtrek.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView latestMeasurementsView = findViewById(R.id.resumeinfo);

        // Ophalen belly measurements
        String baseDir = getBaseContext().getExternalFilesDir(null).getAbsolutePath();
        File bellyFile = new File(baseDir, BELLY_FILE);
        int rc = repository.initializeRepository(bellyFile).getReturnCode();
        if ( rc == 0){
            latestMeasurementsView.setText("laatste meting: " + repository.getLatestMeting());
        } else if (rc == 100){
            latestMeasurementsView.setText("er zijn nog geen buikomtrek metingen !");
        } else {
            latestMeasurementsView.setText("er is een probleem opgetreden bij het ophalen vd metingen !");
        }

/*
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
*/
    }

    // Als op knop Buikomtrek wordt gedrukt
    public void startBellyAct(View view) {
        // Ongeacht of er bellies zijn, ga naar BellyActivity. Indien er bellies zijn worden ze getoond anders wordt er naar NewBellyActivity gegaan
        Intent intent = new Intent(this, BellyActivity.class);
        startActivity(intent);
    }
}