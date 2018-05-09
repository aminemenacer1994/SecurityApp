package com.example.amine.securityapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amine.securityapp.Manifest.permission;

import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.R.id.message;
import static com.example.amine.securityapp.Manifest.permission.*;

public class Main2Activity extends AppCompatActivity {

    //http://www.codebind.com/android-tutorials-and-examples/android-sqlite-tutorial-example/
    //https://stackoverflow.com/questions/5271448/how-to-detect-shake-event-with-android

    private ImageView lessons, games, videos, paint, button;
    private Button em_btn;

    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 234;


    final Context c = this;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String selection;
    Uri uri;
    Intent CropIntent;

    MediaPlayer mySound;


    private int sounds;
    private int[] sounds_num = { R.raw.alarm};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        lessons = (ImageView) findViewById(R.id.lessons);
        games = (ImageView) findViewById(R.id.games);
        videos = (ImageView) findViewById(R.id.videos);
        paint = (ImageView) findViewById(R.id.paint);
        em_btn = (Button) findViewById(R.id.em_btn);
        button = (ImageView) findViewById(R.id.button);


        lessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                Main2Activity.this.startActivity(intent);

            }
        });

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Main2Activity.this, MedicalEmergency.class);
                Main2Activity.this.startActivity(intent);

            }
        });

        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Main2Activity.this, EmergencyContacts.class);
                Main2Activity.this.startActivity(intent);

            }
        });

        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Main2Activity.this, FireEmergency.class);
                Main2Activity.this.startActivity(intent);

            }
        });

        em_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = "911";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);

            }
        });


        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                Toast.makeText(Main2Activity.this, "Message sent to all emergency contacts", Toast.LENGTH_SHORT).show();

                sms();

                mySound = MediaPlayer.create(getApplicationContext(), sounds_num[sounds]);
                mySound.start();

                return false;
            }


        });




        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {



                Toast.makeText(Main2Activity.this, "Message sent to the police", Toast.LENGTH_SHORT).show();

                sms();

            }});

    }


    public void sms(){

        String uri = "http://maps.google.com/maps?saddr=";

        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append(Uri.parse(uri));
        smsManager.sendTextMessage("07871594836", null, smsBody.toString(), null, null);

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

                        LocaleHelper.setLocale(Main2Activity.this, "en");
                        Main2Activity.this.recreate();

                        Toast.makeText(getApplicationContext(), "English language chosen!",
                                Toast.LENGTH_LONG).show();

                        break;

                    case 1:
                        selection = listItems[arg1];

                        LocaleHelper.setLocale(Main2Activity.this, "fr");
                        Main2Activity.this.recreate();

                        Toast.makeText(getApplicationContext(), "French language chosen!",
                                Toast.LENGTH_LONG).show();

                        break;

                    case 2:
                        selection = listItems[arg1];

                        LocaleHelper.setLocale(Main2Activity.this, "ar");
                        Main2Activity.this.recreate();

                        Toast.makeText(getApplicationContext(), "Arabic language chosen!",
                                Toast.LENGTH_LONG).show();

                        break;

                }
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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

                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                Main2Activity.this.startActivity(intent);

                return true;

            case R.id.profile1:

                intent = new Intent(Main2Activity.this, ProfilePage.class);
                Main2Activity.this.startActivity(intent);

                return true;




            case R.id.flashlight:

                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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


            case R.id.instructions:


                builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setTitle("MSecurity");
                builder.setMessage("Shake phone to send a message to the police with your current location." +
                        "              Hold on the panic button to send message to all emergency contacts with your current location and activate an alarm.  ");

                builder.setCancelable(false);

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog = builder.create();
                dialog.show();



                return true;


            case R.id.info:

                intent = new Intent(Main2Activity.this, Info.class);
                Main2Activity.this.startActivity(intent);

                return true;


            case R.id.tips:

                intent = new Intent(Main2Activity.this, SafetyTips.class);
                Main2Activity.this.startActivity(intent);

                return true;

            case R.id.exit:

                intent = new Intent(Main2Activity.this, LoginPage.class);
                Main2Activity.this.startActivity(intent);

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

                                LocaleHelper.setLocale(Main2Activity.this, "en");
                                Main2Activity.this.recreate();
                                break;

                            case 1:

                                selection = listItems[arg1];

                                LocaleHelper.setLocale(Main2Activity.this, "fr");
                                Main2Activity.this.recreate();
                                break;

                            case 2:
                                selection = listItems[arg1];

                                LocaleHelper.setLocale(Main2Activity.this, "ar");
                                Main2Activity.this.recreate();
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
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }



}
