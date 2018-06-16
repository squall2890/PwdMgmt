package android.christian.passwordmanagement;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = "MainActivity";

    private static final String[] READ_WRITE_EXT_STORAGE =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getStoragePermissions();
    }

    public void LaunchNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                MainActivity.this.finish();
            }
        }, 1500);
    }

    private boolean hasStoragePermissions() {
        return EasyPermissions.hasPermissions(this, READ_WRITE_EXT_STORAGE);
    }

    public void getStoragePermissions() {

        if (hasStoragePermissions()) {
            // Have permissions, do the thing!
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.d(TAG, "No SDCARD");
            } else {
                creteDirectories();
            }
        } else {

            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_access_external_storage),
                    123,
                    READ_WRITE_EXT_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        LaunchNextActivity();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        creteDirectories();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    public void creteDirectories() {

        String log = "Creazione Directories dell'applicazione";
        boolean result = false;

        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "MyAppFolderTesting");
        result = directory.mkdirs();
        log += "\nDirectory di Test: " + String.valueOf(result);

        File rootDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Password Management");
        if( !rootDirectory.isDirectory() )  result = rootDirectory.mkdirs();
        log += "\nDirectory di Root: " + String.valueOf(result);

        File systemPath = new File(Environment.getExternalStorageDirectory() + File.separator + "Password Management/system");
        if( !systemPath.isDirectory() ) result = systemPath.mkdirs();
        log += "\nDirectory Password Management/system: " + String.valueOf(result);

        File backupPath = new File(Environment.getExternalStorageDirectory() + File.separator + "Password Management/backup");
        if( !backupPath.isDirectory() )  result = backupPath.mkdirs();
        log += "\nDirectory Password Management/backup: " + String.valueOf(result);

        File importBackupPath = new File(Environment.getExternalStorageDirectory() + File.separator + "Password Management/backup/import");
        if( !importBackupPath.isDirectory() )  result = importBackupPath.mkdirs();
        log += "\nDirectory Password Management/backup/import: " + String.valueOf(result);

        File exportBackupPath = new File(Environment.getExternalStorageDirectory() + File.separator + "Password Management/backup/export");
        if( !exportBackupPath.isDirectory() )  result = exportBackupPath.mkdirs();
        log += "\nDirectory Password Management/backup/import: " + String.valueOf(result);

        LaunchNextActivity();

        Log.d(TAG, log);
    }
}