package android.christian.passwordmanagement;

import android.app.AlertDialog;
import android.christian.passwordmanagement.adapter.ExpandableListAdapter;
import android.christian.passwordmanagement.entity.Password;
import android.christian.passwordmanagement.utility.AppDatabase;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ListPasswordActivity";

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;
    private Password[] passwords;
    private int lastExpandedPosition = -1;
    private int expandedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_password);

        initData();
        showResult();
/*
        initData();
        showResult();
        listView = findViewById(R.id.lvExpRecap);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listHash);

        Log.d(TAG, String.valueOf(listDataHeader.size()));
        Log.d(TAG, String.valueOf(listHash.size()));
        listView.setAdapter(listAdapter);

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    listView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                long packedPosition = listView.getExpandableListPosition(i);
                final int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
                if (childPosition == -1) {
                    // group wa clicked ,you can do something here.
                    Log.i(TAG, "Group is clicked");
                } else {
                    // children was clicked,you can do something here.
                    Log.i(TAG, "Child is clicked");
                    new AlertDialog.Builder(ListPasswordActivity.this)
                            .setTitle("Vuoi Modificare i dati selezionati?")
                            //                .setMessage("Do you really want to whatever?")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    goModifyData(groupPosition);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                return true;
            }
        });*/
    }

    private void goModifyData(int groupPosition) {

        Log.i(TAG, passwords[groupPosition].toString());
        Intent intent = new Intent(this, AddPasswordActivity.class);
        intent.putExtra("UPDATE", true);
        intent.putExtra("PASSWORD", passwords[groupPosition]);
        startActivity(intent);
    }

    private void initData() {

        AppDatabase db = AppDatabase.getAppDatabase(this);
        passwords = db.passwordDao().loadAllPassword();

        Log.d(TAG, String.valueOf(db.isOpen()));

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        for(int i=0; i < passwords.length; ++i) {

            String header = passwords[i].getNomeSito();
            listDataHeader.add(header);

            List<String> passwordRecord = new ArrayList<>();
            passwordRecord.add("URL: " + passwords[i].getUrlSito());
            passwordRecord.add("Username: " + passwords[i].getUsername());
            passwordRecord.add("Password: " + passwords[i].getPassword());
            passwordRecord.add("Email: " + passwords[i].getEmail());
            passwordRecord.add("Note: " + passwords[i].getNote());
            //passwordRecord.add("UUID: " + passwords[i].getUuid());
            passwordRecord.add(getString(R.string.details));

            listHash.put(listDataHeader.get(i), passwordRecord);
        }

        db.close();
        Log.d(TAG, String.valueOf(db.isOpen()));
    }

    public void searchSite(View view) {
        EditText searching = findViewById(R.id.searchSite);
        String searchValue = String.valueOf(searching.getText());
        Log.i(TAG, "Function Search\nValore: " + searchValue);

        searchValue = '%' + searchValue + '%';
        AppDatabase db = AppDatabase.getAppDatabase(this);
        passwords = db.passwordDao().getGeneralResult(searchValue);
        Log.i(TAG, "Numero di risultati: " + String.valueOf(passwords.length));

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        for(int i=0; i < passwords.length; ++i) {

            String header = passwords[i].getNomeSito();
            listDataHeader.add(header);

            List<String> passwordRecord = new ArrayList<>();
            passwordRecord.add("URL: " + passwords[i].getUrlSito());
            passwordRecord.add("Username: " + passwords[i].getUsername());
            passwordRecord.add("Password: " + passwords[i].getPassword());
            passwordRecord.add("Email: " + passwords[i].getEmail());
            passwordRecord.add("Note: " + passwords[i].getNote());
            //passwordRecord.add("UUID: " + passwords[i].getUuid());
            passwordRecord.add(getString(R.string.details));

            listHash.put(listDataHeader.get(i), passwordRecord);
        }
        db.close();

        showResult();
    }

    private void showResult() {
        listView = findViewById(R.id.lvExpRecap);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listHash);

        Log.d(TAG, String.valueOf(listDataHeader.size()));
        Log.d(TAG, String.valueOf(listHash.size()));
        listView.setAdapter(listAdapter);

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                Integer groups = listAdapter.getGroupCount();
                String log = "Number of group: " + String.valueOf(groups);
                log += "\nGROUP EXPAND LISTENER: " + String.valueOf(groupPosition);
                for(int i =0; i < groups; ++i) {
                    if(i != groupPosition)
                        listView.collapseGroup(i);
                }

                Log.i(TAG, log);
               /* expandedGroup = groupPosition;
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    listView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;*/
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,final int groupPosition, int childPosition, long id) {

                Log.i(TAG, "EVENT CLICK LISTENER ON CHILD\nPOSITION CLICKED: " + String.valueOf(childPosition));
                if(childPosition == 5) {
                    Log.i(TAG, "Child is clicked\nPosition: " + String.valueOf(childPosition));
                    new AlertDialog.Builder(ListPasswordActivity.this)
                            .setTitle("Vuoi Modificare i dati selezionati?")
                            //                .setMessage("Do you really want to whatever?")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    goModifyData(groupPosition);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                return false;
            }
        });
/*
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                long packedPosition = listView.getExpandableListPosition(i);
                final int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
                if (childPosition == -1) {
                    // group wa clicked ,you can do something here.
                    Log.i(TAG, "Group is clicked");
                } else {
                    // children was clicked,you can do something here.
                    Log.i(TAG, "Child is clicked\nPosition: " + String.valueOf(childPosition));
                    new AlertDialog.Builder(ListPasswordActivity.this)
                            .setTitle("Vuoi Modificare i dati selezionati?")
                            //                .setMessage("Do you really want to whatever?")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    goModifyData(groupPosition);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                return true;
            }
        });*/
    }

    public void editSite(View view) {

        Log.i(TAG, "CLICK EDIT SITE IN PASSWORD LIST ACTIVITY\nSELECTED POSITION: " + String.valueOf(expandedGroup));
    }
}