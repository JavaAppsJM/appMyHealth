package be.hvwebsites.myhealth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.returninfo.ReturnInfo;
import be.hvwebsites.myhealth.services.FileBaseService;
import be.hvwebsites.myhealth.viewmodels.MeasurementViewModel;

/*
//TODO: Dit project is gecloned van GitHub via "Get From VCS" en dient nog gecorrigeerd te worden
De versie die op GSM staat is flexiapptry en is iets uitgebreider in functionaliteit. Ik heb geen
zicht op wat er meer kan. De fout vn ViewModelProviders is waarschijnlijk wel opgelost.
De data in de app is testdata en kan dus gemist worden.

Conclusie: versie flexiapptry op GSM mag opgegeven worden indien nodig en we gaan verder met
dit project.

Verdere stappen:
- Corrigeren en uptodate brengen van deze versie tot een werkbare app
- Uitbreiding om een lijstje te mailen nr een emailadres (ve doctor)
 */

public class MainActivity extends AppCompatActivity {
    private MeasurementViewModel measurementViewModel;
    // Device
    private final String deviceModel = Build.MODEL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView latestMeasurementsView = findViewById(R.id.resumeinfo);

        // Creer een filebase service (bevat file base en file base directory) obv device en package name
        FileBaseService fileBaseService = new FileBaseService(deviceModel, getPackageName());

        // Basis directory definitie
        String baseDir = fileBaseService.getFileBaseDir();

        // Metingen definities
        Measurement belly = null;
        Measurement upperP = null;
        Measurement lowerP = null;
        Measurement heartbeat = null;

        // Viewmodel definitie
        measurementViewModel = new ViewModelProvider(this).get(MeasurementViewModel.class);
        //measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);
        // Initialize viewmodel met basis directory (data ophalen in viewmodel)
        ReturnInfo viewModelStatus = measurementViewModel.initializeMViewModel(baseDir);
        if (viewModelStatus.getReturnCode() == 0) {
            // Data zit in viewmodel, laatste metingen uit view model halen
            belly = measurementViewModel.getLatestBelly();
            upperP = measurementViewModel.getLatestUpperP();
            lowerP = measurementViewModel.getLatestLowerP();
            heartbeat = measurementViewModel.getLatestHeartbeat();
        } else if (viewModelStatus.getReturnCode() == 100) {
            // 1 of meerdere files niet gevonden
            Toast.makeText(MainActivity.this,
                    viewModelStatus.getReturnMessage(),
                    Toast.LENGTH_LONG).show();
        } else {
            // Ophalen data in view model mislukt
            Toast.makeText(MainActivity.this,
                    "Loading Measurements failed",
                    Toast.LENGTH_LONG).show();
        }

        // Data manipuleren om op scherm te zetten
        String latestMText;
        if (belly != null){
            // Er zijn bellies
            latestMText = belly.getDateAndValue();
        }else {
            latestMText = "Empty";
        }
        if (upperP != null && belly == null){
            // Er zijn bloeddruk metingen mr geen bellies
            latestMText = latestMText.concat(upperP.getDateAndValue());
        }else if (upperP != null){
            // Er zijn bloeddruk metingen en bellies
            latestMText = latestMText.concat(" ; ").concat(upperP.getValueAsString());
            latestMText = latestMText.concat(" ; ").concat(lowerP.getValueAsString());
            latestMText = latestMText.concat(" ; ").concat(heartbeat.getValueAsString());
        } else {
            // Er zijn geen bloeddruk metingen
            latestMText = latestMText.concat(" ; ").concat("Empty");
            latestMText = latestMText.concat(" ; ").concat("Empty");
            latestMText = latestMText.concat(" ; ").concat("Empty");
        }
        // Zet samengestelde gegevens op het scherm
        latestMeasurementsView.setText(latestMText);
    }

    // Als op knop Buikomtrek wordt gedrukt
    public void startBellyRadius(View view) {
        // Ongeacht of er measuremts zijn, ga naar MListActivity. Indien er measurements zijn
        // worden ze getoond anders wordt er naar NewMeasurementsActivity gegaan
        Intent intent = new Intent(this, MListActivity.class);
        intent.putExtra(Measurement.EXTRA_INTENT_KEY_TYPE, "belly");
        startActivity(intent);
    }

    // Als op knop Bloeddruk wordt gedrukt
    public void startBloodPressure(View view) {
        // Ongeacht of er measuremts zijn, ga naar MListActivity. Indien er measurements zijn
        // worden ze getoond anders wordt er naar NewMeasurementsActivity gegaan
        Intent intent = new Intent(this, MListActivity.class);
        intent.putExtra(Measurement.EXTRA_INTENT_KEY_TYPE, "blood");
        startActivity(intent);
    }
}