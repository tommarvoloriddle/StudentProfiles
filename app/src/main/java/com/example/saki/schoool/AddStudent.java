package com.example.saki.schoool;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AddStudent extends AppCompatActivity {


    ImageButton imageButton;
    Student newstudent;
    String address;
    String name;
    String email, bool;
    Bitmap photo;
    EditText editname;
    EditText editmail;
    EditText editphone;
    EditText editaddress;
    int phone = 0;

    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        Intent intent = getIntent();

        bool = intent.getStringExtra("from");
        if (Objects.equals(bool, "true")) {

            getSupportActionBar().setTitle("Edit Student");
            imageButton = (ImageButton) findViewById(R.id.profilepic);
            editaddress = (EditText) findViewById(R.id.editaddresss);
            editphone = (EditText) findViewById(R.id.editphone);
            editmail = (EditText) findViewById(R.id.editmail);
            editname = (EditText) findViewById(R.id.editname);


            email = intent.getStringExtra("email");
            String phone1 = intent.getStringExtra("phone");

            Realm.init(AddStudent.this);
            Realm real;

            try {
                real = Realm.getDefaultInstance();

            } catch (Exception e) {

                // Get a Realm instance for this thread
                RealmConfiguration config = new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();
                real = Realm.getInstance(config);

            }
            real.beginTransaction();

            Student current = real.where(Student.class).equalTo("email", email).findFirst();

            phone = current.getphone();
            name = current.getname();
            address = current.getaddress();
            photo = current.getimage();

            phone1 = String.valueOf(phone);

            real.commitTransaction();
            real.close();

            imageButton.setImageBitmap(photo);
            imageButton.setBackgroundColor(0);
            editaddress.setText(address);
            editmail.setText(email);
            editname.setText(name);
            editphone.setText(phone1);
            if (imageButton != null) {
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                       // startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        selectImage();
                    }
                });
            }
        } else {

            getSupportActionBar().setTitle("Add Students");

            this.imageButton = (ImageButton) this.findViewById(R.id.profilepic);

            if (imageButton != null) {
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        //startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        selectImage();
                    }
                });
            }

        }

    }

    public void getdata() {
        this.editname = (EditText) this.findViewById(R.id.editname);
        if (editname != null) {
            name = editname.getText().toString();
        }


        editmail = (EditText) findViewById(R.id.editmail);
        if (editmail != null) {
            email = editmail.getText().toString();
        }

        editphone = (EditText) findViewById(R.id.editphone);
        String sphone = "12345";
        if (editphone != null) {
            sphone = editphone.getText().toString();
        }
        phone = !sphone.equals("") ? Integer.parseInt(sphone) : 0;


        editaddress = (EditText) findViewById(R.id.editaddresss);
        if (editaddress != null) {
            address = editaddress.getText().toString();
        }

    }

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageButton.setImageBitmap(photo);
            imageButton.setBackgroundColor(0);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton1) {
            getdata();
            boolean isvalid = validatedata();

            if (isvalid) {


                Realm.init(AddStudent.this);
                Realm realm;
                try {
                    realm = Realm.getDefaultInstance();

                } catch (Exception e) {

                    // Get a Realm instance for this thread
                    RealmConfiguration config = new RealmConfiguration.Builder()
                            .deleteRealmIfMigrationNeeded()
                            .build();
                    realm = Realm.getInstance(config);

                }


                realm.beginTransaction();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                RealmResults<Student> delete = realm.where(Student.class).equalTo("email", email).findAll();
                delete.deleteAllFromRealm();


                newstudent = new Student(name, email, address, phone, byteArray);
                realm.copyToRealm(newstudent);
                realm.commitTransaction();


                Intent intent = new Intent(getBaseContext(), Studentlist.class);
                startActivity(intent);
            }

        }

        if (id == R.id.mybutton) {
            Intent back = new Intent(getBaseContext(), Studentlist.class);
            startActivity(back);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validatedata() {

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getBaseContext(), "invalid email",
                    Toast.LENGTH_LONG).show();
            return false;

        }

        if (String.valueOf(phone).length() <= 8) {
            Toast.makeText(getBaseContext(), "invalid number",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (photo == null) {
            Toast.makeText(getBaseContext(), "select a valid picture",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void selectImage() {

            PackageManager pm = getPackageManager();
        ActivityCompat.requestPermissions(AddStudent.this,
                new String[]{Manifest.permission.CAMERA},
                1);


                TextView title = new TextView(getBaseContext());
                title.setText("Add Photo!");
                title.setBackgroundColor(Color.BLACK);
                title.setPadding(10, 15, 15, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(22);

                  AlertDialog.Builder builder = new AlertDialog.Builder(
                 AddStudent.this);

                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};

                builder.setCustomTitle(title);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public static final int SELECT_PICTURE = 0;

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            //dialog.dismiss();
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                           // dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                imageButton.setImageBitmap(photo);
                imageButton.setBackgroundColor(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageButton.setImageBitmap(photo);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(AddStudent.this, "Permission denied to open Camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}