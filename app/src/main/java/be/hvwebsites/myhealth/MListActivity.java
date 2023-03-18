package be.hvwebsites.myhealth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import be.hvwebsites.myhealth.adapters.MeasurementListAdapter;
import be.hvwebsites.myhealth.constants.GlobalConstant;
import be.hvwebsites.myhealth.entities.Belly;
import be.hvwebsites.myhealth.entities.BloodLower;
import be.hvwebsites.myhealth.helpers.BloodPressureM;
import be.hvwebsites.myhealth.entities.BloodUpper;
import be.hvwebsites.myhealth.entities.HeartBeat;
import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.helpers.MListLine;
import be.hvwebsites.myhealth.repositories.Cookie;
import be.hvwebsites.myhealth.repositories.CookieRepository;
import be.hvwebsites.myhealth.helpers.ReturnInfo;
import be.hvwebsites.myhealth.services.FileBaseService;
import be.hvwebsites.myhealth.viewmodels.MeasurementViewModel;


public class MListActivity extends AppCompatActivity {
    private MeasurementViewModel measurementViewModel;
    private List<MListLine> lineList = new ArrayList<>();
    private String typeMeasurement;
    public static final String TYPE_MEASUREMENT = "typemeasurement";
    // Device
    private final String deviceModel = Build.MODEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlist);
        TextView labelCol2Head = findViewById(R.id.mListColHead2);
        TextView labelCol3Head = findViewById(R.id.mListColHead3);
        TextView labelCol4Head = findViewById(R.id.mListColHead4);

        // Recyclerview definieren
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final MeasurementListAdapter adapter = new MeasurementListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Creer een filebase service (bevat file base en file base directory) obv device en package name
        FileBaseService fileBaseService = new FileBaseService(deviceModel, getPackageName());

        // Basis directory definitie
        String baseDir = fileBaseService.getFileBaseDir();

        // Data ophalen
        // Get a viewmodel from the viewmodelproviders
        measurementViewModel = new ViewModelProvider(this).get(MeasurementViewModel.class);
        // Initialize viewmodel mt basis directory (data wordt opgehaald in viewmodel)
        List<ReturnInfo> viewModelRetInfo = measurementViewModel.initializeMViewModel(baseDir);
        for (int i = 0; i < viewModelRetInfo.size(); i++) {
            Toast.makeText(MListActivity.this,
                    viewModelRetInfo.get(i).getReturnMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        // Data uit intent halen als die er is
        Intent msrmtIntent = getIntent();
        CookieRepository cookieRepository = new CookieRepository(baseDir);
        if (msrmtIntent.hasExtra(Measurement.EXTRA_INTENT_KEY_TYPE)){
            typeMeasurement = msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_TYPE);
            // typeMeasurement in cookie steken
            cookieRepository.addCookie(new Cookie(TYPE_MEASUREMENT, typeMeasurement));
        }else {
            // er is geen intent, typemeasurement ophalen als Cookie
            typeMeasurement = cookieRepository.getCookieValueFromLabel(TYPE_MEASUREMENT);
        }

        // fab definieren voor een meting toe te voegen
        // We doen pas hier omdat typeMeasurement moet gekend zijn
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MListActivity.this,
                        NewMeasurementActivity.class);
                intent.putExtra(Measurement.EXTRA_INTENT_KEY_ACTION,
                        getResources().getText(R.string.Insert));
                intent.putExtra(Measurement.EXTRA_INTENT_KEY_TYPE, typeMeasurement);
                startActivity(intent);
            }
        });

        // data zit nu measurementViewModel, tonen op scherm afhankelijk vn type
        switch (typeMeasurement) {
            case GlobalConstant.CASE_BELLY:
                // Zet kolom titels
                labelCol2Head.setText(getResources().getText(R.string.belly_radius_input_label));
                labelCol3Head.setVisibility(View.INVISIBLE);
                labelCol4Head.setVisibility(View.INVISIBLE);

                lineList = fillLineListWithBellies(measurementViewModel.getBellyList());
                adapter.setLineList(lineList);
                setTitle(GlobalConstant.TITLE_BELLIES);
                // TODO: Button ligging verhogen
//                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams();
//                    updateButtonView.setLayoutParams();
                break;
            case GlobalConstant.CASE_BLOOD:
                // Zet kolom titels
                labelCol2Head.setText(GlobalConstant.LABEL_UPPER);
                labelCol3Head.setVisibility(View.VISIBLE);
                labelCol3Head.setText(GlobalConstant.LABEL_LOWER);
                labelCol4Head.setVisibility(View.VISIBLE);
                labelCol4Head.setText(GlobalConstant.LABEL_HEARTBEAT);

                lineList = fillLineListWithBlood(measurementViewModel.getUpperMList(),
                        measurementViewModel.getLowerMList(),
                        measurementViewModel.getHeartbeatList());
                adapter.setLineList(lineList);
                setTitle(GlobalConstant.TITLE_BLOOD);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + typeMeasurement);
        }

        // om te kunnen swipen in de recyclerview ; swippen == deleten
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Measurement dMeasurement = new Measurement();
                        MListLine currentLine = adapter.getLineAtPosition(position);
                        // LineList aanpassen
                        lineList.remove(currentLine);
                        switch (currentLine.getmType()){
                            case GlobalConstant.CASE_BELLY:
                                dMeasurement = currentLine.findCorM(measurementViewModel.getBellyList());
                                // Verwijder belly uit bellylist
                                measurementViewModel.removeBellyFromList(dMeasurement);
                                // Store bellies in file
                                measurementViewModel.storeBellies();
                                break;
                            case GlobalConstant.CASE_BLOOD:
                                // Upper pressure deleten
                                dMeasurement = currentLine.findCorM(measurementViewModel.getUpperMList());
                                // Verwijder upper uit list
                                measurementViewModel.removeUpperPFromList(dMeasurement);
                                // Lower pressure deleten
                                dMeasurement = currentLine.findCorM(measurementViewModel.getLowerMList());
                                // Verwijder lower uit list
                                measurementViewModel.removeLowerPFromList(dMeasurement);
                                // heartbeat deleten
                                dMeasurement = currentLine.findCorM(measurementViewModel.getHeartbeatList());
                                // Verwijder heartbeat uit list
                                measurementViewModel.removeHeartbeatFromList(dMeasurement);
                                // Wegschrijven nr file
                                measurementViewModel.storeUpperP();
                                measurementViewModel.storeLowerP();
                                measurementViewModel.storeHeartbeats();
                                break;
                        }
                        Toast.makeText(MListActivity.this,
                                "Deleting measurement on " + currentLine.getFormatDate(),
                                Toast.LENGTH_LONG).show();
                        // Refresh recyclerview
                        adapter.setLineList(lineList);
                    }
                });
        helper.attachToRecyclerView(recyclerView);

        // verwerken replyIntent vn update of new
        if (msrmtIntent.hasExtra(Measurement.EXTRA_INTENT_KEY_ACTION)) {
            String action = msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_ACTION);
            typeMeasurement = msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_TYPE);
            if (action.equals(GlobalConstant.ACTION_UPDATE)){
                switch (typeMeasurement){
                    case GlobalConstant.CASE_BELLY:
                        Belly newBelly = new Belly(
                                msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                                msrmtIntent.getFloatExtra(Measurement.EXTRA_INTENT_KEY_VALUE,
                                        0));
                        int indexToUpdate = msrmtIntent.getIntExtra(Measurement.EXTRA_INTENT_KEY_INDEX, 0);
                        measurementViewModel.getBellyList().set(indexToUpdate, newBelly);
                        // Wegschrijven nr file
                        measurementViewModel.storeBellies();
                        // Refresh scherm
                        lineList = fillLineListWithBellies(measurementViewModel.getBellyList());
                        adapter.setLineList(lineList);
                        break;
                    case GlobalConstant.CASE_BLOOD:
                        // upperpressure
                        BloodUpper newUpperP = new BloodUpper(
                                msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                                msrmtIntent.getFloatExtra(BloodPressureM.EXTRA_INTENT_KEY_UPPER,
                                        0));
                        indexToUpdate = msrmtIntent.getIntExtra(Measurement.EXTRA_INTENT_KEY_INDEX, 0);
                        measurementViewModel.getUpperMList().set(indexToUpdate, newUpperP);
                        // lowerpressure
                        BloodLower newLowerP = new BloodLower(
                                msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                                msrmtIntent.getFloatExtra(BloodPressureM.EXTRA_INTENT_KEY_LOWER,
                                        0));
                        indexToUpdate = msrmtIntent.getIntExtra(Measurement.EXTRA_INTENT_KEY_INDEX, 0);
                        measurementViewModel.getLowerMList().set(indexToUpdate, newLowerP);
                        // heartbeat
                        BloodUpper newHeartbeat = new BloodUpper(
                                msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                                msrmtIntent.getFloatExtra(BloodPressureM.EXTRA_INTENT_KEY_HEARTB,
                                        0));
                        indexToUpdate = msrmtIntent.getIntExtra(Measurement.EXTRA_INTENT_KEY_INDEX, 0);
                        measurementViewModel.getHeartbeatList().set(indexToUpdate, newHeartbeat);
                        // Wegschrijven nr file
                        measurementViewModel.storeUpperP();
                        measurementViewModel.storeLowerP();
                        measurementViewModel.storeHeartbeats();
                        // Refresh scherm
                        lineList = fillLineListWithBlood(measurementViewModel.getUpperMList(),
                                measurementViewModel.getLowerMList(),
                                measurementViewModel.getHeartbeatList());
                        adapter.setLineList(lineList);
                        break;
                }
            } else if (action.equals(GlobalConstant.ACTION_INSERT)){
                switch (typeMeasurement){
                    case GlobalConstant.CASE_BELLY:
                        Measurement newBelly = new Belly(
                                msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                                msrmtIntent.getFloatExtra(Measurement.EXTRA_INTENT_KEY_VALUE,
                                        0));
                        if (bestaatMeasurement(newBelly.getMeasurementDate(), measurementViewModel.getBellyList())){
                            Toast.makeText(MListActivity.this,
                                    "Voor deze datum is er reeds een measurement !",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            // Toevoegen aan bellyList
                            measurementViewModel.getBellyList().add(newBelly);
                            // Wegschrijven nr file
                            measurementViewModel.storeBellies();
                            // Refresh recyclerview
                            adapter.setLineList(fillLineListWithBellies(measurementViewModel.getBellyList()));
                        }
                        break;
                    case GlobalConstant.CASE_BLOOD:
                        // upperpressure
                        Measurement newUpper = new BloodUpper(
                                msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                                msrmtIntent.getFloatExtra(BloodPressureM.EXTRA_INTENT_KEY_UPPER,
                                        0));
                        if (bestaatMeasurement(newUpper.getMeasurementDate(), measurementViewModel.getUpperMList())){
                            Toast.makeText(MListActivity.this,
                                    "Voor deze datum is er reeds een measurement !",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            // Toevoegen aan List
                            measurementViewModel.getUpperMList().add(newUpper);
                            // Wegschrijven nr file
                            measurementViewModel.storeUpperP();
                        }
                        // lowerpressure
                        Measurement newLower = new BloodLower(
                                msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                                msrmtIntent.getFloatExtra(BloodPressureM.EXTRA_INTENT_KEY_LOWER,
                                        0));
                        if (bestaatMeasurement(newLower.getMeasurementDate(), measurementViewModel.getLowerMList())){
                            Toast.makeText(MListActivity.this,
                                    "Voor deze datum is er reeds een measurement !",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            // Toevoegen aan List
                            measurementViewModel.getLowerMList().add(newLower);
                            // Wegschrijven nr file
                            measurementViewModel.storeLowerP();
                        }
                        // heartbeat
                        Measurement newHeartbeat = new HeartBeat(
                                msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                                msrmtIntent.getFloatExtra(BloodPressureM.EXTRA_INTENT_KEY_HEARTB,
                                        0));
                        if (bestaatMeasurement(newHeartbeat.getMeasurementDate(), measurementViewModel.getHeartbeatList())){
                            Toast.makeText(MListActivity.this,
                                    "Voor deze datum is er reeds een measurement !",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            // Toevoegen aan bellyList
                            measurementViewModel.getHeartbeatList().add(newHeartbeat);
                            // Wegschrijven nr file
                            measurementViewModel.storeHeartbeats();
                        }
                        // Refresh scherm
                        lineList = fillLineListWithBlood(measurementViewModel.getUpperMList(),
                                measurementViewModel.getLowerMList(),
                                measurementViewModel.getHeartbeatList());
                        adapter.setLineList(lineList);
                        break;
                }
            }
        }
    }

    public boolean bestaatMeasurement(String date, List<Measurement> measurements){
        for (int i = 0; i < measurements.size(); i++) {
            if (measurements.get(i).getMeasurementDate().equals(date)){
                //  bestaat reeds
                return true;
            }
        }
        return false;
    }

    private List<MListLine> fillLineListWithBellies(List<Measurement> bellyList){
        List<MListLine> lineList = new ArrayList<>();
        for (int i = 0; i < bellyList.size(); i++) {
            MListLine listLine = new MListLine();
            listLine.fillListLine(bellyList.get(i),null, null, null);
            lineList.add(listLine);
        }
        return lineList;
    }

    private List<MListLine> fillLineListWithBlood(List<Measurement> upperPList,
                                                  List<Measurement> lowerPList,
                                                  List<Measurement> heartbeatPList){
        List<MListLine> lineList = new ArrayList<>();
        for (int i = 0; i < upperPList.size(); i++) {
            MListLine listLine = new MListLine();
            listLine.fillListLine(null, upperPList.get(i), lowerPList.get(i), heartbeatPList.get(i));
            lineList.add(listLine);
        }
        return lineList;
    }
}