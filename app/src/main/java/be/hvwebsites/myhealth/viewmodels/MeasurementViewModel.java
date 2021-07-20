package be.hvwebsites.myhealth.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.hvwebsites.myhealth.entities.Belly;
import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.repositories.BellyRepository;
import be.hvwebsites.myhealth.repositories.MeasurementRepository;
import be.hvwebsites.myhealth.returninfo.ReturnInfo;

public class MeasurementViewModel extends AndroidViewModel {
    private MeasurementRepository repository;
    private List<Measurement> measurementList = new ArrayList<>();

    public MeasurementViewModel(Application application){
        super(application);
        repository = new MeasurementRepository();
    }

    public ReturnInfo initializeBellyViewModel(File MeasurementFile){
        ReturnInfo measurementStatus = repository.initializeRepository(MeasurementFile);
        if (measurementStatus.getReturnCode() == 0){
            measurementList = repository.getMeasurementList();
        } else if (measurementStatus.getReturnCode() == 100){
        }else {
        }
        return measurementStatus;
    }

    public boolean storeMeasurements(File measurementsFile, List<Measurement> measurementList){
        if (repository.storeMeasurements(measurementsFile, measurementList)){
            return true;
        }else {
            return false;
        }
    }

    public List<Measurement> getMeasurementList() {
        return measurementList;
    }

}
