package android.christian.passwordmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button button = findViewById(R.id.devButton);
        button.setVisibility(View.INVISIBLE);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        //Messaggio di azione completata
        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            String data = extras.getString("TOAST");
            if( data != null && !data.isEmpty()) {
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(this, data, duration);
                toast.show();
            }
        }
    }

    public void addPassword(View view) {

        Intent intent = new Intent(this, AddPasswordActivity.class);
        startActivity(intent);
    }

    public void showList(View view) {
        Log.i(TAG, "Show List Function");
        Intent intent = new Intent(this, ListPasswordActivity.class);
        startActivity(intent);
    }

    public void goToSetting(View view) {
        Log.i(TAG, "goToSetting Function");
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    public void goToDev(View view) {
        Intent intent = new Intent(this, DevActivity.class);
        startActivity(intent);
    }
}