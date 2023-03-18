package be.hvwebsites.myhealth.repositories;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import be.hvwebsites.myhealth.helpers.ReturnInfo;

public class CookieRepository {
    private List<Cookie> cookieList = new ArrayList<>();
    File cookieFile;
    public static final String COOKIE_FILE = "cookie.txt";
    public static final int COOKIE_NOT_FOUND = 99999;

    public CookieRepository(String basedir) {
        // File definitie
        cookieFile = new File(basedir, COOKIE_FILE);
        // Cookies ophalen
        readCookies();
    }

    public String getCookieValueFromLabel(String label){
        return cookieList.get(bestaatCookie(label)).getCookieValue();
    }

    public List<Cookie> getCookieList() {
        return cookieList;
    }

    public void addCookie(Cookie cookie){
        // opgegeven cookie wordt toegevoegd in de cookieList en bewaard
        int cookieIndex = bestaatCookie(cookie.getCookieLabel());
        if (cookieIndex != COOKIE_NOT_FOUND){
            cookieList.remove(cookieIndex);
        }
        cookieList.add(cookie);
        // Wegschrijven nr file
        writeCookies();
    }

    public void deleteCookie(String label){
        // cookie met opgegeven label wordt gedelete uit de cookieList en bewaard
        cookieList.remove(bestaatCookie(label));
        // Wegschrijven nr file
        writeCookies();
    }

     private int bestaatCookie(String label){
        if (cookieList != null){
            for (int i = 0; i < cookieList.size(); i++) {
                if (cookieList.get(i).getCookieLabel().equals(label)){
                    return i;
                }
            }
        }
        return COOKIE_NOT_FOUND;
    }

    public ReturnInfo readCookies(){
        ReturnInfo returnInfo = new ReturnInfo(0, "");
        // Cookie File lezen
        if (cookieFile.exists()){
            try {
                Scanner inFile = new Scanner(cookieFile);
                int i = 0;
                while (inFile.hasNext()){
                    cookieList.add(getCookieFromFileLine(inFile.nextLine()));
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
            returnInfo.setReturnMessage("Er zijn nog geen cookies !");
            return returnInfo;
        }
    }

    public Cookie getCookieFromFileLine(String fileLine){
        Cookie cookie = new Cookie();
        // fileLine splitsen in argumenten
        String[] fileLineContent = fileLine.split("<");
        for (int i = 0; i < fileLineContent.length; i++) {
            // TODO: date.* in een static zetten
            if (fileLineContent[i].matches("label.*")){
                cookie.setCookieLabel(fileLineContent[i+1].replace(">",""));
            }
            // TODO: measurement.* in een static zetten
            if (fileLineContent[i].matches("value.*")){
                cookie.setCookieValue((fileLineContent[i+1].replace(">","")));
            }
        }
        return cookie;
    }

    public boolean writeCookies(){
        try {
            PrintWriter outFile = new PrintWriter(cookieFile);
            for (int i = 0; i < cookieList.size(); i++) {
                outFile.println(makeFileLine(cookieList.get(i)));
            }
            outFile.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String makeFileLine(Cookie cookie){
        // TODO: date.* en measurement.* in een static zetten
        String fileLine = "<label><" + cookie.getCookieLabel()
                + "><value><" + cookie.getCookieValue() + ">";
        return fileLine;
    }

}
