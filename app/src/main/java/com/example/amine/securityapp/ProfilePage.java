package com.example.amine.securityapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ProfilePage extends AppCompatActivity {

    EditText edit_name, edit_email, edit_password, edit_id;
    Button add, view, update, delete;
    ImageView camera, draw;
    SQLiteHelper myDb;

    final int requestPermissionCode = 1;
    private static final int PICK_IMAGE_REQUEST = 234;
    File file;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile page");

        myDb = new SQLiteHelper(this);

        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_email = (EditText)findViewById(R.id.edit_email);
        edit_password = (EditText)findViewById(R.id.edit_password);
        edit_id = (EditText)findViewById(R.id.edit_id);

        add = (Button)findViewById(R.id.add);
        view = (Button)findViewById(R.id.view);
        update = (Button)findViewById(R.id.update);
        delete = (Button)findViewById(R.id.delete);

        camera = (ImageView)findViewById(R.id.camera);
        draw = (ImageView)findViewById(R.id.draw);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();

            }
        });

        addData();
        viewAll();
        updateData();
        deleteData();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            draw.setImageBitmap(imageBitmap);
        }
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }




    public void addData(){

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isInserted = myDb.insertData(edit_name.getText().toString(), edit_email.getText().toString(),
                        edit_password.getText().toString(), edit_id.getText().toString());

                if(isInserted == true){

                    Toast.makeText(ProfilePage.this, "data has been added", Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(ProfilePage.this, "data has not been added", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    public void viewAll() {
        view.setOnClickListener(
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
                            buffer.append("Medical :"+ res.getString(3)+"\n\n");
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

    public void updateData(){

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isUpdated = myDb.updateData(edit_name.getText().toString(), edit_email.getText().toString(),
                        edit_password.getText().toString(), edit_id.getText().toString());

                if(isUpdated == true){

                    Toast.makeText(ProfilePage.this, "data has been updated", Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(ProfilePage.this, "data has not been updated", Toast.LENGTH_LONG).show();

                }

            }
        });


    }


    public void deleteData(){

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDb.deleteData(edit_id.getText().toString());
                if (deletedRows > 0){

                    Toast.makeText(ProfilePage.this, "data has been deleted", Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(ProfilePage.this, "data has not been deleted", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
