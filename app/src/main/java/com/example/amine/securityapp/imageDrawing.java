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
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class imageDrawing extends AppCompatActivity {

    Button iff, ip, id, ie, options;
    ImageView blue, green, red, yellow, orange, white;
    ImageView draw, gallery, camera, clockwise, anticlockwise;
    RelativeLayout color_layout;
    private Paint paint;
    DrawingView dv;


    private static final int RESULT_LOAD_IMAGE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_drawing);

        iff = (Button) findViewById(R.id.iff);
        ip = (Button) findViewById(R.id.ip);
        id = (Button) findViewById(R.id.id);
        ie = (Button) findViewById(R.id.ie);
        options = (Button) findViewById(R.id.options);
        color_layout = (RelativeLayout) findViewById(R.id.color_layout);

        red = (ImageView) findViewById(R.id.red);
        white = (ImageView) findViewById(R.id.white);
        green = (ImageView) findViewById(R.id.green);
        blue = (ImageView) findViewById(R.id.blue);
        yellow = (ImageView) findViewById(R.id.yellow);

        camera = (ImageView) findViewById(R.id.camera);
        gallery = (ImageView) findViewById(R.id.gallery);

        clockwise = (ImageView) findViewById(R.id.clockwise);
        anticlockwise = (ImageView) findViewById(R.id.anticlockwise);


        dv = new DrawingView(this);

        final RelativeLayout lay = (RelativeLayout) findViewById(R.id.color_layout);
        lay.addView(dv);

        RelativeLayout color_layout = (RelativeLayout) findViewById(R.id.color_layout);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(8);


        ie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(imageDrawing.this, imageEnhancement.class);
                imageDrawing.this.startActivity(intent);

            }
        });

        iff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(imageDrawing.this, imageEditing.class);
                imageDrawing.this.startActivity(intent);

            }
        });

        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(imageDrawing.this, imageProcessing.class);
                imageDrawing.this.startActivity(intent);
            }
        });

        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(imageDrawing.this, imageDrawing.class);
                imageDrawing.this.startActivity(intent);

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


        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(Color.RED);
                Toast.makeText(getApplicationContext(), "Colour red chosen !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(Color.BLUE);
                Toast.makeText(getApplicationContext(), "Colour blue chosen !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(Color.YELLOW);
                Toast.makeText(getApplicationContext(), "Colour yellow chosen !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(Color.GREEN);
                Toast.makeText(getApplicationContext(), "Colour green chosen !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(Color.WHITE);
                Toast.makeText(getApplicationContext(), "Colour white chosen !!!",
                        Toast.LENGTH_SHORT).show();
            }
        });



        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(imageDrawing.this);
                builder.setTitle("Image editor");
                builder.setMessage("Save to ???");
                builder.setCancelable(false);

                builder.setPositiveButton("Pens & pencils", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Changes applied !!!",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(imageDrawing.this, imagePens.class);
                        imageDrawing.this.startActivity(intent);

                    }
                });

                builder.setNegativeButton("Brush sizes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Changes applied !!!",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(imageDrawing.this, imageBrushes.class);
                        imageDrawing.this.startActivity(intent);

                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
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





    // colours
    class DrawingView extends View {

        public int width;
        public int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c) {
            super(c);
            context = c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, paint);
            canvas.drawPath(circlePath, circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();

            mCanvas.drawPath(mPath, paint);

            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

        public void clearDrawing() {

            setDrawingCacheEnabled(false);

            onSizeChanged(width, height, width, height);
            invalidate();

            setDrawingCacheEnabled(true);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            width = w;      // don't forget these
            height = h;

            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
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

                AlertDialog.Builder builder = new AlertDialog.Builder(imageDrawing.this);
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
