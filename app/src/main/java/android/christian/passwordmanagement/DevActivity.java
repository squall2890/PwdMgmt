package android.christian.passwordmanagement;

import android.app.Dialog;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DevActivity extends AppCompatActivity {

    private final static String TAG = "DevActivity";
    Button filePickerButton;
    TextView filePickerText;

    //Setting Dialog File Explorer
    File root;
    File curFolder;
    ListView dialog_ListView;
    Button buttonUp;
    TextView textFolder;

    private List<String> fileList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        filePickerButton = findViewById(R.id.filePickerButton);
        filePickerText = findViewById(R.id.filePickertText);
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;
    }

     public void showFilePicker(View view) {

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
                    Toast.makeText(DevActivity.this,selected.toString() + " selected", Toast.LENGTH_LONG).show();
                    filePickerText.setText(selected.getName());
                    dialog.dismiss();
                }
            }
        });

        ListDir(curFolder);
        dialog.show();
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


}
