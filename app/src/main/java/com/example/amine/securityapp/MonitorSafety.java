package com.example.amine.securityapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MonitorSafety extends AppCompatActivity {


    EditText edit_name, edit_email, edit_password, edit_id;
    Button add, view, update, delete, button;
    SQLiteHelper myDb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_safety);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Emergency contacts");


        myDb = new SQLiteHelper(this);

        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_email = (EditText)findViewById(R.id.edit_email);
        edit_password = (EditText)findViewById(R.id.edit_password);
        edit_id = (EditText)findViewById(R.id.edit_id);

        add = (Button)findViewById(R.id.add);
        view = (Button)findViewById(R.id.view);
        update = (Button)findViewById(R.id.update);
        delete = (Button)findViewById(R.id.delete);

        addData();
        viewAll();
        updateData();
        deleteData();



    }



    public void deleteData(){

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDb.deleteData(edit_id.getText().toString());
                if (deletedRows > 0){

                    Toast.makeText(MonitorSafety.this, "Emergency contact number data has been delete", Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(MonitorSafety.this, "Sorry, unable to delete data", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void addData(){

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isInserted = myDb.insertData(edit_name.getText().toString(), edit_email.getText().toString(),
                        edit_password.getText().toString(), edit_id.getText().toString());

                if(isInserted == true){

                    Toast.makeText(MonitorSafety.this, "Emergency contact number data has been save", Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(MonitorSafety.this, "Sorry, unable to save data", Toast.LENGTH_LONG).show();

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
                            showMessage("Error","No data found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("id :"+ res.getString(0)+"\n");
                            buffer.append("Name :"+ res.getString(1)+"\n");
                            buffer.append("email :"+ res.getString(2)+"\n");
                            buffer.append("Tel num :"+ res.getString(3)+"\n\n");
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

                    Toast.makeText(MonitorSafety.this, "Emergency contacts data has been updated", Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(MonitorSafety.this, "Eroor, could not update data", Toast.LENGTH_LONG).show();

                }

            }
        });


    }







    final Context c = this;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String selection;
    Uri uri;
    Intent CropIntent;


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

                        LocaleHelper.setLocale(MonitorSafety.this, "en");
                        MonitorSafety.this.recreate();

                        Toast.makeText(getApplicationContext(), "English language chosen!",
                                Toast.LENGTH_LONG).show();

                        break;

                    case 1:
                        selection = listItems[arg1];

                        LocaleHelper.setLocale(MonitorSafety.this, "fr");
                        MonitorSafety.this.recreate();

                        Toast.makeText(getApplicationContext(), "French language chosen!",
                                Toast.LENGTH_LONG).show();

                        break;

                    case 2:
                        selection = listItems[arg1];

                        LocaleHelper.setLocale(MonitorSafety.this, "ar");
                        MonitorSafety.this.recreate();

                        Toast.makeText(getApplicationContext(), "Arabic language chosen!",
                                Toast.LENGTH_LONG).show();

                        break;

                }
            }
        });
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

                Intent intent = new Intent(MonitorSafety.this, MainActivity.class);
                MonitorSafety.this.startActivity(intent);

                return true;

            case R.id.profile1:

                intent = new Intent(MonitorSafety.this, ProfilePage.class);
                MonitorSafety.this.startActivity(intent);

                return true;


            case R.id.flashlight:

                AlertDialog.Builder builder = new AlertDialog.Builder(MonitorSafety.this);
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

                intent = new Intent(MonitorSafety.this, Info.class);
                MonitorSafety.this.startActivity(intent);

                return true;


            case R.id.tips:

                intent = new Intent(MonitorSafety.this, SafetyTips.class);
                MonitorSafety.this.startActivity(intent);

                return true;

            case R.id.exit:

                listItems = getResources().getStringArray(R.array.Languages);
                checkedItems = new boolean[listItems.length];

                builder = new AlertDialog.Builder(MonitorSafety.this);
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

                                LocaleHelper.setLocale(MonitorSafety.this, "en");
                                MonitorSafety.this.recreate();
                                break;

                            case 1:

                                selection = listItems[arg1];

                                LocaleHelper.setLocale(MonitorSafety.this, "fr");
                                MonitorSafety.this.recreate();
                                break;

                            case 2:
                                selection = listItems[arg1];

                                LocaleHelper.setLocale(MonitorSafety.this, "ar");
                                MonitorSafety.this.recreate();
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
