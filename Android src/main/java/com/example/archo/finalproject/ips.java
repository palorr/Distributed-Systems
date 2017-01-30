package com.example.archo.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ips extends AppCompatActivity {
    private Button submit;
    EditText i1;
    EditText i2;
    EditText i3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ips);
        ///
        i1=(EditText)findViewById(R.id.ip1);
        i2=(EditText)findViewById(R.id.ip2);
        i3=(EditText)findViewById(R.id.ip3);
        SharedPreferences shared = getSharedPreferences("info",MODE_PRIVATE);
        i1.setText(shared.getString("ip1",null));
        i2.setText(shared.getString("ip2",null));
        i3.setText(shared.getString("ip3",null));
        ///
        submit = (Button) findViewById(R.id.gtm);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String ip1 = i1.getText().toString();
                final String ip2 = i2.getText().toString();
                final String ip3 = i3.getText().toString();
                //
                Intent i = new Intent(ips.this, MapsActivity.class);
                i.putExtra("ip1", ip1);
                i.putExtra("ip2", ip2);
                i.putExtra("ip3", ip3);
                startActivity(i);

            }
        });













    }
}
