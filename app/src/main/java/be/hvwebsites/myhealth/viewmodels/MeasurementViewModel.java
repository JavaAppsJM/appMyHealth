package be.hvwebsites.myhealth.viewmodels;

import android.app.Application;
import android.os.MessageQueue;

import androidx.lifecycle.AndroidViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.hvwebsites.myhealth.R;
import be.hvwebsites.myhealth.entities.Belly;
import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.repositories.MeasurementRepository;
import be.hvwebsites.myhealth.returninfo.ReturnInfo;

public class MeasurementViewModel extends AndroidViewModel {
    private MeasurementRepository repository;
    private Measurement latestBelly;
    private Measurement latestUpperP;
    private Measurement latestLowerP;
    private Measurement latestHeartbeat;
    private List<Measurement> bellyList = new ArrayList<>();
    private List<Measurement> upperMList = new ArrayList<>();
    private List<Measurement> lowerMList = new ArrayList<>();
    private List<Measurement> heartbeatList = new ArrayList<>();
    // File declaraties
    File bellyFile;
    File upperPFile;
    File lowerPFile;
    File heartbeatFile;
    public static final String BELLY_FILE = "buikomtrek.txt";
    public static final String UPPER_BLOOD_PRESSURE_FILE = "bovendruk.txt";
    public static final String LOWER_BLOOD_PRESSURE_FILE = "onderdruk.txt";
    public static final String HEARTBEAT_FILE = "hartslag.txt";
    // TODO: Filenames vanuit strings halen
//    String bellyFileName = getResources().getString(R.string.bellyFile);

    public MeasurementViewModel(Application application){
        super(application);
        repository = new MeasurementRepository();
    }

    public ReturnInfo initializeMViewModel(String baseDir){
        // File definities
        bellyFile = new File(baseDir, BELLY_FILE);
        upperPFile = new File(baseDir, UPPER_BLOOD_PRESSURE_FILE);
        lowerPFile = new File(baseDir, LOWER_BLOOD_PRESSURE_FILE);
        heartbeatFile = new File(baseDir, HEARTBEAT_FILE);
        ReturnInfo measurementStatus = repository.initializeRepository(bellyFile);
        if (measurementStatus.getReturnCode() == 0){
            bellyList.addAll(repository.getMeasurementList());
            latestBelly = repository.getLatestMeasurement();
        } else if (measurementStatus.getReturnCode() == 100){
        }else {
        }
        measurementStatus = repository.initializeRepository(upperPFile);
        if (measurementStatus.getReturnCode() == 0){
            upperMList.addAll(repository.getMeasurementList());
            latestUpperP = repository.getLatestMeasurement();
        } else if (measurementStatus.getReturnCode() == 100){
        }else {
        }
        measurementStatus = repository.initializeRepository(lowerPFile);
        if (measurementStatus.getReturnCode() == 0){
            lowerMList.addAll(repository.getMeasurementList());
            latestLowerP = repository.getLatestMeasurement();
        } else if (measurementStatus.getReturnCode() == 100){
        }else {
        }
        measurementStatus = repository.initializeRepository(heartbeatFile);
        if (measurementStatus.getReturnCode() == 0){
            heartbeatList.addAll(repository.getMeasurementList());
            latestHeartbeat = repository.getLatestMeasurement();
        } else if (measurementStatus.getReturnCode() == 100){
        }else {
        }
        return measurementStatus;
    }

    public void removeBellyFromList(Measurement belly){
        bellyList.remove(belly);
    }

    public void removeUpperPFromList(Measurement upperP){
        upperMList.remove(upperP);
    }

    public void removeLowerPFromList(Measurement lowerP){
        lowerMList.remove(lowerP);
    }

    public void removeHeartbeatFromList(Measurement heartbeat){
        heartbeatList.remove(heartbeat);
    }

    public Measurement getLatestBelly() {
        return latestBelly;
    }

    public Measurement getLatestUpperP() {
        return latestUpperP;
    }

    public Measurement getLatestLowerP() {
        return latestLowerP;
    }

    public Measurement getLatestHeartbeat() {
        return latestHeartbeat;
    }

    public List<Measurement> getBellyList() {
        return bellyList;
    }

    public List<Measurement> getUpperMList() {
        return upperMList;
    }

    public List<Measurement> getLowerMList() {
        return lowerMList;
    }

    public List<Measurement> getHeartbeatList() {
        return heartbeatList;
    }

    public void storeBellies(){
        storeMeasurements(bellyFile, bellyList);
    }

    public void storeUpperP(){
        storeMeasurements(upperPFile, upperMList);
    }

    public void storeLowerP(){
        storeMeasurements(lowerPFile, lowerMList);
    }

    public void storeHeartbeats(){
        storeMeasurements(heartbeatFile, heartbeatList);
    }

    private boolean storeMeasurements(File measurementsFile, List<Measurement> measurementList){
        if (repository.storeMeasurements(measurementsFile, measurementList)){
            return true;
        }else {
            return false;
        }
    }
}
