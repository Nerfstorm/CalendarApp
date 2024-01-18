package com.example.fkandroidstudio;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

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
    public static int weekLayoutHeight;
    private static final int PICK_FILE_REQUEST_CODE = 200;


    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        InputStream inputStream;
                        try {
                            inputStream = getContentResolver().openInputStream(Objects.requireNonNull(uri));
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
                            while ((length = Objects.requireNonNull(inputStream).read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }
                            AddFile addFile = new AddFile(file,this);
                            Thread addFileThread = new Thread(addFile);
                            addFileThread.start();

                            outputStream.close();
                            inputStream.close();
                            addFileThread.join();
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle error
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                restartActivity();}
    );
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public static TimeZone timeZone;
    public static Calendar calendar;
    public static File InternalStorageDir;

    public static CalendarManager calendarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeZone = TimeZone.getTimeZone(TimeZone.getDefault().getID());
        calendar = CalendarManagerUtility.GetCalendar();
        InternalStorageDir = getFilesDir();

        // DateManager dateManager = new DateManager(this);

        calendarManager = new CalendarManager(this);
        calendarManager.Initialize();

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
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        else Log.e("MainActivity", "Action bar is null");

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
                //for debugging
                TableQueries tableQueries = new TableQueries(this, 0);
                Thread thread = new Thread(tableQueries);
                thread.start();

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

        calendarManager.IterateMapForSpecWeek(calendar.getTime());

        textViewCurrentWeek.setText(CalendarManagerUtility.DisplayWeek(calendar));
        textViewCurrentYear.setText(CalendarManagerUtility.DisplayYear(calendar));

        buttonCalRight.setOnClickListener(v -> {
            RemoveAllViews();
            calendarManager.SkipTime(textViewCurrentWeek,textViewCurrentYear,true);
        });
        buttonCalLeft.setOnClickListener(v -> {
            RemoveAllViews();
            calendarManager.SkipTime(textViewCurrentWeek,textViewCurrentYear,false);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            Log.d("main", "Activity Result used");
            // Call your function to process the selected file
            File icsFile = uriToFile(fileUri);
            //calendarDbMgr.AddFileToDatabase(icsFile);

        }
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

        //Garbage code, needs to be rewritten later

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
        Objects.requireNonNull(intent,"Intent is null").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private File uriToFile(Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
            cursor.close();
        }

        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }
    // Handle permission request result

    // Delete files from the specified directory
    private void DeleteFiles() {
        String directoryPath = InternalStorageDir.toString();

        File directory = new File(directoryPath);

        TableQueries tableQueries = new TableQueries(this, 99);
        Thread thread = new Thread(tableQueries);
        thread.start();

        if (directory.exists()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) if(!file.delete()) Log.e("files","Files have not been deleted");
                Toast.makeText(this, "Files deleted successfully.", Toast.LENGTH_SHORT).show();

            } else Toast.makeText(this, "No files to delete.", Toast.LENGTH_SHORT).show();

        } else Toast.makeText(this, "Directory does not exist.", Toast.LENGTH_SHORT).show();

    }
}