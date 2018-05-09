package com.example.amine.securityapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class imageEnhancement extends AppCompatActivity {


    Button saturation, b_w, contrast, brightness, negative, red, blue, green;
    ImageView draw, camera, gallery, save, share, rotate, delete, crop;
    Bitmap bitmap;
    Intent intent;
    Context context;


    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private StorageReference storageReference;
    private Uri filePath;



    File file;
    final int requestPermissionCode=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_enhancement);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Image processing");

        storageReference = FirebaseStorage.getInstance().getReference();



        draw = (ImageView) findViewById(R.id.draw);
        gallery = (ImageView) findViewById(R.id.gallery);
        camera = (ImageView) findViewById(R.id.camera);
        save = (ImageView) findViewById(R.id.save);
        share = (ImageView) findViewById(R.id.share);
        delete = (ImageView) findViewById(R.id.delete);
        rotate = (ImageView) findViewById(R.id.rotate);

        saturation = (Button) findViewById(R.id.saturation);
        b_w = (Button) findViewById(R.id.b_w);
        contrast = (Button) findViewById(R.id.contrast);
        brightness = (Button) findViewById(R.id.brightness);

        negative = (Button) findViewById(R.id.negative);
        red = (Button) findViewById(R.id.red);
        green = (Button) findViewById(R.id.green);
        blue = (Button) findViewById(R.id.blue);
        final ProgressDialog progressDialog = new ProgressDialog(this);



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShare();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startSave();
                uploadFile();
            }
        });

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw.setRotation(draw.getRotation() + 90);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw.setImageDrawable(null);
            }
        });


        draw.setDrawingCacheEnabled(true);
        draw.buildDrawingCache(true);

        saturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Processing in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                bitmap = draw.getDrawingCache();

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int r = Color.red(s_pixel);
                        int g = Color.green(s_pixel);
                        int b = Color.blue(s_pixel);


                        int gpv = (360 / 256 * r + g * 256 + b / 256);


                        bitmap.setPixel(i, j, Color.rgb(gpv, gpv, gpv));

                    }
                }

                draw.setImageBitmap(bitmap);
                draw.postInvalidate();

            }
        });


        b_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Processing in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                bitmap = draw.getDrawingCache();

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int r = Color.red(s_pixel);
                        int g = Color.green(s_pixel);
                        int b = Color.blue(s_pixel);

                        int gpv = (int) (0.299 * r + 0.587 * g + 0.114 * b);

                        bitmap.setPixel(i, j, Color.rgb(gpv, gpv, gpv));
                    }
                }

                draw.setImageBitmap(bitmap);
                draw.postInvalidate();

            }
        });


        contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Processing in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                bitmap = draw.getDrawingCache();

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int r = Color.red(s_pixel);
                        int g = Color.green(s_pixel);
                        int b = Color.blue(s_pixel);

                        int gpv = (int) (0.59 * r + 0.57 * g + 0.54 * b);

                        bitmap.setPixel(i, j, Color.rgb(gpv, gpv, gpv));
                    }
                }

                draw.setImageBitmap(bitmap);
                draw.postInvalidate();
            }
        });


        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Processing in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                bitmap = draw.getDrawingCache();

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int a = Color.alpha(s_pixel);
                        int r = Color.red(s_pixel);
                        int g = Color.green(s_pixel);
                        int b = Color.blue(s_pixel);

                        int gpv = (int) (0.299 * r + 0.587 * g + 0.114 * b) / (a * 3);

                        bitmap.setPixel(i, j, Color.rgb(gpv, gpv, gpv));
                    }
                }

                draw.setImageBitmap(bitmap);
                draw.postInvalidate();

            }
        });


        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Processing in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                bitmap = draw.getDrawingCache();

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int r = Color.red(s_pixel);
                        int g = Color.green(s_pixel);
                        int b = Color.blue(s_pixel);

                        int gpv = (int) (0.99 * r + 0.987 * g + 0.514 * b);

                        bitmap.setPixel(i, j, Color.rgb(gpv, gpv, gpv));
                    }
                }
                draw.setImageBitmap(bitmap);
                draw.postInvalidate();

            }
        });


        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getApplicationContext(), "Processing in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                bitmap = draw.getDrawingCache();

                double red = 0.78;
                double green = 0.39;
                double blue = 0.42;

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int a = Color.alpha(s_pixel);
                        int r = (int) (Color.red(s_pixel) * red);
                        int g = (int) (Color.green(s_pixel) * green);
                        int b = (int) (Color.blue(s_pixel) * blue);

                        bitmap.setPixel(i, j, Color.argb(a, r, g, b));


                    }
                }
                draw.setImageBitmap(bitmap);
                draw.postInvalidate();

            }
        });
        draw.setImageResource(0);


        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Processing in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                bitmap = draw.getDrawingCache();

                double red = 0.39;
                double green = 0.78;
                double blue = 0.42;

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int a = Color.alpha(s_pixel);
                        int r = (int) (Color.red(s_pixel) * red);
                        int g = (int) (Color.green(s_pixel) * green);
                        int b = (int) (Color.blue(s_pixel) * blue);

                        bitmap.setPixel(i, j, Color.argb(a, r, g, b));
                    }
                }
                draw.setImageBitmap(bitmap);
                draw.postInvalidate();

            }
        });


        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bitmap = draw.getDrawingCache();

                double red = 0.39;
                double green = 0.42;
                double blue = 0.78;

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int a = Color.alpha(s_pixel);
                        int r = (int) (Color.red(s_pixel) * red);
                        int g = (int) (Color.green(s_pixel) * green);
                        int b = (int) (Color.blue(s_pixel) * blue);

                        bitmap.setPixel(i, j, Color.argb(a, r, g, b));
                    }
                }

                draw.setImageBitmap(bitmap);
                progressDialog.dismiss();
                draw.postInvalidate();

            }
        });
        draw.setImageResource(0);


        int permissionCheck = ContextCompat.checkSelfPermission(imageEnhancement.this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            RequestRuntimePermission();
    }

    private void RequestRuntimePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(imageEnhancement.this, Manifest.permission.CAMERA))
            Toast.makeText(this,"Camera permission allowed", Toast.LENGTH_SHORT).show();
        else{
            ActivityCompat.requestPermissions(imageEnhancement.this, new String[]{Manifest.permission.CAMERA}, requestPermissionCode);
        }

    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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


    public void startShare(){

        Bitmap bitmap = viewToBitmap(draw, draw.getWidth(), draw.getHeight());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + " SecurityShare.jpg");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e){
            e.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/SecurityShare.jpg"));
        startActivity(Intent.createChooser(shareIntent, "share image"));
    }


    public static Bitmap viewToBitmap(View view, int width, int height){
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void startSave(){
        FileOutputStream fileOutputStream=null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()){
            Toast.makeText(this, "cant save image to directory", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy,mm,ss,hh,mm,ss");
        String date = simpleDataFormat.format(new Date());
        String name= "Img" +date+".jpg";
        String file_name = file.getAbsolutePath()+"/" + name;
        File new_file = new File(file_name);
        try {
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(draw,draw.getWidth(), draw.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
            Toast.makeText(this,"image saved", Toast.LENGTH_SHORT).show();
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
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(file, "Security apps");

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

    // action bar menu
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);


        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){


            case R.id.share:

                startShare();

                return true;


            case R.id.exit:

                AlertDialog.Builder builder = new AlertDialog.Builder(imageEnhancement.this);
                builder.setTitle("Security");
                builder.setMessage("Are you sure you want to exit ?");
                builder.setCancelable(false);

                builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        System.exit(0);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                    }
                });

                Dialog dialog = builder.create();
                dialog.show();

                return true;

        }

        return false;
    }
}
