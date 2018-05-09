package com.example.amine.securityapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


public class videoRecording extends AppCompatActivity {


    static final int REQUEST_VIDEO_CAPTURE = 1;

    VideoView draw;
    ImageView play, pause, stop, record, settings;

    final Context c = this;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    File file;
    String selection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recording);

        play = (ImageView) findViewById(R.id.play);
        pause = (ImageView) findViewById(R.id.pause);
        stop = (ImageView) findViewById(R.id.stop);
        record = (ImageView) findViewById(R.id.record);
        settings = (ImageView) findViewById(R.id.settings);


        draw = (VideoView) findViewById(R.id.draw);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Video Mode");



        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_options();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                draw.start();
                record.setEnabled(false);
                pause.setEnabled(true);
                stop.setEnabled(true);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                draw.pause();
                record.setEnabled(false);
                play.setEnabled(true);
                stop.setEnabled(false);


            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                draw.stopPlayback();
                record.setEnabled(true);
                pause.setEnabled(false);
                play.setEnabled(true);

                Toast.makeText(videoRecording.this, "Recording Completed and saved to phones device",
                        Toast.LENGTH_LONG).show();

            }
        });


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }

            }
        });

    }




    // display video on video viewer


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            draw.setVideoURI(videoUri);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public void video_options(){
        listItems = getResources().getStringArray(R.array.video_options);
        checkedItems = new boolean[listItems.length];

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose one of the following options...").setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                switch (arg1) {

                    case 0:

                        selection = listItems[arg1];

                       // getFilePath();
                       // uploadVid();

                        break;

                    case 1:

                        selection = listItems[arg1];

                        video_share();
                        break;

                    case 2:
                        selection = listItems[arg1];

                        video_delete();

                        break;



                }
            }
        })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        AlertDialog mdialog = mBuilder.create();
        mdialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case R.id.home:

                Intent intent = new Intent(videoRecording.this, MainActivity.class);
                videoRecording.this.startActivity(intent);

                return true;


            case R.id.flashlight:

                AlertDialog.Builder builder = new AlertDialog.Builder(videoRecording.this);
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

                intent = new Intent(videoRecording.this, Info.class);
                videoRecording.this.startActivity(intent);

                return true;


            case R.id.tips:

                intent = new Intent(videoRecording.this, SafetyTips.class);
                videoRecording.this.startActivity(intent);

                return true;

            case R.id.exit:

                builder = new AlertDialog.Builder(videoRecording.this);
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

                // languages();


            default:
                return super.onOptionsItemSelected(item);

        }

    }


    public void video_share(){


        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("video/3gp");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Video");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://sdcard/dcim/Camera/filename.3gp"));
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Enjoy the Video");
        startActivity(Intent.createChooser(sendIntent, "Email:"));




    }

    public void video_delete(){

        File videoFile = new File(String.valueOf(file));
        videoFile.delete();
    }

}
