package be.hvwebsites.myhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.repositories.MeasurementRepository;
import be.hvwebsites.myhealth.returninfo.ReturnInfo;
import be.hvwebsites.myhealth.viewmodels.MeasurementViewModel;

public class MainActivity extends AppCompatActivity {
    private MeasurementViewModel measurementViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView latestMeasurementsView = findViewById(R.id.resumeinfo);

        // Basis directory definitie
        String baseDir = getBaseContext().getExternalFilesDir(null).getAbsolutePath();

        // latest measurements definities
        Measurement belly = null;
        Measurement upperP = null;
        Measurement lowerP = null;
        Measurement heartbeat = null;

        measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);
        // Initialize viewmodel
        ReturnInfo viewModelStatus = measurementViewModel.initializeMViewModel(baseDir);
        if (viewModelStatus.getReturnCode() == 0) {
            // Files gelezen
            belly = measurementViewModel.getLatestBelly();
            upperP = measurementViewModel.getLatestUpperP();
            lowerP = measurementViewModel.getLatestLowerP();
            heartbeat = measurementViewModel.getLatestHeartbeat();
        } else if (viewModelStatus.getReturnCode() == 100) {
            Toast.makeText(MainActivity.this,
                    viewModelStatus.getReturnMessage(),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this,
                    "Loading Belly Measurements failed",
                    Toast.LENGTH_LONG).show();
        }
        String latestMText;
        if (belly != null){
            latestMText = belly.getDateAndValue();
        }else {
            latestMText = "Empty";
        }
        if (upperP != null && belly == null){
            latestMText = latestMText.concat(upperP.getDateAndValue());
        }else if (upperP != null){
            latestMText = latestMText.concat(" ; ").concat(upperP.getValueAsString());
            latestMText = latestMText.concat(" ; ").concat(lowerP.getValueAsString());
            latestMText = latestMText.concat(" ; ").concat(heartbeat.getValueAsString());
        } else {
            latestMText = latestMText.concat(" ; ").concat("Empty");
            latestMText = latestMText.concat(" ; ").concat("Empty");
            latestMText = latestMText.concat(" ; ").concat("Empty");
        }
        latestMeasurementsView.setText(latestMText);
    }

    // Als op knop Buikomtrek wordt gedrukt
    public void startBellyRadius(View view) {
        // Ongeacht of er measuremts zijn, ga naar MListActivity. Indien er measurements zijn worden ze getoond anders wordt er naar NewMeasurementsActivity gegaan
        Intent intent = new Intent(this, MListActivity.class);
        intent.putExtra(Measurement.EXTRA_INTENT_KEY_TYPE, "belly");
        startActivity(intent);
    }

    // Als op knop Bloeddruk wordt gedrukt
    public void startBloodPressure(View view) {
        // Ongeacht of er measuremts zijn, ga naar MListActivity. Indien er measurements zijn worden ze getoond anders wordt er naar NewMeasurementsActivity gegaan
        Intent intent = new Intent(this, MListActivity.class);
        intent.putExtra(Measurement.EXTRA_INTENT_KEY_TYPE, "blood");
        startActivity(intent);
    }
}