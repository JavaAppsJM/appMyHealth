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
import be.hvwebsites.myhealth.entities.Measurement;

public class UpdateBellyMActivity extends AppCompatActivity {
    private TextView dateView;
    private EditText radiusView;
    private Measurement oldBelly;
    private int indexToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_belly_m);

        // Scherm velden definieren
        dateView = findViewById(R.id.updateBellyDateValue);
        radiusView = findViewById(R.id.updateNumberRadius);

        // Data uit intent halen
        Intent bellyIntent = getIntent();
        if (bellyIntent.hasExtra(Belly.EXTRA_INTENT_KEY_DATE)){
            oldBelly = new Belly(
                    bellyIntent.getStringExtra(Measurement.EXTRA_INTENT_KEY_DATE),
                    bellyIntent.getFloatExtra(Measurement.EXTRA_INTENT_KEY_VALUE, 0));
            dateView.setText(oldBelly.getFormatDate());
            radiusView.setText(String.valueOf(oldBelly.getMeasurementValue()));
            indexToUpdate = bellyIntent.getIntExtra(BellyActivity.EXTRA_INTENT_KEY_INDEX, 0);
        }

        // Scherm velden vervolg
        final Button addbutton = findViewById(R.id.updateBelly);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // replyintent vr startActivity vn BellyActivity
                Intent replyIntent = new Intent(
                        UpdateBellyMActivity.super.getApplicationContext(),
                        BellyActivity.class);
                if (TextUtils.isEmpty(dateView.getText()) ||
                        TextUtils.isEmpty(radiusView.getText())){
                    Toast.makeText(UpdateBellyMActivity.this,
                            "Nothing entered, nothing saved !", Toast.LENGTH_LONG).show();
                }else{
                    // update velden terug sturen nr BellyActivity
                    String dateString = dateView.getText().toString();
                    Measurement belly = new Belly(dateString,
                            Float.parseFloat(String.valueOf(radiusView.getText())));
                    Toast.makeText(UpdateBellyMActivity.this,
                            "Updated belly measurement: " + belly.toString(),
                            Toast.LENGTH_LONG).show();
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_DATE, dateString);
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_VALUE,
                            Float.parseFloat(String.valueOf(radiusView.getText())));
                    replyIntent.putExtra(BellyActivity.EXTRA_INTENT_KEY_INDEX, indexToUpdate);
                    replyIntent.putExtra(BellyActivity.EXTRA_INTENT_KEY_ACTION, "update");
                    startActivity(replyIntent);
                }
            }
        });
    }
}