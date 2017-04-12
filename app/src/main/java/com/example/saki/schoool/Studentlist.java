package com.example.saki.schoool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.saki.schoool.R.layout.headerlayout;

public class Studentlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_studentlist);

           // ListOfStudents listOfStudents = new ListOfStudents();

           // ArrayList<Student> students = new ArrayList<Student>();

        ListView lv = (ListView) findViewById(R.id.studentlist);

        LayoutInflater listinflator = getLayoutInflater();
        ViewGroup header = (ViewGroup) listinflator.inflate(R.layout.headerlayout ,lv ,false);
        lv.addHeaderView(header,null,false);

        getSupportActionBar().setTitle("Students");


        Button addstudent = (Button) findViewById(R.id.addstudent);

        if (addstudent != null) {
            addstudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addstudentsintent = new Intent(getBaseContext(), AddStudent.class);
                    startActivity(addstudentsintent);
                }
            });
        }

        Realm.init(this);

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Student> students = realm.where(Student.class).findAll();





        if(students.size() == 0){

               TextView nostudents = (TextView) findViewById(R.id.nostudents);
               nostudents.setText("No Students Found in School");
           }

        else {

               Studentadapter studentadapter = new Studentadapter(this, students);
               ListView studentslistview = (ListView) findViewById(R.id.studentlist);
            if (studentslistview != null) {
                studentslistview.setItemsCanFocus(true);
            }
            studentslistview.setAdapter(studentadapter);




           }

    }
}
