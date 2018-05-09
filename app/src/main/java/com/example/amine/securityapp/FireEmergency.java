package com.example.amine.securityapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class FireEmergency extends AppCompatActivity {

    private ImageView lessons, games, videos, paint;
    private Button texttrr;


    final Context c = this;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String selection;
    Uri uri;
    Intent CropIntent;

    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_emergency);

        games = (ImageView) findViewById(R.id.games);
        videos = (ImageView) findViewById(R.id.videos);
        paint = (ImageView) findViewById(R.id.paint);
        lessons = (ImageView) findViewById(R.id.lessons);
        texttrr = (Button) findViewById(R.id.texttrr);




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Fire emergency");

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FireEmergency.this, videoRecording.class);
                FireEmergency.this.startActivity(intent);
            }
        });

        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FireEmergency.this, audioRecording.class);
                FireEmergency.this.startActivity(intent);

            }
        });


        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FireEmergency.this, MapsActivity.class);
                FireEmergency.this.startActivity(intent);

            }
        });

        lessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FireEmergency.this, cameraRecording.class);
                FireEmergency.this.startActivity(intent);

            }
        });


        texttrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "911";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);

            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {



                Toast.makeText(FireEmergency.this, "Nearby Fire stations have been alerted", Toast.LENGTH_SHORT).show();



            }});
    }



    public void languages() {

        listItems = getResources().getStringArray(R.array.Languages);
        checkedItems = new boolean[listItems.length];

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose a language...").setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                switch (arg1) {

                    case 0:
                        selection = listItems[arg1];

                        LocaleHelper.setLocale(FireEmergency.this, "en");
                        FireEmergency.this.recreate();

                        Toast.makeText(getApplicationContext(), "English language chosen!",
                                Toast.LENGTH_LONG).show();

                        break;

                    case 1:
                        selection = listItems[arg1];

                        LocaleHelper.setLocale(FireEmergency.this, "fr");
                        FireEmergency.this.recreate();

                        Toast.makeText(getApplicationContext(), "French language chosen!",
                                Toast.LENGTH_LONG).show();

                        break;

                    case 2:
                        selection = listItems[arg1];

                        LocaleHelper.setLocale(FireEmergency.this, "ar");
                        FireEmergency.this.recreate();

                        Toast.makeText(getApplicationContext(), "Arabic language chosen!",
                                Toast.LENGTH_LONG).show();

                        break;

                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {



            case R.id.home:

                Intent intent = new Intent(FireEmergency.this, MainActivity.class);
                FireEmergency.this.startActivity(intent);

                return true;

            case R.id.profile1:

                intent = new Intent(FireEmergency.this, ProfilePage.class);
                FireEmergency.this.startActivity(intent);

                return true;


            case R.id.flashlight:

                AlertDialog.Builder builder = new AlertDialog.Builder(FireEmergency.this);
                builder.setTitle("Security");
                builder.setMessage("Flash light !!!");
                builder.setCancelable(false);

                builder.setPositiveButton("ON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                            String cameraId = null; // Usually back camera is at 0 position.
                            try {
                                cameraId = camManager.getCameraIdList()[0];
                                camManager.setTorchMode(cameraId, true);   //Turn ON
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

                builder.setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                            String cameraId = null; // Usually back camera is at 0 position.
                            try {
                                cameraId = camManager.getCameraIdList()[0];
                                camManager.setTorchMode(cameraId, false);   //Turn off
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                Dialog dialog = builder.create();
                dialog.show();


                return true;


            case R.id.info:

                intent = new Intent(FireEmergency.this, Info.class);
                FireEmergency.this.startActivity(intent);

                return true;


            case R.id.tips:

                intent = new Intent(FireEmergency.this, SafetyTips.class);
                FireEmergency.this.startActivity(intent);

                return true;

            case R.id.exit:

                listItems = getResources().getStringArray(R.array.Languages);
                checkedItems = new boolean[listItems.length];

                builder = new AlertDialog.Builder(FireEmergency.this);
                builder.setTitle("MSecurity");
                builder.setMessage("Exit !");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        System.exit(0);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog = builder.create();
                dialog.show();


                return true;

            case R.id.languages:

                listItems = getResources().getStringArray(R.array.Languages);
                checkedItems = new boolean[listItems.length];

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle("Choose a language...").setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        switch (arg1) {

                            case 0:

                                selection = listItems[arg1];

                                LocaleHelper.setLocale(FireEmergency.this, "en");
                                FireEmergency.this.recreate();
                                break;

                            case 1:

                                selection = listItems[arg1];

                                LocaleHelper.setLocale(FireEmergency.this, "fr");
                                FireEmergency.this.recreate();
                                break;

                            case 2:
                                selection = listItems[arg1];

                                LocaleHelper.setLocale(FireEmergency.this, "ar");
                                FireEmergency.this.recreate();
                                break;


                        }
                    }
                })

                        .setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                AlertDialog mdialog = mBuilder.create();
                mdialog.show();



                return true;


            default:
                return super.onOptionsItemSelected(item);

        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
