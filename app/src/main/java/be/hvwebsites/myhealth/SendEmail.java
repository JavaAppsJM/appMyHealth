package be.hvwebsites.myhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.zip.DataFormatException;

import be.hvwebsites.myhealth.constants.GlobalConstant;
import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.fragments.DatePickerFragment;
import be.hvwebsites.myhealth.helpers.BloodPressureM;
import be.hvwebsites.myhealth.helpers.DateString;
import be.hvwebsites.myhealth.helpers.ReturnInfo;
import be.hvwebsites.myhealth.interfaces.NewDatePickerInterface;
import be.hvwebsites.myhealth.repositories.Cookie;
import be.hvwebsites.myhealth.repositories.CookieRepository;
import be.hvwebsites.myhealth.services.FileBaseService;
import be.hvwebsites.myhealth.viewmodels.MeasurementViewModel;

public class SendEmail extends AppCompatActivity implements NewDatePickerInterface {
    private MeasurementViewModel measurementViewModel;
    private EditText minimumDateView;
    private String minimumDateStr;
    private CheckBox alreadySentView;
    private boolean alreadySent;
    private EditText emailAdresView;
    private String emailadresStr;

    // Device
    private final String deviceModel = Build.MODEL;

    // Constants
    private static final String EMAIL_ADRES_KEY = "emailadres";
    private static final String EMAIL_ADRES_DEFAULT = "mj842580@gmail.com";
    private static final String EMAIL_ADRES_SUBJECT = "Bloeddrukmetingen vanaf ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        // Koppel variabelen aan scherm componenten
        minimumDateView = findViewById(R.id.valueMinimumDate);
        alreadySentView = findViewById(R.id.checkboxAlreadySent);
        emailAdresView = findViewById(R.id.valueEmailAdres);

        // Ophalen metingen
        // Creer een filebase service (bevat file base en file base directory) obv device en package name
        FileBaseService fileBaseService = new FileBaseService(deviceModel, getPackageName());

        // Basis directory definitie
        String baseDir = fileBaseService.getFileBaseDir();

        // Viewmodel definitie
        measurementViewModel = new ViewModelProvider(this).get(MeasurementViewModel.class);
        // Initialize viewmodel met basis directory (data ophalen in viewmodel)
        List<ReturnInfo> viewModelRetInfo = measurementViewModel.initializeMViewModel(baseDir);
        for (int i = 0; i < viewModelRetInfo.size(); i++) {
            Toast.makeText(SendEmail.this,
                    viewModelRetInfo.get(i).getReturnMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        if (!measurementViewModel.isErrorViewModel()) {
            // Data zit in viewmodel, laatste metingen uit view model halen
        } else {
            // Ophalen data in view model mislukt
            Toast.makeText(SendEmail.this,
                    "Loading Measurements failed",
                    Toast.LENGTH_LONG).show();
            //TODO: Wat moet er nu gebeuren ??
        }

        // Ophalen emailadres als cookie
        CookieRepository cookieRepository = new CookieRepository(baseDir);
        emailadresStr = cookieRepository.getCookieValueFromLabel(EMAIL_ADRES_KEY);
        if (emailadresStr == null){
            emailadresStr = EMAIL_ADRES_DEFAULT;
        }

        // emailadres op scherm invullen
        emailAdresView.setText(emailadresStr);

        // Send button definitie
        final Button sendButton = findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent(SendEmail.this,
                        MainActivity.class);
                if (TextUtils.isEmpty(minimumDateView.getText()) ||
                        TextUtils.isEmpty(emailAdresView.getText())){
                    // Er zijn geen gegevens ingevuld
                    Toast.makeText(SendEmail.this,
                            "Nothing entered, nothing send !", Toast.LENGTH_LONG).show();
                }else{
                    // Er zijn gegevens ingevuld
                    minimumDateStr = minimumDateView.getText().toString();
                    emailadresStr = emailAdresView.getText().toString();
                    alreadySent = alreadySentView.isChecked();
                    Toast.makeText(SendEmail.this,
                            "Metingen verstuurd !",
                            Toast.LENGTH_LONG).show();

                    // emailadres bewaren in cookie
                    cookieRepository.addCookie(new Cookie(EMAIL_ADRES_KEY, emailadresStr));

                    // De gegevens worden in een mail verwerkt
                    String[] emailAdresses = new String[0];
                    emailAdresses[0] = emailadresStr;
                    String emailSubject = EMAIL_ADRES_SUBJECT + new DateString(minimumDateStr).getFormatDate();
                    String emailBody = measurementViewModel.getMeasurementsForEmail(minimumDateStr, alreadySent);
                    composeEmail(emailAdresses, emailSubject, emailBody);

                    // Ga terug nr MainActivity
                    startActivity(replyIntent);
                }
            }
        });
    }

    public void composeEmail(String[] addresses, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public void showDatePicker(View view) {
        // Toont de datum picker, de gebruiker kan nu de datum picken
        DialogFragment newFragment = new DatePickerFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("Caller", typeMeasurement);
//        newFragment.setArguments(bundle);
        FragmentManager emailFragmentMgr = getSupportFragmentManager();
        newFragment.show(emailFragmentMgr, getString(R.string.datepicker));

    }

    @Override
    public void processDatePickerResult(int year, int month, int day) {
        // Verwerkt de gekozen datum uit de picker
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (day_string +
                "/" + month_string + "/" + year_string);

        minimumDateView.setText(dateMessage);

        Toast.makeText(this, "Date: " + dateMessage, Toast.LENGTH_SHORT).show();
    }
}