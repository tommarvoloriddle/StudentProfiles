package com.example.saki.schoool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by padma on 01-04-2017.
 */
public class Studentadapter extends ArrayAdapter<Student> {


    public Context thiscontext;

    RealmResults<Student> students;
    public Studentadapter(Activity context, RealmResults<Student> students) {

        super(context, 0, students);
        this.thiscontext = context;
        this.students = students;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        int type = getItemViewType(position);


        View listitem = view;
        if (listitem == null) {
            if(students.size() >0) {
                listitem = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item, parent, false
                );
            }
            else{
                listitem = LayoutInflater.from(getContext()).inflate(
                        R.layout.headerlayout, parent, false
                );
            }
        }


        final Student student = (Student) getItem(position);


        TextView name = (TextView) listitem.findViewById(R.id.name);
        name.setText(student.getname());

        TextView phone = (TextView) listitem.findViewById(R.id.phone);
        phone.setText(String.valueOf(student.getphone()));

        TextView email = (TextView) listitem.findViewById(R.id.email);
        email.setText(student.getemail());

        TextView address = (TextView) listitem.findViewById(R.id.address);
        address.setText(String.valueOf(student.getaddress()));

        ImageView image = (ImageView) listitem.findViewById(R.id.imageView);
        image.setImageBitmap(student.getimage());

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thiscontext, AddStudent.class);

                intent.putExtra("from" , "true");
                intent.putExtra("email" , student.getemail());



                thiscontext.startActivity(intent);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addressString = student.getaddress();

              Uri addressUri = Uri.parse("geo:0,0?q=" + addressString);
    /*
    Intent to open the map
     */
                Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);

    /*
    verify if the devise can launch the map intent
     */
                if (intent.resolveActivity(thiscontext.getPackageManager()) != null) {
       /*
       launch the intent
        */
                    thiscontext.startActivity(intent);
                }
            }
        });

        return listitem;
    }

}



