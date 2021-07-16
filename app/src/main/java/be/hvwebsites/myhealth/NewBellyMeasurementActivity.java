package be.hvwebsites.myhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.hvwebsites.myhealth.entities.Belly;

public class NewBellyMeasurementActivity extends AppCompatActivity {
    private EditText dateView;
    private EditText radiusView;
    public static final String EXTRA_INTENT_KEY_ACTION =
            "be.hvwebsites.myhealth.INTENT_KEY_ACTION";
    public static final String EXTRA_INTENT_KEY_DATE =
            "be.hvwebsites.myhealth.INTENT_KEY_DATE";
    public static final String EXTRA_INTENT_KEY_RADIUS =
            "be.hvwebsites.myhealth.INTENT_KEY_RADIUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_belly_measurement);
        dateView = findViewById(R.id.editTextDate);
        radiusView = findViewById(R.id.editNumberRadius);
        final Button addbutton = findViewById(R.id.addNewBelly);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // replyintent vr startActivityForResult
                //Intent replyIntent = new Intent();
                // Gewone startactivity dan kan de reply in de onCreate verwerkt worden
                // dit om de refresh vd adapter te kunnen doen
                Intent replyIntent = new Intent(NewBellyMeasurementActivity.this,
                        BellyActivity.class);
                if (TextUtils.isEmpty(dateView.getText()) ||
                        TextUtils.isEmpty(radiusView.getText())){
                    Toast.makeText(NewBellyMeasurementActivity.this,
                            "Nothing entered, nothing saved !", Toast.LENGTH_LONG).show();
                }else{
                    String dateString = dateView.getText().toString();
                    Belly belly = new Belly(dateString,
                            Float.parseFloat(String.valueOf(radiusView.getText())));
                    Toast.makeText(NewBellyMeasurementActivity.this,
                            "New belly measurement : " + belly.toString(),
                            Toast.LENGTH_LONG).show();
                    replyIntent.putExtra(EXTRA_INTENT_KEY_DATE, dateString);
                    replyIntent.putExtra(EXTRA_INTENT_KEY_RADIUS,
                            Float.parseFloat(String.valueOf(radiusView.getText())));
                    // Bepalen of er moet geupdate of geinsert worden obv wat er via de intent binnenkomt
                    // is niet langer nodig gezien update niet langs hier mag komen !!
                    // Intent intentAction = getIntent();
                    // setResult vr startActivityForResult
                    //setResult(RESULT_OK, replyIntent);
                    replyIntent.putExtra(EXTRA_INTENT_KEY_ACTION, "insert");
                    startActivity(replyIntent);
                }
                //finish();
            }
        });
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), getString(R.string.datepicker));
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (day_string +
                "/" + month_string + "/" + year_string);

        dateView.setText(dateMessage);

        Toast.makeText(this, "Date: " + dateMessage, Toast.LENGTH_SHORT).show();
    }
}