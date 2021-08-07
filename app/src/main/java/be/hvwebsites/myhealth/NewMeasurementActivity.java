package be.hvwebsites.myhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.hvwebsites.myhealth.entities.Belly;
import be.hvwebsites.myhealth.entities.BloodPressureM;
import be.hvwebsites.myhealth.entities.Measurement;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measurement);
        dateView = findViewById(R.id.newScrnValueDate);
        labelValue1View = findViewById(R.id.newScrnLabelWaarde1);
        value1View = findViewById(R.id.newScrnValueWaarde1);
        labelValue2View = findViewById(R.id.newScrnLabelWaarde2);
        value2View = findViewById(R.id.newScrnValueWaarde2);
        labelValue3View = findViewById(R.id.newScrnLabelWaarde3);
        value3View = findViewById(R.id.newScrnValueWaarde3);
        labelInstructionView = findViewById(R.id.newScrnInstruction);

        // Data uit intent halen
        Intent msrmtIntent = getIntent();
        typeMeasurement = msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_TYPE);
        labelInstructionView.setText("Vul in en druk op TOEVOEGEN");
        switch (typeMeasurement) {
            case "belly":
                labelValue1View.setText("Buikomtrek:");
                labelValue2View.setVisibility(View.INVISIBLE);
                value2View.setVisibility(View.INVISIBLE);
                labelValue3View.setVisibility(View.INVISIBLE);
                value3View.setVisibility(View.INVISIBLE);
                // Titel vn Activity nog wijzigen
                setTitle("Toevoegen Buikomtrek Meting");
                // Button ligging verhogen
//                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams();
//                    updateButtonView.setLayoutParams();
                break;
            case "blood":
                labelValue1View.setText("Bovendruk:");
                labelValue2View.setText("Onderdruk:");
                labelValue3View.setText("Hartslag:");
                // Titel vn Activity nog wijzigen
                setTitle("Toevoegen Bloeddruk Meting");
                break;
        }

        final Button addbutton = findViewById(R.id.newScrnButtonToevoegen);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent(NewMeasurementActivity.this,
                        MListActivity.class);
                if (TextUtils.isEmpty(dateView.getText()) ||
                        TextUtils.isEmpty(value1View.getText())){
                    Toast.makeText(NewMeasurementActivity.this,
                            "Nothing entered, nothing saved !", Toast.LENGTH_LONG).show();
                }else{
                    String dateString = dateView.getText().toString();
                    Toast.makeText(NewMeasurementActivity.this,
                            "New measurement : " + dateString + " "
                                    + value1View.getText(),
                            Toast.LENGTH_LONG).show();
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_DATE, dateString);
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_VALUE,
                            Float.parseFloat(String.valueOf(value1View.getText())));
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_ACTION, "insert");
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_TYPE, typeMeasurement);
                    if (typeMeasurement.equals("blood")){
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
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Caller", typeMeasurement);
        newFragment.setArguments(bundle);
        FragmentManager bellyFragmentMgr = getSupportFragmentManager();
        newFragment.show(bellyFragmentMgr, getString(R.string.datepicker));
    }

    @Override
    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (day_string +
                "/" + month_string + "/" + year_string);

        dateView.setText(dateMessage);

        Toast.makeText(this, "Date: " + dateMessage, Toast.LENGTH_SHORT).show();
    }
}