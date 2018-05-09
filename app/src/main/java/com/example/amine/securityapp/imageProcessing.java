package com.example.amine.securityapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;

public class imageProcessing extends AppCompatActivity {

    Button iff, ip, id, ie;
    Button sharpen, blurring, enhance, edge_detection;
    private ImageView clockwise, anticlockwise, draw, delete, share, exit, save;
    ImageView camera, gallery;
    Uri uri;

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);

        iff = (Button) findViewById(R.id.iff);
        ip = (Button) findViewById(R.id.ip);
        id = (Button) findViewById(R.id.id);
        ie = (Button) findViewById(R.id.ie);
        clockwise = (ImageView) findViewById(R.id.clockwise);
        anticlockwise = (ImageView) findViewById(R.id.anticlockwise);


        blurring = (Button) findViewById(R.id.blurring);
        edge_detection = (Button) findViewById(R.id.edge_detection);
        enhance = (Button) findViewById(R.id.enhance);

        draw = (ImageView) findViewById(R.id.draw);
        gallery = (ImageView) findViewById(R.id.gallery);
        camera = (ImageView) findViewById(R.id.camera);
        save = (ImageView) findViewById(R.id.save);



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                CropImage();

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                CropImage();
            }
        });


        draw.setDrawingCacheEnabled(true);
        draw.buildDrawingCache(true);


        blurring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bitmap = draw.getDrawingCache();

                Toast.makeText(getApplicationContext(), "enhancement transformation in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                int F[] = {-2, -1, 0, -1, 1, 1, 0, 1, 2};

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 1; j < mPhotoHeight - 1; j++) {
                    for (int i = 1; i < mPhotoWidth - 1; i++) {


                        int Pr = ((F[-2] * Color.red(bitmap.getPixel(i - 1, j - 1))) + (F[-1] * Color.red(bitmap.getPixel(i, j - 1))) +
                                (F[0] * Color.red(bitmap.getPixel(i + 1, j - 1))) + (F[-1] * Color.red(bitmap.getPixel(i - 1, j))) +
                                (F[1] * Color.red(bitmap.getPixel(i, j))) + (F[1] * Color.red(bitmap.getPixel(i + 1, j))) +
                                (F[0] * Color.red(bitmap.getPixel(i - 1, j + 1))) + (F[1] * Color.red(bitmap.getPixel(i, j + 1))) +
                                (F[2] * Color.red(bitmap.getPixel(i + 1, j + 1)))) / 9;

                        int Pg = ((F[-2] * Color.red(bitmap.getPixel(i - 1, j - 1))) + (F[-1] * Color.red(bitmap.getPixel(i, j - 1))) +
                                (F[0] * Color.red(bitmap.getPixel(i + 1, j - 1))) + (F[-1] * Color.red(bitmap.getPixel(i - 1, j))) +
                                (F[1] * Color.red(bitmap.getPixel(i, j))) + (F[1] * Color.red(bitmap.getPixel(i + 1, j))) +
                                (F[0] * Color.red(bitmap.getPixel(i - 1, j + 1))) + (F[1] * Color.red(bitmap.getPixel(i, j + 1))) +
                                (F[2] * Color.red(bitmap.getPixel(i + 1, j + 1)))) / 9;

                        int Pb = ((F[-2] * Color.red(bitmap.getPixel(i - 1, j - 1))) + (F[-1] * Color.red(bitmap.getPixel(i, j - 1))) +
                                (F[0] * Color.red(bitmap.getPixel(i + 1, j - 1))) + (F[-1] * Color.red(bitmap.getPixel(i - 1, j))) +
                                (F[1] * Color.red(bitmap.getPixel(i, j))) + (F[1] * Color.red(bitmap.getPixel(i + 1, j))) +
                                (F[0] * Color.red(bitmap.getPixel(i - 1, j + 1))) + (F[1] * Color.red(bitmap.getPixel(i, j + 1))) +
                                (F[2] * Color.red(bitmap.getPixel(i + 1, j + 1)))) / 9;

                        bitmap.setPixel(i, j, Color.rgb(Pr, Pg, Pb));
                    }
                }
                draw.setImageBitmap(bitmap);
                draw.postInvalidate();
            }
        });

        edge_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "edge detection transformation in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                bitmap = draw.getDrawingCache();

                int F[] = {1, 1, 1, 1, 1, 1, 1, 1, 1};

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 1; j < mPhotoHeight - 1; j++) {
                    for (int i = 1; i < mPhotoWidth - 1; i++) {

                        int Pr = ((F[0] * Color.red(bitmap.getPixel(i - 1, j - 1))) + (F[1] * Color.red(bitmap.getPixel(i, j - 1))) +
                                (F[2] * Color.red(bitmap.getPixel(i + 1, j - 1))) + (F[3] * Color.red(bitmap.getPixel(i - 1, j))) +
                                (F[4] * Color.red(bitmap.getPixel(i, j))) + (F[5] * Color.red(bitmap.getPixel(i + 1, j))) +
                                (F[6] * Color.red(bitmap.getPixel(i - 1, j + 1))) + (F[7] * Color.red(bitmap.getPixel(i, j + 1))) +
                                (F[8] * Color.red(bitmap.getPixel(i + 1, j + 1)))) / 9;

                        int Pg = ((F[0] * Color.green(bitmap.getPixel(i - 1, j - 1))) + (F[1] * Color.green(bitmap.getPixel(i, j - 1))) +
                                (F[2] * Color.green(bitmap.getPixel(i + 1, j - 1))) + (F[3] * Color.green(bitmap.getPixel(i - 1, j))) +
                                (F[4] * Color.green(bitmap.getPixel(i, j))) + (F[5] * Color.green(bitmap.getPixel(i + 1, j))) +
                                (F[6] * Color.green(bitmap.getPixel(i - 1, j + 1))) + (F[7] * Color.green(bitmap.getPixel(i, j + 1))) +
                                (F[8] * Color.green(bitmap.getPixel(i + 1, j + 1)))) / 9;

                        int Pb = ((F[0] * Color.blue(bitmap.getPixel(i - 1, j - 1))) + (F[1] * Color.blue(bitmap.getPixel(i, j - 1))) +
                                (F[2] * Color.blue(bitmap.getPixel(i + 1, j - 1))) + (F[3] * Color.blue(bitmap.getPixel(i - 1, j))) +
                                (F[4] * Color.blue(bitmap.getPixel(i, j))) + (F[5] * Color.blue(bitmap.getPixel(i + 1, j))) +
                                (F[6] * Color.blue(bitmap.getPixel(i - 1, j + 1))) + (F[7] * Color.blue(bitmap.getPixel(i, j + 1))) +
                                (F[8] * Color.blue(bitmap.getPixel(i + 1, j + 1)))) / 9;

                        bitmap.setPixel(i, j, Color.rgb(Pr, Pg, Pb));
                    }
                }
                draw.setImageBitmap(bitmap);
                draw.postInvalidate();
            }
        });


        sharpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bitmap = draw.getDrawingCache();

                Toast.makeText(getApplicationContext(), "sharpening transformation in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                int F[] = {0, -2, 0, -2, 11, -2, 0, -2, 0};

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 1; j < mPhotoHeight - 1; j++) {
                    for (int i = 1; i < mPhotoWidth - 1; i++) {


                        int Pr = ((F[0] * Color.red(bitmap.getPixel(i - 1, j - 1))) + (F[1] * Color.red(bitmap.getPixel(i, j - 1))) +
                                (F[2] * Color.red(bitmap.getPixel(i + 1, j - 1))) + (F[3] * Color.red(bitmap.getPixel(i - 1, j))) +
                                (F[4] * Color.red(bitmap.getPixel(i, j))) + (F[5] * Color.red(bitmap.getPixel(i + 1, j))) +
                                (F[6] * Color.red(bitmap.getPixel(i - 1, j + 1))) + (F[7] * Color.red(bitmap.getPixel(i, j + 1))) +
                                (F[8] * Color.red(bitmap.getPixel(i + 1, j + 1)))) / 9;

                        int Pg = ((F[0] * Color.green(bitmap.getPixel(i - 1, j - 1))) + (F[1] * Color.green(bitmap.getPixel(i, j - 1))) +
                                (F[2] * Color.green(bitmap.getPixel(i + 1, j - 1))) + (F[3] * Color.green(bitmap.getPixel(i - 1, j))) +
                                (F[4] * Color.green(bitmap.getPixel(i, j))) + (F[5] * Color.green(bitmap.getPixel(i + 1, j))) +
                                (F[6] * Color.green(bitmap.getPixel(i - 1, j + 1))) + (F[7] * Color.green(bitmap.getPixel(i, j + 1))) +
                                (F[8] * Color.green(bitmap.getPixel(i + 1, j + 1)))) / 9;

                        int Pb = ((F[0] * Color.blue(bitmap.getPixel(i - 1, j - 1))) + (F[1] * Color.blue(bitmap.getPixel(i, j - 1))) +
                                (F[2] * Color.blue(bitmap.getPixel(i + 1, j - 1))) + (F[3] * Color.blue(bitmap.getPixel(i - 1, j))) +
                                (F[4] * Color.blue(bitmap.getPixel(i, j))) + (F[5] * Color.blue(bitmap.getPixel(i + 1, j))) +
                                (F[6] * Color.blue(bitmap.getPixel(i - 1, j + 1))) + (F[7] * Color.blue(bitmap.getPixel(i, j + 1))) +
                                (F[8] * Color.blue(bitmap.getPixel(i + 1, j + 1)))) / 9;

                        bitmap.setPixel(i, j, Color.rgb(Pr, Pr, Pr));
                    }
                }
                draw.setImageBitmap(bitmap);
                draw.postInvalidate();
            }
        });


        enhance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "blurring transformation in progress... !!!",
                        Toast.LENGTH_SHORT).show();

                bitmap = draw.getDrawingCache();


                int F[] = {-1,-1,-1,-1,8,-1,-1,-1,-1};

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 1; j < mPhotoHeight-1; j++) {
                    for (int i = 1; i < mPhotoWidth-1; i++) {

                        int Pr = (  (F[0]*Color.red(bitmap.getPixel(i-1,j-1))) + (F[1]*Color.red(bitmap.getPixel(i,j-1))) +
                                (F[2]*Color.red(bitmap.getPixel(i+1,j-1))) + (F[3]*Color.red(bitmap.getPixel(i-1,j))) +
                                (F[4]*Color.red(bitmap.getPixel(i,j))) + (F[5]*Color.red(bitmap.getPixel(i+1,j))) +
                                (F[6]*Color.red(bitmap.getPixel(i-1,j+1))) + (F[7]*Color.red(bitmap.getPixel(i,j+1))) +
                                (F[8]*Color.red(bitmap.getPixel(i+1,j+1))) ) / 9;

                        int Pg = (  (F[0]*Color.green(bitmap.getPixel(i-1,j-1))) + (F[1]*Color.green(bitmap.getPixel(i,j-1))) +
                                (F[2]*Color.green(bitmap.getPixel(i+1,j-1))) + (F[3]*Color.green(bitmap.getPixel(i-1,j))) +
                                (F[4]*Color.green(bitmap.getPixel(i,j))) + (F[5]*Color.green(bitmap.getPixel(i+1,j))) +
                                (F[6]*Color.green(bitmap.getPixel(i-1,j+1))) + (F[7]*Color.green(bitmap.getPixel(i,j+1))) +
                                (F[8]*Color.green(bitmap.getPixel(i+1,j+1))) ) / 9;

                        int Pb = (  (F[0]*Color.blue(bitmap.getPixel(i-1,j-1))) + (F[1]*Color.blue(bitmap.getPixel(i,j-1))) +
                                (F[2]*Color.blue(bitmap.getPixel(i+1,j-1))) + (F[3]*Color.blue(bitmap.getPixel(i-1,j))) +
                                (F[4]*Color.blue(bitmap.getPixel(i,j))) + (F[5]*Color.blue(bitmap.getPixel(i+1,j))) +
                                (F[6]*Color.blue(bitmap.getPixel(i-1,j+1))) + (F[7]*Color.blue(bitmap.getPixel(i,j+1))) +
                                (F[8]*Color.blue(bitmap.getPixel(i+1,j+1))) ) / 9;

                        int Pa = (  (F[0]*Color.blue(bitmap.getPixel(i-1,j-1))) + (F[1]*Color.blue(bitmap.getPixel(i,j-1))) +
                                (F[2]*Color.blue(bitmap.getPixel(i+1,j-1))) + (F[3]*Color.blue(bitmap.getPixel(i-1,j))) +
                                (F[4]*Color.blue(bitmap.getPixel(i,j))) + (F[5]*Color.blue(bitmap.getPixel(i+1,j))) +
                                (F[6]*Color.blue(bitmap.getPixel(i-1,j+1))) + (F[7]*Color.blue(bitmap.getPixel(i,j+1))) +
                                (F[8]*Color.blue(bitmap.getPixel(i+1,j+1))) ) / 9;

                        bitmap.setPixel(i, j, Color.argb(Pa, Pr, Pg, Pb));
                    }
                }
                draw.setImageBitmap(bitmap);
                draw.postInvalidate();
            }
        });

    }




    private void CropImage(){

        try {

            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 96);
            cropIntent.putExtra("outputY", 96);
            cropIntent.putExtra("noFaceDetection", true);
            // retrieve data on return
            cropIntent.putExtra("return-data", false);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 1);
        }
        catch (ActivityNotFoundException ex)
        {

        }

    }



    // import image from gallery & camera



    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    //share image
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


    // saving images
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0 && resultCode == RESULT_OK){
            CropImage();
        }
        else if(requestCode == 2)
        {
            if(data != null)
            {
                uri = data.getData();
                CropImage();
            }
        }
        else if(requestCode == 1)
        {
            if(data != null)

            {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                draw.setImageBitmap(bitmap);
            }

        }
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

                AlertDialog.Builder builder = new AlertDialog.Builder(imageProcessing.this);
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
