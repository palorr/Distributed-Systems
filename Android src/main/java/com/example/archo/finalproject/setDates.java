package com.example.archo.finalproject;
//
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//
public class setDates extends AppCompatActivity {
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dates);

        submit = (Button) findViewById(R.id.gtm);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText efd=(EditText)findViewById(R.id.firstdate);
                EditText esd=(EditText)findViewById(R.id.seconddate);
                final String fd = efd.getText().toString();
                final String sd = esd.getText().toString();
                //
                Intent i = new Intent(setDates.this, MapsActivity.class);
                i.putExtra("fd", fd);
                i.putExtra("sd", sd);
                startActivity(i);

            }
        });
    }

}