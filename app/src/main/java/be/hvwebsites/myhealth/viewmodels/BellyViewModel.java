package be.hvwebsites.myhealth.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.hvwebsites.myhealth.entities.Belly;
import be.hvwebsites.myhealth.repositories.BellyRepository;
import be.hvwebsites.myhealth.returninfo.ReturnInfo;

public class BellyViewModel extends AndroidViewModel {
    private BellyRepository repository;
    private List<Belly> tBellyList = new ArrayList<>();

    public BellyViewModel(Application application){
        super(application);
        repository = new BellyRepository();
    }

    public ReturnInfo initializeBellyViewModel(File bellyFile){
        ReturnInfo bellyToestand = repository.initializeRepository(bellyFile);
        if (bellyToestand.getReturnCode() == 0){
            tBellyList = repository.getTBellyList();
            Belly latestBelly = repository.getLatestBelly();
        } else if (bellyToestand.getReturnCode() == 100){
        }else {
        }
        return bellyToestand;
    }

    public boolean storeBellies(File bellyFile, List<Belly> bellyList){
        if (repository.storeBellies(bellyFile, bellyList)){
            return true;
        }else {
            return false;
        }
    }

    public List<Belly> gettBellyList() {
        return tBellyList;
    }

}
