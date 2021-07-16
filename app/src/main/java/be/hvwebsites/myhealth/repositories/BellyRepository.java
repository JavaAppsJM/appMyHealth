package be.hvwebsites.myhealth.repositories;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import be.hvwebsites.myhealth.entities.Belly;
import be.hvwebsites.myhealth.returninfo.ReturnInfo;

public class BellyRepository {
    private Belly latestBelly;
    private Belly readBelly;
    private List<Belly> tmpBellyList;

    public BellyRepository(){
        tmpBellyList = new ArrayList<>();
    }

    public ReturnInfo initializeRepository(File bellyFile){
        ReturnInfo bellyToestand = fileNrBellyList(bellyFile);
        if (bellyToestand.getReturnCode() == 0){
            // Bellyfile lezen is gelukt en bellies zitten in bellyList
            // Bepaal latestbelly
            latestBelly = readBelly;
            for (int j = 0; j < tmpBellyList.size() ; j++) {
                if (latestBelly.getDateInt() < tmpBellyList.get(j).getDateInt()){
                    latestBelly = tmpBellyList.get(j);
                }
            }
        }else if (bellyToestand.getReturnCode() == 100){
            // Geen bellies
        }else {
            // Fout bij lezen file
        }
        return bellyToestand;
    }

    public ReturnInfo fileNrBellyList(File bellyFile){
        ReturnInfo returnInfo = new ReturnInfo(0);
        // Belly File lezen
        if (bellyFile.exists()){
            try {
                Scanner inFile = new Scanner(bellyFile);
                int i = 0;
                while (inFile.hasNext()){
                    readBelly = getBellyFromFileLine(inFile.nextLine());
                    tmpBellyList.add(readBelly);
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
            returnInfo.setReturnMessage("Er zijn nog geen bellies !");
            return returnInfo;
        }
    }

    public List<Belly> getTBellyList() {
        return tmpBellyList;
    }

    public Belly getBellyFromFileLine(String fileLine){
        Belly belly = new Belly();
        // fileLine splitsen in belly argumenten
        String[] fileLineContent = fileLine.split("<");
        for (int i = 0; i < fileLineContent.length; i++) {
            if (fileLineContent[i].matches("date.*")){
                belly.setDate(fileLineContent[i+1].replace(">",""));
            }
            if (fileLineContent[i].matches("bellyRadius.*")){
                belly.setBellyRadius(Float.valueOf(fileLineContent[i+1].replace(">","")));
            }
        }
        return belly;
    }

    public Belly getLatestBelly(){
        return latestBelly;
    }

    public boolean storeBellies(File bellyFile, List<Belly> bellyList){
        // Sorteren bellies
        List<Belly> sortedBellies = sortBellies(bellyList);

        // Wegschrijven nr file
        try {
            PrintWriter outFile = new PrintWriter(bellyFile);
            for (int i = 0; i < sortedBellies.size(); i++) {
                outFile.println(makeFileLine(sortedBellies.get(i)));
            }
            outFile.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Belly> sortBellies(List<Belly> bellies){
        Belly tmpBelly = new Belly();
        for (int j = 0; j < bellies.size()-1; j++) {
            for (int i = bellies.size()-2 ; i >= j ; i--) {
                if (bellies.get(i).getDateInt() < bellies.get(i+1).getDateInt()){
                    tmpBelly.setBelly(bellies.get(i));
                    bellies.get(i).setBelly(bellies.get(i+1));
                    bellies.get(i+1).setBelly(tmpBelly);
                }
            }
        }
        return bellies;
    }

    public String makeFileLine(Belly belly){
        String fileLine = "<date><" + belly.getDate()
                + "><bellyRadius><" + String.valueOf(belly.getBellyRadius()) + ">";
        return fileLine;
    }

}
