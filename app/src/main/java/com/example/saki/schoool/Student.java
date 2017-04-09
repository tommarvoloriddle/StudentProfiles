package com.example.saki.schoool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by padma on 01-04-2017.
 */
public class Student extends RealmObject implements Serializable{

@PrimaryKey
   private String email;



    private int phone;
   private String address;

    private byte[] image;

    private String name;


    public Student()
    {

    }


    public Student(String iname , String imail , String iaddress , int iphone , byte[] images )
    {
        name = iname;
        email = imail;
        address = iaddress;
        phone = iphone;
        image = images;

    }




    public String getname() {

        return name;
    }

    public int getphone() {
        return phone;
    }

    public String getemail() {
        return email;
    }

    public String getaddress() {
        return address;
    }

    public Bitmap getimage() {

       Bitmap bitmap = BitmapFactory.decodeByteArray(image , 0 , image.length);
        return bitmap;
    }
}
