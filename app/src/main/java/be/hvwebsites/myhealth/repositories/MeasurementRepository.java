package be.hvwebsites.myhealth.repositories;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.returninfo.ReturnInfo;

public class MeasurementRepository {
    private Measurement latestMeasurement;
    private Measurement readMeasurement;
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
            // Bepaal latest meting
            latestMeasurement = readMeasurement;
            for (int j = 0; j < measurementList.size() ; j++) {
                if (latestMeasurement.getDateInt() < measurementList.get(j).getDateInt()){
                    latestMeasurement = measurementList.get(j);
                }
            }
        }else if (metingToestand.getReturnCode() == 100){
            // Geen metingen
        }else {
            // Fout bij lezen file
        }
        return metingToestand;
    }

    public ReturnInfo fileNrMeasurementList(File metingFile){
        ReturnInfo returnInfo = new ReturnInfo(0);
        // Meting File lezen
        if (metingFile.exists()){
            try {
                Scanner inFile = new Scanner(metingFile);
                int i = 0;
                while (inFile.hasNext()){
                    readMeasurement = getMetingFromFileLine(inFile.nextLine());
                    measurementList.add(readMeasurement);
                    i++;
                }
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

    public Measurement getMetingFromFileLine(String fileLine){
        Measurement measurement = new Measurement();
        // fileLine splitsen in argumenten
        String[] fileLineContent = fileLine.split("<");
        for (int i = 0; i < fileLineContent.length; i++) {
            // TODO: date.* in een static zetten
            if (fileLineContent[i].matches("date.*")){
                measurement.setMeasurementDate(fileLineContent[i+1].replace(">",""));
            }
            // TODO: measurement.* in een static zetten
            if (fileLineContent[i].matches("measurement.*")){
                measurement.setMeasurementValue(Float.valueOf(fileLineContent[i+1].replace(">","")));
            }
        }
        return measurement;
    }

    public boolean storeMeasurements(File metingFile, List<Measurement> measurementList){
        // Sorteren
        List<Measurement> sortedMeasurements = sortMetingen(measurementList);

        // Wegschrijven nr file
        try {
            PrintWriter outFile = new PrintWriter(metingFile);
            for (int i = 0; i < sortedMeasurements.size(); i++) {
                outFile.println(makeFileLine(sortedMeasurements.get(i)));
            }
            outFile.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Measurement> sortMetingen(List<Measurement> measurementList){
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

    public String makeFileLine(Measurement measurement){
        // TODO: date.* en measurement.* in een static zetten
        String fileLine = "<date><" + measurement.getMeasurementDate()
                + "><measurement><" + String.valueOf(measurement.getMeasurementValue()) + ">";
        return fileLine;
    }

}
