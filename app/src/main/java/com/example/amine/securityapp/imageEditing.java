package com.example.amine.securityapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.description;
import static android.R.attr.name;
import static com.example.amine.securityapp.R.attr.title;

public class imageEditing extends AppCompatActivity implements View.OnClickListener {

    Button iff, ip, id, ie;
    Button sepia, negative, transparent, green, blue, red;
    ImageView draw, gallery, camera, clockwise, anticlockwise, save;
    Bitmap bitmap;
    Intent intent;


    private static final int RESULT_LOAD_IMAGE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editing);



        iff = (Button) findViewById(R.id.iff);
        ip = (Button) findViewById(R.id.ip);
        id = (Button) findViewById(R.id.id);
        ie = (Button) findViewById(R.id.ie);
        draw = (ImageView) findViewById(R.id.draw);

        sepia = (Button) findViewById(R.id.sepia);
        negative = (Button) findViewById(R.id.negative);
        transparent = (Button) findViewById(R.id.transparent);
        green = (Button) findViewById(R.id.green);
        blue = (Button) findViewById(R.id.blue);
        red = (Button) findViewById(R.id.red);

        camera = (ImageView) findViewById(R.id.camera);
        gallery = (ImageView) findViewById(R.id.gallery);
        clockwise = (ImageView) findViewById(R.id.clockwise);
        anticlockwise = (ImageView) findViewById(R.id.anticlockwise);
        save = (ImageView) findViewById(R.id.save);


        draw.buildDrawingCache();
        bitmap = draw.getDrawingCache();



        iff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(imageEditing.this, imageEditing.class);
                imageEditing.this.startActivity(intent);

            }
        });

        ie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(imageEditing.this, imageEnhancement.class);
                imageEditing.this.startActivity(intent);

            }
        });

        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(imageEditing.this, imageProcessing.class);
                imageEditing.this.startActivity(intent);

            }
        });

        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(imageEditing.this, imageDrawing.class);
                imageEditing.this.startActivity(intent);

            }
        });



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

        clockwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                draw.setRotation(draw.getRotation() + 90);

            }
        });

        anticlockwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                draw.setRotation(draw.getRotation() - 90);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startSave();
            }
        });



        draw.setDrawingCacheEnabled(true);
        draw.buildDrawingCache(true);


        sepia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bitmap = draw.getDrawingCache();

                final double red = 0.9;
                final double green = 0.59;
                final double blue = 0.11;

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int a = Color.alpha(s_pixel);
                        int r = Color.red(s_pixel);
                        int g = Color.green(s_pixel);
                        int b = Color.blue(s_pixel);

                        b = g = r = (int) (red * r + green * g + blue * b);

                        r += 110;
                        if (r > 255) {
                            r = 255;
                        }

                        g += 65;
                        if (g > 255) {
                            g = 255;
                        }

                        b += 20;
                        if (b > 255) {
                            b = 255;
                        }

                        bitmap.setPixel(i, j, Color.argb(a, r, g, b));
                    }
                }
                draw.setImageBitmap(bitmap);
                draw.postInvalidate();

            }
        });


        transparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bitmap = draw.getDrawingCache();

                int mPhotoWidth = draw.getWidth();
                int mPhotoHeight = draw.getHeight();

                for (int j = 0; j < mPhotoHeight; j++) {
                    for (int i = 0; i < mPhotoWidth; i++) {

                        int s_pixel = bitmap.getPixel(i, j);

                        int r = Color.red(s_pixel);
                        int g = Color.green(s_pixel);
                        int b = Color.blue(s_pixel);
                        int a = Color.alpha(s_pixel);

                        int gpv = (r + g + b + a);

                        bitmap.setPixel(i, j, Color.argb(gpv, gpv, gpv, gpv));
                    }
                }
                draw.setImageBitmap(bitmap);
                draw.postInvalidate();

            }
        });


        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                draw.postInvalidate();

            }
        });
        draw.setImageResource(0);


        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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


        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    }


// import image from gallery & camera

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView draw = (ImageView) findViewById(R.id.draw);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            draw.setImageBitmap(bmp);

        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            draw.setImageBitmap(photo);
        }


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            draw.setImageBitmap(imageBitmap);
        }
    }


    Context context;
    String imageName;

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

                AlertDialog.Builder builder = new AlertDialog.Builder(imageEditing.this);
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

    @Override
    public void onClick(View view) {

    }
}
