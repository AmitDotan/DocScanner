package com.prj.imagescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home_page extends AppCompatActivity {

    Button btn_Picture, btn_PicturesList, btn_Instructions, btn_Exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btn_Picture = (Button) findViewById(R.id.btn_Picture);
        btn_PicturesList = (Button) findViewById(R.id.btn_PicturesList);
        btn_Instructions = (Button) findViewById(R.id.btn_Instructions);
        btn_Exit = (Button) findViewById(R.id.btn_Exit);

        btn_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(home_page.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        btn_PicturesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(home_page.this, PicturesList.class);
                startActivity(myIntent);
                finish();
            }
        });

        btn_Instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(home_page.this, instructions.class);
                startActivity(myIntent);
                finish();
            }
        });

        btn_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });


    }

    @Override
    public void onBackPressed() {
        // your code.
    }


}