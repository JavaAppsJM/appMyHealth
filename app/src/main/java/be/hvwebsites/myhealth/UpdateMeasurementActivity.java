package be.hvwebsites.myhealth;

import androidx.appcompat.app.AppCompatActivity;

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

public class UpdateMeasurementActivity extends AppCompatActivity {
    private TextView dateView;
    private TextView labelValue1View;
    private EditText value1View;
    private TextView labelValue2View;
    private EditText value2View;
    private TextView labelValue3View;
    private EditText value3View;
    private TextView labelInstructionView;
    private Button updateButtonView;
    private int indexToUpdate;
    private String typeMeasurement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_measurement);

        // Scherm velden definieren
        dateView = findViewById(R.id.updateScrnDatumWaarde);
        labelValue1View = findViewById(R.id.updateScrnLabelWaarde1);
        value1View = findViewById(R.id.updateScrnWaarde1);
        labelValue2View = findViewById(R.id.updateScrnLabelWaarde2);
        value2View = findViewById(R.id.updateScrnWaarde2);
        labelValue3View = findViewById(R.id.updateScrnLabelWaarde3);
        value3View = findViewById(R.id.updateScrnWaarde3);
        labelInstructionView = findViewById(R.id.updateScreenInstruction);
//        updateButtonView = findViewById(R.id.updateScrnButtonAanpassen);

        // Data uit intent halen
        Intent msrmtIntent = getIntent();
        if (msrmtIntent.hasExtra(Measurement.EXTRA_INTENT_KEY_TYPE)){
            typeMeasurement = msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_TYPE);
            Measurement oldMeasurement = new Measurement(
                    msrmtIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                    msrmtIntent.getFloatExtra(Measurement.EXTRA_INTENT_KEY_VALUE, 0));
            dateView.setText(oldMeasurement.getFormatDate());
            value1View.setText(String.valueOf(oldMeasurement.getMeasurementValue()));
            indexToUpdate = msrmtIntent.getIntExtra(Measurement.EXTRA_INTENT_KEY_INDEX, 0);
            switch (typeMeasurement) {
                case "belly":
                    labelValue1View.setText("Buikomtrek:");
                    labelValue2View.setVisibility(View.INVISIBLE);
                    value2View.setVisibility(View.INVISIBLE);
                    labelValue3View.setVisibility(View.INVISIBLE);
                    value3View.setVisibility(View.INVISIBLE);
                    labelInstructionView.setText("Pas buikomtrek aan en druk AANPASSEN");
                    // Titel vn Activity nog wijzigen
                    setTitle("Aanpassen Buikomtrek");
                    // Button ligging verhogen
//                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams();
//                    updateButtonView.setLayoutParams();
                    break;
                case "blood":
                    labelValue1View.setText("Bovendruk:");
                    value1View.setText(String.valueOf(msrmtIntent.getFloatExtra(BloodPressureM.EXTRA_INTENT_KEY_UPPER, 0)));
                    labelValue2View.setText("Onderdruk:");
                    value2View.setText(String.valueOf(msrmtIntent.getFloatExtra(BloodPressureM.EXTRA_INTENT_KEY_LOWER, 0)));
                    labelValue3View.setText("Hartslag:");
                    value3View.setText(String.valueOf(msrmtIntent.getFloatExtra(BloodPressureM.EXTRA_INTENT_KEY_HEARTB, 0)));
                    labelInstructionView.setText("Pas waarden aan en druk op AANPASSEN");
                    // Titel vn Activity nog wijzigen
                    setTitle("Aanpassen Bloeddruk Meting");
                    break;
            }
        }

        // Scherm velden vervolg
        final Button updatebutton = findViewById(R.id.updateScrnButtonAanpassen);
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // replyintent vr startActivity vn parent activity
                Intent replyIntent = new Intent(
                        UpdateMeasurementActivity.super.getApplicationContext(),
                        MListActivity.class);
                if (TextUtils.isEmpty(dateView.getText()) ||
                        TextUtils.isEmpty(value1View.getText())){
                    Toast.makeText(UpdateMeasurementActivity.this,
                            "Nothing entered, nothing saved !", Toast.LENGTH_LONG).show();
                }else{
                    // update velden terug sturen nr parent activity
                    String dateString = dateView.getText().toString();
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_DATE, dateString);
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_VALUE,
                            Float.parseFloat(String.valueOf(value1View.getText())));
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_INDEX, indexToUpdate);
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_ACTION, "update");
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_TYPE, typeMeasurement);
                    if (typeMeasurement.equals("blood")){
                        replyIntent.putExtra(BloodPressureM.EXTRA_INTENT_KEY_UPPER,
                                Float.parseFloat(String.valueOf(value1View.getText())));
                        replyIntent.putExtra(BloodPressureM.EXTRA_INTENT_KEY_LOWER,
                                Float.parseFloat(String.valueOf(value2View.getText())));
                        replyIntent.putExtra(BloodPressureM.EXTRA_INTENT_KEY_HEARTB,
                                Float.parseFloat(String.valueOf(value3View.getText())));
                    }
                    Toast.makeText(UpdateMeasurementActivity.this,
                            "Updated measurement for date: " + dateString,
                            Toast.LENGTH_LONG).show();
                    startActivity(replyIntent);
                }
            }
        });
    }
}