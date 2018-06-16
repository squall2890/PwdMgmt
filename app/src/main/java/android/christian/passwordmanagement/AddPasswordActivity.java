package android.christian.passwordmanagement;

import android.app.AlertDialog;
import android.christian.passwordmanagement.entity.Password;
import android.christian.passwordmanagement.utility.AppDatabase;
import android.christian.passwordmanagement.utility.RandomString;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPasswordActivity extends AppCompatActivity {

    private final String TAG = AddPasswordActivity.class.getName();
    private EditText edSite;
    private EditText edUrlSite;
    private EditText edUser;
    private EditText edPassword;
    private EditText edEmail;
    private EditText edNote;
    private Password password;
    private Button goToSite;
    private boolean update = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        setEditTextView();

        //Messaggio di azione completata
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String data = extras.getString("UPDATE");
            password = (Password) extras.getSerializable("PASSWORD");
            update = true;
            edSite.setText( password.getNomeSito() );
            edUrlSite.setText( password.getUrlSito() );
            edUser.setText( password.getUsername() );
            edPassword.setText( password.getPassword() );
            edEmail.setText( password.getEmail() );
            edNote.setText( password.getNote() );

            if( password.getUrlSito() == null || password.getUrlSito().isEmpty() ) {
                goToSite.setEnabled(false);
            }
        } else {
            goToSite.setVisibility(View.GONE);
        }
    }

    private void setEditTextView() {
        edSite = findViewById(R.id.newSiteName);
        edUrlSite = findViewById(R.id.newSite);
        edUser = findViewById(R.id.newUser);
        edPassword = findViewById(R.id.newPassword);
        edEmail = findViewById(R.id.newEmail);
        edNote = findViewById(R.id.newNote);
        goToSite = findViewById(R.id.goToUrl);
    }

    public void randomPassword(View view) {
        String password = Password.generateRandomPassword(12, RandomString.MEDIUM);

        Log.i(TAG, "Password generata: " + password);

        EditText editText = findViewById(R.id.newPassword);
        editText.setText(password);
    }

    public void savePassword(View view) {
        // TODO: 31/03/2018 Funzione per salvare la nuova password. Se ok torna alla Home

        String site = String.valueOf(edSite.getText());
        String siteUrl = String.valueOf(edUrlSite.getText());
        String user = String.valueOf(edUser.getText());
        String passwordValue = String.valueOf(edPassword.getText());
        String email = String.valueOf(edEmail.getText());
        String note = String.valueOf(edNote.getText());

        AppDatabase appDatabase = AppDatabase.getAppDatabase(this);

        if(update) {

            password.setNomeSito(site);
            password.setUrlSito(siteUrl);
            password.setUsername(user);
            password.setPassword(passwordValue);
            password.setEmail(email);
            password.setNote(note);
            appDatabase.passwordDao().updatePassword(password);
        } else {
            Password passwordObj = new Password(site, siteUrl, user, passwordValue, email, note);
            appDatabase.passwordDao().insertPassword(passwordObj);
        }

        appDatabase.close();
        AppDatabase.destroyInstance();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("TOAST", "La password è stata inserita/aggiornata");
        startActivity(intent);
    }

    public void cancelPassword() {
        AppDatabase db = AppDatabase.getAppDatabase(AddPasswordActivity.this);
        db.passwordDao().deletePassword(password);
        db.close();
        AppDatabase.destroyInstance();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("TOAST", "La password è stata cancellata");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void abortAction(View view) {

        if(update){

            new AlertDialog.Builder(AddPasswordActivity.this)
                    .setTitle("Vuoi Modificare i dati selezionati?")
                    .setMessage("Una volta confermata l'azione tornerai nella Home")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            cancelPassword();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            AddPasswordActivity.this.onBackPressed();
        }
    }

    public void goToSite(View view) {

        String url = password.getUrlSito();
        Intent intent = new Intent(this, WebPageActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }
}