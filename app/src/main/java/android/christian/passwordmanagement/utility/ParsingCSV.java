package android.christian.passwordmanagement.utility;

import android.christian.passwordmanagement.entity.Password;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParsingCSV {

    private String filepath;
    private static final String TAG = "ParsingCSV";
    private Password password;

    public ParsingCSV(String filepath) {
        this.filepath = filepath;
    }

    public List read() throws IOException {
        List resultList = new ArrayList();
        String log = "";
        String site = "";
        String note = "";
        String username = "";
        String pwd = "";
        String email = "";

        BufferedReader reader = new BufferedReader(new FileReader(filepath));

        String csvLine;
        while ((csvLine = reader.readLine()) != null) {

            String[] row = csvLine.split("\\|", -1);

            //log += Arrays.toString(row) + "\t(" + String.valueOf(row.length) + ")\n";
            for(int i=0; i < row.length ; ++i) {

                site = (row[0].isEmpty()) ? "" : row[0];
                username = (row[1].isEmpty()) ? "" : row[1];
                pwd = (row[2].isEmpty()) ? "" : row[2];
                email = (row[3].isEmpty()) ? "" : row[3];
                note = (row[4].isEmpty()) ? "" : row[4];

            }

            password = new Password(site, "", username, pwd, email, note);
            resultList.add(password);
        }

        log += "\nNumero record: " + String.valueOf(resultList.size());
        log += "\nExample: " + resultList.get(0).toString();
        log += "\nExample: " + resultList.get(59).toString();

        Log.d(TAG, log);
        return resultList;
    }
    
    public boolean write(){
        // TODO: 10/04/2018 funzione per la creazione di un file csv di backup
        return true;
    }
}