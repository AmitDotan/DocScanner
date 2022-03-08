package com.prj.imagescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class instructions extends AppCompatActivity {

    Button btn_Home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        btn_Home = (Button) findViewById(R.id.btn_Home);

        btn_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(instructions.this, home_page.class);
                startActivity(myIntent);
                finish();
            }
        });

        ((TextView)findViewById(R.id.tv_Instructions)).setText(
                "------\n" +
                "\n" +
                "1.\t   Page 1: Splash Screen:\n" +
                "\n" +
                "\t   When user clicks on app icon Splash Screen appears, that automatically redirects after some time to Home Page :\n" +

                "\n" +
                "\n" +
                "2.\t   Page 2: Home Page\n" +
                "------------\n" +
                "\n" +
                "\n" +
                "      Contains Main Menu and there are four buttons in main menu which are self explanatory.\n" +
                "\n" +
                "3.      Page 3: Picture Taking Page\n" +
                        "\n" +
                        "\n" +
                "\t\t    1. Give All asked permissions like File Read/Write, File Manage, Camera .\n" +
                "\t\t    2. Type image name\n" +
                "\n" +
                        "\n" +
                "\t    3. Take Picture Form Camera, The Picture will then be automatically saved\n" +

                "4.      Page 4: Pictures List Page\n" +
                "\t\t    1. The saved pictures taken using camera will be displayed in a list .\n" +
                "\t\t    2. On Clicking over the image name, The image will be opened for preview\n" +
                "\n" +
                "5.      Page 5: Insructions Page\n" +
                "\t\t    1. The saved pictures taken using camera will be displayed in a list .\n" +
                "\t\t    2. On Clicking over the image name, The image will be opened for preview\n"


        );
    }

    @Override
    public void onBackPressed() {
        // your code.
        Intent myIntent = new Intent(instructions.this, home_page.class);
        startActivity(myIntent);
        finish();
    }
}