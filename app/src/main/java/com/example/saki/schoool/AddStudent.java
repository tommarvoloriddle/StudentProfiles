package com.example.saki.schoool;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AddStudent extends AppCompatActivity {


    ImageButton imageButton;
    Student newstudent;
    String address  ;
    String name ;
    String email,bool;
    Bitmap photo ;
    EditText editname ;
    EditText editmail ;
    EditText editphone ;
    EditText editaddress ;
    int phone = 0;
    private static final int CAMERA_REQUEST = 1888;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        Intent intent = getIntent();

        bool = intent.getStringExtra("from");
    if(Objects.equals(bool, "true")) {

        getSupportActionBar().setTitle("Edit Student");
        imageButton = (ImageButton) findViewById(R.id.profilepic);
        editaddress = (EditText) findViewById(R.id.editaddresss);
        editphone = (EditText) findViewById(R.id.editphone);
        editmail = (EditText) findViewById(R.id.editmail);
        editname = (EditText) findViewById(R.id.editname);

        address = intent.getStringExtra("address");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        String phone1 = intent.getStringExtra("phone");
        byte[] temp = intent.getByteArrayExtra("image");
        if (temp != null)
            photo = BitmapFactory.decodeByteArray(temp, 0, temp.length);

        if(phone1 == null){
            Toast.makeText(getBaseContext(), "phone is 0",
                    Toast.LENGTH_LONG).show();
        }

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
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            });
        }
    }

        else {

        getSupportActionBar().setTitle("Add Students");

            this.imageButton = (ImageButton) this.findViewById(R.id.profilepic);

            if (imageButton != null) {
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });
            }

        }

    }
   public void getdata()
    {
         this.editname = (EditText)this.findViewById(R.id.editname);
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
         phone = !sphone.equals("")?Integer.parseInt(sphone) : 0;


         editaddress = (EditText) findViewById(R.id.editaddresss);
        if (editaddress != null) {
            address = editaddress.getText().toString();
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
             photo = (Bitmap) data.getExtras().get("data");
            imageButton.setImageBitmap(photo);
            imageButton.setBackgroundColor(0);
        }
    }

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

                if(isvalid) {
                    Realm.init(AddStudent.this);

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

                    RealmResults<Student> delete = realm.where(Student.class).equalTo("email" , email).findAll();
                    delete.deleteAllFromRealm();


                    newstudent = new Student(name, email, address, phone, byteArray);
                    realm.copyToRealm(newstudent);
                    realm.commitTransaction();


                    Intent intent = new Intent(getBaseContext(), Studentlist.class);
                    startActivity(intent);
                }

        }

        if(id == R.id.mybutton){
            Intent back = new Intent(getBaseContext(),Studentlist.class);
            startActivity(back);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validatedata() {

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getBaseContext(), "invalid email",
                    Toast.LENGTH_LONG).show();
            return false;

        }

        if(String.valueOf(phone).length()<=8){
            Toast.makeText(getBaseContext(), "invalid number",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if(photo == null){
            Toast.makeText(getBaseContext(),"select a valid picture",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
