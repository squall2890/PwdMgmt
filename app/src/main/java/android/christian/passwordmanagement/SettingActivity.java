package android.christian.passwordmanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.christian.passwordmanagement.entity.Password;
import android.christian.passwordmanagement.utility.AppDatabase;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";

    //Setting Dialog File Explorer
    private File root;
    private File curFolder;
    private ListView dialog_ListView;
    private Button buttonUp;
    private TextView textFolder;
    private List<String> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;
    }

    public void exportData(View view) {

        Calendar currentTime = Calendar.getInstance();
        String fileName = currentTime.get(Calendar.YEAR) + "-" + (currentTime.get(Calendar.MONTH) + 1) + "-" + currentTime.get(Calendar.DAY_OF_MONTH) +
                "T" + currentTime.get(Calendar.HOUR_OF_DAY) + ":" + currentTime.get(Calendar.MINUTE) +"_export.csv";
        String xmlFile = root + "/Password Management/backup/export/" + fileName;
        Toast toast;
        int duration = Toast.LENGTH_LONG;
        String toast_message = null;

        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(xmlFile), '|', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "\n");
            AppDatabase db = AppDatabase.getAppDatabase(this);
            Password[] passwords = db.passwordDao().loadAllPassword();
            db.close();
            AppDatabase.destroyInstance();
            List<String[]> csvData = new ArrayList<>();

            for(int i=0; i < passwords.length; ++i) {
//                Log.d(TAG, "Riga esportata: (" + String.valueOf(passwords[i].toCSVString().length) + ") " + passwords[i].toCSVString().toString());
                csvData.add(passwords[i].toCSVString());
            }
            csvWriter.writeAll(csvData);
            csvWriter.close();
            toast_message = "Export eseguito correttamente\n" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            toast_message = "Errore nella creazione del file di backup";
        } finally {

            toast = Toast.makeText(this, toast_message, duration);
            toast.show();
        }
    }

    public void showImportPicker(View view) {
//        Log.i(TAG, "import data");

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.explorefiledialog);
        dialog.setTitle("Select Backup File");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        textFolder = dialog.findViewById(R.id.folder);
        buttonUp = dialog.findViewById(R.id.up);
        buttonUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ListDir(curFolder.getParentFile());
            }
        });
        dialog_ListView = dialog.findViewById(R.id.dialoglist);
        dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File selected = new File(curFolder.getPath() + "/" + fileList.get(position));
                if(selected.isDirectory()){
                    ListDir(selected);
                }else {
                    importData(selected);
                    dialog.dismiss();
                }
            }
        });

        ListDir(curFolder);
        dialog.show();
    }

    private void importData(File name) {

        int duration = Toast.LENGTH_LONG;
        String toast_message = null;
        String xmlFile = name.toString();

        String log = "";
        String site, note, username, pwd, email, url;
        int countPassword = 0;
        Password temp_password;
        List<Password> temp_passwords;

        try {

            final CSVParser parser = new CSVParserBuilder().withSeparator('|').withIgnoreQuotations(true).build();
            CSVReader reader = new CSVReaderBuilder(new FileReader(xmlFile)).withCSVParser(parser).build();

            List<String[]> passwordList = reader.readAll();
            AppDatabase db = AppDatabase.getAppDatabase(this);

            for(String[] record : passwordList) {
                site = (record[0].isEmpty()) ? "" : record[0];
                url = (record[1].isEmpty()) ? "" : record [1];
                username = (record[2].isEmpty()) ? "" : record[2];
                pwd = (record[3].isEmpty()) ? "" : record[3];
                email = (record[4].isEmpty()) ? "" : record[4];
                note = (record[5].isEmpty()) ? "" : record[5];

                temp_passwords = db.passwordDao().getPasswordForSiteUser(record[0], record[1]);

                if( temp_passwords.size() == 0 ) {
                    temp_password = new Password(site, url, username, pwd, email, note);
                    db.passwordDao().insertPassword( temp_password );
                    countPassword++;
                }
            }

            db.close();
            AppDatabase.destroyInstance();

            toast_message = "Import completato. Totale Password importate: " + String.valueOf(countPassword);

            Log.d(TAG, "Totale password: " + countPassword);
            Log.d(TAG, log);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(this, toast_message, duration).show();
        }
    }

    void ListDir(File f){

        if(f.equals(root)){
            buttonUp.setEnabled(false);
        }else{
            buttonUp.setEnabled(true);
        }

        curFolder = f;
        //textFolder.setText(f.getPath());
        textFolder.setText(f.getName());
        String log = "Sub Folder:\n";

        File[] files = f.listFiles();
        fileList.clear();
        for (File file : files){
//            fileList.add(file.getPath());
            fileList.add(file.getName());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList);
        dialog_ListView.setAdapter(directoryList);
    }

    public void deleteAllData(View view) {

        new AlertDialog.Builder(this)
                .setTitle("Vuoi Cancellare tutti i dati?")
                .setMessage("Attenzione questa azione è irreversibile")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        eraseData();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void eraseData() {

        String toastMessage;
        AppDatabase appDatabase = AppDatabase.getAppDatabase(this);
        Password[] passwords = appDatabase.passwordDao().loadAllPassword();

        if( passwords.length > 0 ) {
            appDatabase.passwordDao().deletePassword(passwords);
            toastMessage = "Dati Cancellati!";
        } else {
            toastMessage = "Non ci sono dati da cancellare";
        }

        appDatabase.close();
        AppDatabase.destroyInstance();

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }
}