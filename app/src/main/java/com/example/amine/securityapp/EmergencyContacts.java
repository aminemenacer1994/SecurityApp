package com.example.amine.securityapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.net.Proxy.Type.HTTP;

public class EmergencyContacts extends AppCompatActivity {

    ImageView lessons, games, videos, paint;
    SQLiteHelper myDb;

    final Context c = this;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String selection;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        myDb = new SQLiteHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Emergency contacts");

        lessons = (ImageView)findViewById(R.id.lessons);
        games = (ImageView)findViewById(R.id.games);
        videos = (ImageView)findViewById(R.id.videos);
        paint = (ImageView)findViewById(R.id.paint);

        lessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //view contacts
                viewAll();
            }
        });

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmergencyContacts.this, MonitorSafety.class);
                EmergencyContacts.this.startActivity(intent);
            }
        });

        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send to contacts
                sms();
                //email();
                Toast.makeText(EmergencyContacts.this, "Text message and email have been sent to all emergency contacts", Toast.LENGTH_SHORT).show();

            }


        });

        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmergencyContacts.this, Messaging.class);
                EmergencyContacts.this.startActivity(intent);
            }
        });

    }


    Uri attachment;
    String[] addresses;
    String subject;



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void sms() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", "Help, im in danger");
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
/*
    public void email() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
*/



    public void viewAll() {
        lessons.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("id :"+ res.getString(0)+"\n");
                            buffer.append("Name :"+ res.getString(1)+"\n");
                            buffer.append("email :"+ res.getString(2)+"\n");
                            buffer.append("password :"+ res.getString(3)+"\n\n");
                        }

                        // Show all data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
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

                Intent intent = new Intent(EmergencyContacts.this, MainActivity.class);
                EmergencyContacts.this.startActivity(intent);

                return true;

            case R.id.profile1:

                intent = new Intent(EmergencyContacts.this, ProfilePage.class);
                EmergencyContacts.this.startActivity(intent);

                return true;


            case R.id.flashlight:

                AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyContacts.this);
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

                intent = new Intent(EmergencyContacts.this, Info.class);
                EmergencyContacts.this.startActivity(intent);

                return true;


            case R.id.tips:

                intent = new Intent(EmergencyContacts.this, SafetyTips.class);
                EmergencyContacts.this.startActivity(intent);

                return true;

            case R.id.exit:

                listItems = getResources().getStringArray(R.array.Languages);
                checkedItems = new boolean[listItems.length];

                builder = new AlertDialog.Builder(EmergencyContacts.this);
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

                                LocaleHelper.setLocale(EmergencyContacts.this, "en");
                                EmergencyContacts.this.recreate();
                                break;

                            case 1:

                                selection = listItems[arg1];

                                LocaleHelper.setLocale(EmergencyContacts.this, "fr");
                                EmergencyContacts.this.recreate();
                                break;

                            case 2:
                                selection = listItems[arg1];

                                LocaleHelper.setLocale(EmergencyContacts.this, "ar");
                                EmergencyContacts.this.recreate();
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




}
