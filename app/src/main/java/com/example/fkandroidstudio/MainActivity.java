package com.example.fkandroidstudio;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    public static int weekLayoutHeight;

    // Create an instance of the contract
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            InputStream inputStream = null;
                            try {
                                inputStream = getContentResolver().openInputStream(uri);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            String originalFileName = getFileNameFromUri(uri);
                            // Create a new file in the internal storage
                            File file = new File(getFilesDir(), originalFileName);

                            // Create FileOutputStream
                            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                                byte[] buffer = new byte[1024];
                                int length;
                                // Read the data and write it to the new file
                                while ((length = inputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, length);
                                }
                                outputStream.close();
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                // Handle error
                            }
                        }
                    }
                    restartActivity();}
            }
    );
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public static TimeZone timeZone;
    public static Calendar calendar;
    public static File InternalStorageDir;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeZone = TimeZone.getTimeZone(TimeZone.getDefault().getID());
        calendar = DateManager.GetCalendar();
        InternalStorageDir = getFilesDir();
        DateManager dateManager = new DateManager(this);

        setContentView(R.layout.activity_main);

        TextView textViewCurrentWeek = findViewById(R.id.dateView);
        TextView textViewCurrentYear = findViewById(R.id.yearView);
        ImageButton buttonCalRight = findViewById(R.id.calButtonRight);
        ImageButton buttonCalLeft = findViewById(R.id.calButtonLeft);
        RelativeLayout randomWeekLayout = findViewById(R.id.wedLayout);

        drawerLayout = findViewById(R.id.bkg_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open_nav,R.string.close_nav);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.upload) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                // Filter to only show results that can be "opened", such as a file (as opposed to a list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // Filter to show only .ics files
                intent.setType("text/calendar");
                mGetContent.launch(intent);
                //listFilesInInternalStorage();
            } else if (item.getItemId() == R.id.settings) {
                Toast.makeText(MainActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.deleteViewButton){
                DeleteFiles();
                restartActivity();
            }
            return false;
        });

        ViewTreeObserver vto = randomWeekLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to avoid continuously getting callbacks
                randomWeekLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                weekLayoutHeight = randomWeekLayout.getHeight();
            }
        });

        Log.i("evMain", "BeforevariablesHaveBeenAsigned");
        //!!!
        dateManager.displayWeekCalendar.displayWeekEvents(calendar.getTime());
        //!!!
        Log.i("evMain", "fkme");
        textViewCurrentWeek.setText(DateShow.displayWeek(calendar));
        textViewCurrentYear.setText(DateShow.displayYear(calendar));
        Log.i("evMain", "variablesHaveBeenAsigned");

        buttonCalRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveAllViews();
                dateManager.SkipTime(textViewCurrentWeek,textViewCurrentYear,true);
            }
        });
        buttonCalLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveAllViews();
                dateManager.SkipTime(textViewCurrentWeek,textViewCurrentYear,false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    private String getFileNameFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        return cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        // If the file name is not found, generate a default name
        return "myFile.ics";
    }
    private void RemoveAllViews(){

        //un jeg de cod, il refac mai incolo

        RelativeLayout monLayout = findViewById(R.id.monLayout);
        RelativeLayout tueLayout = findViewById(R.id.tueLayout);
        RelativeLayout wedLayout = findViewById(R.id.wedLayout);
        RelativeLayout thuLayout = findViewById(R.id.thuLayout);
        RelativeLayout friLayout = findViewById(R.id.friLayout);
        RelativeLayout satLayout = findViewById(R.id.satLayout);
        RelativeLayout sunLayout = findViewById(R.id.sunLayout);
        monLayout.removeAllViews();
        tueLayout.removeAllViews();
        wedLayout.removeAllViews();
        thuLayout.removeAllViews();
        friLayout.removeAllViews();
        satLayout.removeAllViews();
        sunLayout.removeAllViews();
    }

    private void restartActivity() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    // Handle permission request result

    // Delete files from the specified directory
    private void DeleteFiles() {
        String directoryPath = InternalStorageDir.toString();

        File directory = new File(directoryPath);

        if (directory.exists()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    file.delete();
                }

                Toast.makeText(this, "Files deleted successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No files to delete.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Directory does not exist.", Toast.LENGTH_SHORT).show();
        }
    }
}