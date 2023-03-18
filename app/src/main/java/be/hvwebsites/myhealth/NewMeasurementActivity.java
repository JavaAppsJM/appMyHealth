package be.hvwebsites.myhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.hvwebsites.myhealth.constants.GlobalConstant;
import be.hvwebsites.myhealth.helpers.BloodPressureM;
import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.fragments.DatePickerFragment;
import be.hvwebsites.myhealth.interfaces.NewDatePickerInterface;

public class NewMeasurementActivity extends AppCompatActivity implements NewDatePickerInterface {
    private EditText dateView;
    private TextView labelValue1View;
    private EditText value1View;
    private TextView labelValue2View;
    private EditText value2View;
    private TextView labelValue3View;
    private EditText value3View;
    private TextView labelInstructionView;
    private String typeMeasurement;
    // Device
    private final String deviceModel = Build.MODEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measurement);
        // Koppel variabelen aan scherm componenten
        dateView = findViewById(R.id.newScrnValueDate);
        labelValue1View = findViewById(R.id.newScrnLabelWaarde1);
        value1View = findViewById(R.id.newScrnValueWaarde1);
        labelValue2View = findViewById(R.id.newScrnLabelWaarde2);
        value2View = findViewById(R.id.newScrnValueWaarde2);
        labelValue3View = findViewById(R.id.newScrnLabelWaarde3);
        value3View = findViewById(R.id.newScrnValueWaarde3);
        labelInstructionView = findViewById(R.id.newScrnInstruction);

        // Vul vaste schermgegevens in
        // Data uit intent halen
        Intent msrmtIntent = getIntent();
        typeMeasurement = msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_TYPE);
        labelInstructionView.setText(GlobalConstant.INSTRUCTION_NEW);
        switch (typeMeasurement) {
            case GlobalConstant.CASE_BELLY:
                labelValue1View.setText(GlobalConstant.LABEL_BELLY_COLON);
                labelValue2View.setVisibility(View.INVISIBLE);
                value2View.setVisibility(View.INVISIBLE);
                labelValue3View.setVisibility(View.INVISIBLE);
                value3View.setVisibility(View.INVISIBLE);
                // Titel vn Activity nog wijzigen
                setTitle(GlobalConstant.TITLE_ADD_BELLY);
                // Button ligging verhogen
//                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams();
//                    updateButtonView.setLayoutParams();
                break;
            case GlobalConstant.CASE_BLOOD:
                labelValue1View.setText(GlobalConstant.LABEL_UPPER_COLON);
                labelValue2View.setText(GlobalConstant.LABEL_LOWER_COLON);
                labelValue3View.setText(GlobalConstant.LABEL_HEARTBEAT_COLON);
                // Titel vn Activity nog wijzigen
                setTitle(GlobalConstant.TITLE_ADD_BLOOD);
                break;
        }

        // Toevoeg button definitie
        final Button addbutton = findViewById(R.id.newScrnButtonToevoegen);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Als op de toevoeg button geklikt is worden de gegevens, die ingevuld zijn
                // op het scherm, in de intent doorgegeven
                Intent replyIntent = new Intent(NewMeasurementActivity.this,
                        MListActivity.class);
                if (TextUtils.isEmpty(dateView.getText()) ||
                        TextUtils.isEmpty(value1View.getText())){
                    // Er zijn geen gegevens ingevuld
                    Toast.makeText(NewMeasurementActivity.this,
                            "Nothing entered, nothing saved !", Toast.LENGTH_LONG).show();
                }else{
                    // Er zijn gegevens ingevuld
                    String dateString = dateView.getText().toString();
                    Toast.makeText(NewMeasurementActivity.this,
                            "New measurement : " + dateString + " "
                                    + value1View.getText(),
                            Toast.LENGTH_LONG).show();
                    // De gegevens worden in de intent ingevuld
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_DATE, dateString);
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_VALUE,
                            Float.parseFloat(String.valueOf(value1View.getText())));
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_ACTION, GlobalConstant.ACTION_INSERT);
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_TYPE, typeMeasurement);
                    if (typeMeasurement.equals(GlobalConstant.CASE_BLOOD)){
                        replyIntent.putExtra(BloodPressureM.EXTRA_INTENT_KEY_UPPER,
                                Float.parseFloat(String.valueOf(value1View.getText())));
                        replyIntent.putExtra(BloodPressureM.EXTRA_INTENT_KEY_LOWER,
                                Float.parseFloat(String.valueOf(value2View.getText())));
                        replyIntent.putExtra(BloodPressureM.EXTRA_INTENT_KEY_HEARTB,
                                Float.parseFloat(String.valueOf(value3View.getText())));
                    }
                    startActivity(replyIntent);
                }
            }
        });
    }

    @Override
    public void showDatePicker(View view) {
        // Toont de datum picker, de gebruiker kan nu de datum picken
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Caller", typeMeasurement);
        newFragment.setArguments(bundle);
        FragmentManager bellyFragmentMgr = getSupportFragmentManager();
        newFragment.show(bellyFragmentMgr, getString(R.string.datepicker));
    }

    @Override
    public void processDatePickerResult(int year, int month, int day) {
        // Verwerkt de gekozen datum uit de picker
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (day_string +
                "/" + month_string + "/" + year_string);

        dateView.setText(dateMessage);

        Toast.makeText(this, "Date: " + dateMessage, Toast.LENGTH_SHORT).show();
    }
}