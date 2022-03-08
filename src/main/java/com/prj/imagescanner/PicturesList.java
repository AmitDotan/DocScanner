package com.prj.imagescanner;

import static android.os.Environment.getExternalStorageDirectory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PicturesList extends AppCompatActivity {

    Button btn_Home;
    private GridViewAdapter customGridAdapter;
    private GridView gridView;
    String picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures_list);
        gridView = (GridView) findViewById(R.id.gridView);

        this.picPath = getExternalStorageDirectory().getPath() + "/imgscan/pics";

        btn_Home = (Button) findViewById(R.id.btn_Home);

        btn_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(PicturesList.this, home_page.class);
                startActivity(myIntent);
                finish();
            }
        });

        BindImagesGrid();
    }

    private void BindImagesGrid()
    {

        customGridAdapter = new GridViewAdapter(getApplicationContext(), R.layout.row_grid, getData());
        gridView.setAdapter(customGridAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Matrix matrix = new Matrix();
                //Toast.makeText(getContext(),
                //      (v.getId, Toast.LENGTH_SHORT).show();

                if (v instanceof ImageView) {

                    //String st = picPath + ((ImageItem) parent.getItemAtPosition(position)).getImagename();
                    String st = ((ImageItem) parent.getItemAtPosition(position)).getImagename();
                    Bitmap bmap = BitmapFactory.decodeFile(picPath +"/"+ st);
                    //Bitmap bmap = ((ImageItem)parent.getItemAtPosition(position)).getImage();
                    if(bmap!=null) {
                        final Dialog nagDialog = new Dialog(PicturesList.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        nagDialog.setCancelable(false);
                        nagDialog.setContentView(R.layout.preview_image);
                        Button btnClose = (Button) nagDialog.findViewById(R.id.btnIvClose);
                        ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_preview_image);
                        matrix.postRotate(90);

                        Bitmap bMapRotate = Bitmap.createBitmap(bmap, 0, 0, bmap.getWidth(), bmap.getHeight(), matrix, true);
                        ivPreview.setImageBitmap(bMapRotate);
                        //ivPreview.setImageBitmap(((ImageItem)parent.getItemAtPosition(position)).getImage());
                        ivPreview.setBackgroundDrawable(getResources().getDrawable(R.drawable.shapes_border));

                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {

                                nagDialog.dismiss();
                            }
                        });

                        nagDialog.show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Image physically not available on device",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();

        final List<ImageItem> results = new ArrayList<ImageItem>();

        File[] files = new File(picPath).listFiles();

        ArrayList localArrayList = new ArrayList();
        localArrayList.add("ImageName");

        try
        {

            if(files!=null) {
                for (File file : files) {
                    if (file.isFile()) {
                        //results.add(file.getName());

                        gridView.setVisibility(LinearLayout.VISIBLE);

                        String filename = file.getName();
                        Bitmap bitmap = BitmapFactory.decodeFile(picPath +"/"+ filename);
                        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
                        imageItems.add(new ImageItem(bitmap, filename));
                        //int imageNo = 0;
                        //imageNo = 1;
                        //imageNo++;
                        //imageItems.add(new ImageItem(bitmap, "Image# "+imageNo++, "("+ "usman" +")","imageNo",filename));

                    } else {
                        gridView.setVisibility(LinearLayout.GONE);
                    }
                }
            }
        }
        catch (SQLiteException localSQLiteException)
        {
            Toast.makeText(getApplicationContext(), localSQLiteException.toString(),Toast.LENGTH_LONG).show();
        }



        return imageItems;
    }

    @Override
    public void onBackPressed() {

        Intent myIntent = new Intent(PicturesList.this, home_page.class);
        startActivity(myIntent);
        finish();
    }

}