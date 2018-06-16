package android.christian.passwordmanagement;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class WebPageActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = "WebPageActivity";

    private static final String INTERNET = Manifest.permission.INTERNET;
    private String url;
    private String errorUrl = "L'URL non è valido";
    private String errorGrant = "Per visualizzare la pagina devi dare i permessi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            url = extras.getString("URL");
            Log.d(TAG, "URL: " + url);
            getInternetPermission();

        } else {
            redirectTo();
            Toast.makeText(this, errorUrl, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    private boolean hasInternetPermissions() {
        return EasyPermissions.hasPermissions(this, INTERNET);
    }

    public void getInternetPermission() {

        if (hasInternetPermissions()) {
            viewPage();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_access_external_storage), 2000, INTERNET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        viewPage();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        viewPage();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
        //verificare se va bene il redirect
    }

    private void viewPage() {

        WebView myWebView = findViewById(R.id.webView);

        if(!url.startsWith("http://"))
            url = "http://" + url;

        myWebView.loadUrl(url);
    }

    private void redirectTo() {
//        Intent intent = new Intent(this, this);
        finish();
    }
}
