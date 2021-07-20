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
import be.hvwebsites.myhealth.entities.Measurement;

public class NewBellyMeasurementActivity extends AppCompatActivity {
    private EditText dateView;
    private EditText radiusView;

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
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_DATE, dateString);
                    replyIntent.putExtra(Measurement.EXTRA_INTENT_KEY_VALUE,
                            Float.parseFloat(String.valueOf(radiusView.getText())));
                    replyIntent.putExtra(BellyActivity.EXTRA_INTENT_KEY_ACTION, "insert");
                    startActivity(replyIntent);
                }
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