package com.example.amine.securityapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class audioRecording extends AppCompatActivity {

    ImageView buttonStart, buttonStop, buttonPlayLastRecordAudio, buttonStopPlayingRecording;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    ImageView settings;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    Uri uri;
    private ProgressDialog mProgress;



    private String mFileName = null;
    private StorageReference mStorage;

    String selection;
    final Context c = this;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recording);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Audio Mode");

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);


        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/record_audio";

        buttonStart = (ImageView) findViewById(R.id.button);
        buttonStop = (ImageView) findViewById(R.id.button2);
        buttonPlayLastRecordAudio = (ImageView) findViewById(R.id.button3);
        buttonStopPlayingRecording = (ImageView)findViewById(R.id.button4);
        settings = (ImageView)findViewById(R.id.settings);

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        settings = (ImageView) findViewById(R.id.settings);
        random = new Random();

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_options();
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(audioRecording.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }

        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);


                Toast.makeText(audioRecording.this, "Recording Completed and saved to phones device",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                mediaPlayer.start();
                Toast.makeText(audioRecording.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }

            }
        });

    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }



    public void audioUpload(){

        StorageReference filePath = mStorage.child("Audio").child("new_audio.3gp");

        mProgress.setMessage("Uploading audio");
        mProgress.show();

        Uri uri = Uri.fromFile(new File(mFileName));

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgress.dismiss();
                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

            }
        });

    }


    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(audioRecording.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(audioRecording.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(audioRecording.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {



            case R.id.home:

                Intent intent = new Intent(audioRecording.this, MainActivity.class);
                audioRecording.this.startActivity(intent);

                return true;


            case R.id.flashlight:

                AlertDialog.Builder builder = new AlertDialog.Builder(audioRecording.this);
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

                intent = new Intent(audioRecording.this, Info.class);
                audioRecording.this.startActivity(intent);

                return true;


            case R.id.tips:

                intent = new Intent(audioRecording.this, SafetyTips.class);
                audioRecording.this.startActivity(intent);

                return true;

            case R.id.exit:

                builder = new AlertDialog.Builder(audioRecording.this);
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


    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Security apps");

    }

    public void audio_options(){
        listItems = getResources().getStringArray(R.array.audio_options);
        checkedItems = new boolean[listItems.length];

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose one of the following options...").setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                switch (arg1) {

                    case 0:

                        selection = listItems[arg1];

                        audio_share();
                        break;

                    case 1:

                        selection = listItems[arg1];

                        audio_delete();

                        break;

                    case 2:
                        selection = listItems[arg1];

                        audio_save();
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

    }


    public void audio_share(){

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Audio File"));
    }

    public void audio_delete(){

        File sdcard = new File(Environment.getExternalStorageDirectory(), "sample");
        String fromFullPath = "/username556596268.mp3";
        String toFullPath = "/username.mp3";

        File from = new File(sdcard,fromFullPath);
        File to = new File(sdcard,toFullPath);
        from.renameTo(to);
        from.delete();
    }

    public void audio_save(){
    }
}
