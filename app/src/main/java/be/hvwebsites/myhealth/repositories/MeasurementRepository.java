package be.hvwebsites.myhealth.repositories;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.helpers.ReturnInfo;

public class MeasurementRepository {
    private Measurement latestMeasurement;
    private List<Measurement> measurementList;

    public MeasurementRepository(){
        measurementList = new ArrayList<>();
    }

    public Measurement getLatestMeasurement  () {
        return latestMeasurement;
    }

    public List<Measurement> getMeasurementList() {
        return measurementList;
    }

    public ReturnInfo initializeRepository(File mFile){
        // Initialize latestMeasurement en measurementList
        latestMeasurement = new Measurement();
        measurementList.clear();
        ReturnInfo metingToestand = fileNrMeasurementList(mFile);
        if (metingToestand.getReturnCode() == 0){
            // Metingfile lezen is gelukt en metingen zitten in List
            if (measurementList.size() > 0){
                latestMeasurement.setMeasurement(measurementList.get(measurementList.size()-1));
                for (int j = 0; j < measurementList.size() ; j++) {
                    if (latestMeasurement.getDateInt() < measurementList.get(j).getDateInt()){
                        latestMeasurement.setMeasurement(measurementList.get(j));
                    }
                }
            }else {
                // Geen metingen
            }
        }else if (metingToestand.getReturnCode() == 100){
            // Geen metingen
        }else {
            // Fout bij lezen file
        }
        return metingToestand;
    }

    public ReturnInfo fileNrMeasurementList(File metingFile){
        ReturnInfo returnInfo = new ReturnInfo(0, "");
        // Meting File lezen
        if (metingFile.exists()){
            try {
                Scanner inFile = new Scanner(metingFile);
                int i = 0;
                while (inFile.hasNext()){
                    //readMeasurement = getMetingFromFileLine(inFile.nextLine());
                    measurementList.add(new Measurement(inFile.nextLine()));
                    i++;
                }
                //latestMeasurement = measurementList.get(measurementList.size()-1);
                inFile.close();
                return returnInfo;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // Returninfo invullen
                returnInfo.setReturnCode(101);
                returnInfo.setReturnMessage("File niet gevonden !");
                return returnInfo;
            }
        }else {
            // Returninfo invullen
            returnInfo.setReturnCode(100);
            returnInfo.setReturnMessage("Er zijn nog geen metingen !");
            return returnInfo;
        }
    }

    public boolean storeMeasurements(File metingFile, List<Measurement> measurementList){
        // Sorteren
        List<Measurement> sortedMeasurements = sortMeasurements(measurementList);

        // Wegschrijven nr file
        try {
            PrintWriter outFile = new PrintWriter(metingFile);
            for (int i = 0; i < sortedMeasurements.size(); i++) {
                outFile.println(sortedMeasurements.get(i).convertToFileLine());
                //outFile.println(makeFileLine(sortedMeasurements.get(i)));
            }
            outFile.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Measurement> sortMeasurements(List<Measurement> measurementList){
        Measurement tmpMeasurement = new Measurement();
        for (int j = 0; j < measurementList.size()-1; j++) {
            for (int i = measurementList.size()-2; i >= j ; i--) {
                if (measurementList.get(i).getDateInt() < measurementList.get(i+1).getDateInt()){
                    tmpMeasurement.setMeasurement(measurementList.get(i));
                    measurementList.get(i).setMeasurement(measurementList.get(i+1));
                    measurementList.get(i+1).setMeasurement(tmpMeasurement);
                }
            }
        }
        return measurementList;
    }

/*
    public String makeFileLine(Measurement measurement){
        String fileLine = "<date><" + measurement.getMeasurementDate()
                + "><measurement><" + String.valueOf(measurement.getMeasurementValue()) + ">";
        return fileLine;
    }
*/

/*
    public Measurement getMetingFromFileLine(String fileLine){
        Measurement measurement = new Measurement();
        // fileLine splitsen in argumenten
        String[] fileLineContent = fileLine.split("<");
        for (int i = 0; i < fileLineContent.length; i++) {
            if (fileLineContent[i].matches("date.*")){
                measurement.setMeasurementDate(fileLineContent[i+1].replace(">",""));
            }
            if (fileLineContent[i].matches("measurement.*")){
                measurement.setMeasurementValue(Float.valueOf(fileLineContent[i+1].replace(">","")));
            }
        }
        return measurement;
    }
*/
}
