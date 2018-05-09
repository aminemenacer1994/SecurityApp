package com.example.amine.securityapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static android.R.attr.path;
import static android.app.PendingIntent.getActivity;

public class cameraRecording extends AppCompatActivity implements View.OnClickListener {


    ImageView gallery, camera, draw, save, rotate, image_processing, delete, share;
    String selection;

    final int requestPermissionCode = 1;
    private static final int PICK_IMAGE_REQUEST = 234;
    File file;


    final Context c = this;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    private Uri filePath;

    Uri source1;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private StorageReference imgRef;
    private ProgressDialog mProgressDialog;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_recording);


        share = (ImageView) findViewById(R.id.share);
        rotate = (ImageView) findViewById(R.id.rotate);
        save = (ImageView) findViewById(R.id.save);
        delete = (ImageView) findViewById(R.id.delete);
        draw = (ImageView) findViewById(R.id.draw);
        camera = (ImageView) findViewById(R.id.camera);
        gallery = (ImageView) findViewById(R.id.gallery);
        image_processing = (ImageView) findViewById(R.id.image_processing);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Camera Mode");

        storageReference = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShare();
            }
        });


        save.setOnClickListener(this);

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw.setRotation(draw.getRotation() + 90);
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePictureIntent();
                Toast.makeText(cameraRecording.this, "Image saved to devices memory", Toast.LENGTH_SHORT).show();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw.setImageDrawable(null);

            }
        });


        image_processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cameraRecording.this, imageEnhancement.class);
                cameraRecording.this.startActivity(intent);
            }
        });

    }

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("images.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            draw.setImageBitmap(imageBitmap);

        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {


            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                draw.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


/*
            if(resultCode == RESULT_OK){
                switch (requestCode){
                    case RQS_IMAGE1:
                        source1 = data.getData();
                        textSource1.setText(source1.toString());
                        break;
                }
            }
*/

        }



    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;

    }

    public void startShare() {

        Bitmap bitmap = viewToBitmap(draw, draw.getWidth(), draw.getHeight());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + " SecurityShare.jpg");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/SecurityShare.jpg"));
        startActivity(Intent.createChooser(shareIntent, "share image"));
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void startSave() {
        FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()) {
            Toast.makeText(this, "cant save image to directory", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy,mm,ss,hh,mm,ss");
        String date = simpleDataFormat.format(new Date());
        String name = "Img" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);
        try {
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(draw, draw.getWidth(), draw.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            Toast.makeText(this, "image saved", Toast.LENGTH_SHORT).show();
            try {
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Security apps");

    }


    private static final int SELECTED_PIC = 1;

    /*
    // camera options
    public void camera() {

        listItems = getResources().getStringArray(R.array.camera);
        checkedItems = new boolean[listItems.length];

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose one of the options...").setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                switch (arg1) {

                    case 0:

                        selection = listItems[arg1];
                        dispatchTakePictureIntent();



                        break;

                    case 1:
                        showFileChooser();
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
*/

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode)
        {
            case requestPermissionCode:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void RequestRuntimePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(cameraRecording.this, Manifest.permission.CAMERA))
            Toast.makeText(this,"Camera permission allowed", Toast.LENGTH_SHORT).show();
        else{
            ActivityCompat.requestPermissions(cameraRecording.this, new String[]{Manifest.permission.CAMERA}, requestPermissionCode);
        }


    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {



            case R.id.home:

                Intent intent = new Intent(cameraRecording.this, MainActivity.class);
                cameraRecording.this.startActivity(intent);

                return true;


            case R.id.flashlight:

                AlertDialog.Builder builder = new AlertDialog.Builder(cameraRecording.this);
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

                intent = new Intent(cameraRecording.this, Info.class);
                cameraRecording.this.startActivity(intent);

                return true;


            case R.id.tips:

                intent = new Intent(cameraRecording.this, SafetyTips.class);
                cameraRecording.this.startActivity(intent);

                return true;

            case R.id.exit:

                builder = new AlertDialog.Builder(cameraRecording.this);
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


    @Override
    public void onClick(View view) {
        if (view == save) {
           // uploadFile();
            startSave();

        }
    }
}
