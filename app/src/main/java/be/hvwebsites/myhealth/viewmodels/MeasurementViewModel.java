package be.hvwebsites.myhealth.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.hvwebsites.myhealth.constants.GlobalConstant;
import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.helpers.BloodPressureM;
import be.hvwebsites.myhealth.helpers.DateString;
import be.hvwebsites.myhealth.repositories.MeasurementRepository;
import be.hvwebsites.myhealth.helpers.ReturnInfo;

public class MeasurementViewModel extends AndroidViewModel {
    private MeasurementRepository repository;
    private boolean errorViewModel = false;
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
    // File names constants
    public static final String BELLY_FILE = "buikomtrek.txt";
    public static final String UPPER_BLOOD_PRESSURE_FILE = "bovendruk.txt";
    public static final String LOWER_BLOOD_PRESSURE_FILE = "onderdruk.txt";
    public static final String HEARTBEAT_FILE = "hartslag.txt";
    // Errors
    public static final String ERROR_BELLIES = "Fout bij ophalen buikomtrek metingen !";
    public static final String ERROR_BLOODPRESSURES = "Fout bij ophalen bloeddruk metingen !";
    public static final String ERROR_HEARTBEATS = "Fout bij ophalen hartslag metingen !";


    public MeasurementViewModel(Application application){
        super(application);
        repository = new MeasurementRepository();
    }

    public List<ReturnInfo> initializeMViewModel(String baseDir){
        List<ReturnInfo> returninfo = new ArrayList<>();

        // File definities
        bellyFile = new File(baseDir, BELLY_FILE);
        upperPFile = new File(baseDir, UPPER_BLOOD_PRESSURE_FILE);
        lowerPFile = new File(baseDir, LOWER_BLOOD_PRESSURE_FILE);
        heartbeatFile = new File(baseDir, HEARTBEAT_FILE);

        // Bellies ophalen
        ReturnInfo measurementStatus = repository.initializeRepository(bellyFile);
        if (measurementStatus.getReturnCode() == 0){
            bellyList.addAll(repository.getMeasurementList());
            latestBelly = repository.getLatestMeasurement();
        } else if (measurementStatus.getReturnCode() == 100){
            returninfo.add(new ReturnInfo(100, GlobalConstant.NO_BELLIES_YET));
        }else {
            returninfo.add(new ReturnInfo(measurementStatus.getReturnCode(), ERROR_BELLIES));
            errorViewModel = true;
        }

        // Bovendruk metingen ophalen
        measurementStatus = repository.initializeRepository(upperPFile);
        if (measurementStatus.getReturnCode() == 0){
            upperMList.addAll(repository.getMeasurementList());
            latestUpperP = repository.getLatestMeasurement();
        } else if (measurementStatus.getReturnCode() == 100){
            returninfo.add(new ReturnInfo(100, GlobalConstant.NO_BLOODPRESSURES_YET));
        }else {
            returninfo.add(new ReturnInfo(measurementStatus.getReturnCode(), ERROR_BLOODPRESSURES));
            errorViewModel = true;
        }

        // Onderdruk metingen ophalen
        measurementStatus = repository.initializeRepository(lowerPFile);
        if (measurementStatus.getReturnCode() == 0){
            lowerMList.addAll(repository.getMeasurementList());
            latestLowerP = repository.getLatestMeasurement();
        } else if (measurementStatus.getReturnCode() == 100){
            returninfo.add(new ReturnInfo(100, GlobalConstant.NO_BLOODPRESSURES_YET));
        }else {
            returninfo.add(new ReturnInfo(measurementStatus.getReturnCode(), ERROR_BLOODPRESSURES));
            errorViewModel = true;
        }

        // Hartslag metingen ophalen
        measurementStatus = repository.initializeRepository(heartbeatFile);
        if (measurementStatus.getReturnCode() == 0){
            heartbeatList.addAll(repository.getMeasurementList());
            latestHeartbeat = repository.getLatestMeasurement();
        } else if (measurementStatus.getReturnCode() == 100){
            returninfo.add(new ReturnInfo(100, GlobalConstant.NO_HEARTBEATS_YET));
        }else {
            returninfo.add(new ReturnInfo(measurementStatus.getReturnCode(), ERROR_HEARTBEATS));
            errorViewModel = true;
        }
        return returninfo;
    }

    public List<String> getMeasurementsForEmail(String minimumDate, boolean alreadySent){
        // Lists de measurements to send
        // minimumDate format ex: 01022022
        // alreadySent : true als de metingen die reeds verstuurd zijn terug mogen gestuurd worden
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < upperMList.size(); i++) {
            if ((minimumDate == null || upperMList.get(i).getDateInt() > new DateString(minimumDate).getIntDate())
            && (alreadySent || upperMList.get(i).getLatestEmailDate() == null)){
                // Meting mag verstuurd worden
                resultList.add(new BloodPressureM(upperMList.get(i), lowerMList.get(i), heartbeatList.get(i))
                        .getEmailString());
            }
        }
        return resultList;
    }

    public boolean isErrorViewModel() {
        return errorViewModel;
    }

    public void setErrorViewModel(boolean errorViewModel) {
        this.errorViewModel = errorViewModel;
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
