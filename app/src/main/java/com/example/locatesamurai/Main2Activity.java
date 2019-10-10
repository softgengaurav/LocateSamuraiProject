package com.example.locatesamurai;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView entry_value = (TextView) findViewById(R.id.entry_value);
        Intent second_intent = getIntent();

        final String msg = second_intent.getStringExtra("ITEM_SELECTED");
        entry_value.setText(msg);
        // ListEntry entry = (ListEntry) parent.getItemAtPosition(position);
        btn = findViewById(R.id.plot_button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                Toast.makeText(getApplicationContext(),"Hello World", Toast.LENGTH_SHORT).show();
                Intent intent_maps = new Intent(Main2Activity.this, MapsActivity.class);
                intent_maps.putExtra("ITEM_SELECTED", msg);
                startActivity(intent_maps);
            }
        });
        }

    }

