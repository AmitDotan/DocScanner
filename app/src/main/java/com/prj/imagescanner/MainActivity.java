package com.prj.imagescanner;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Environment.getExternalStorageDirectory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 2296;

    String destPath;
    SQLiteDatabase myDB;
    ProgressDialog mdialog;
    String picPath;
    ImageView pic;

    private static String JPEG_FILE_PREFIX = "";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    String[] perms = {"android.permission.CAMERA"};

    EditText txt_PicPath, txt_ImageName;

    Button btn_Save;
    Button btn_TakePicture;

    private String Document_img1 = "";
    Boolean isRecordSave = false;
    String status;

    String ProjectID, SAID, USERID, YEAR, QUARTER;

    private static final int REQUEST_CODE_PERMISSION = 2;
    Boolean is_permission_granted=false;


    private static final int PERMISSION_LOCATION_REQUEST_CODE = 99;


    TextView tv_RowID;

    String Data_ID;
    int status_resultsCount = 0;
    int pictureNo = 1;


    public static final int RESULT_OK  = -1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String ui,un,em;
    String paramSAID,paramProjectID;
    String paramPage;

    Button btn_Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Home = (Button) findViewById(R.id.btn_Home);

        btn_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, home_page.class);
                startActivity(myIntent);
                finish();
            }
        });

        // Get Overall permissions check for read/write, camera permissions
        getPermission();

        if(checkPermission()) {

        }
        else
        {
            requestPermission();
        }

        if(isWriteStoragePermissionGranted() && isCameraPermissionGranted())
        {
            boolean yes = true;
        }

        setTitle("Picture(s) Collection");

        //this.destPath = (getExternalStorageDirectory().getPath() + "/imgscan/db/psdp.db");
        this.picPath = getExternalStorageDirectory().getPath() + "/imgscan/pics";
        File directory = new File(this.picPath);
        if (! directory.exists()){
            directory.mkdirs();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        // finding controls ---------------------------
        btn_TakePicture = (Button) findViewById(R.id.btn_TakePicture);
        txt_PicPath = (EditText) findViewById(R.id.txt_PicPath);
        pic = (ImageView) findViewById(R.id.pic);
        txt_ImageName = (EditText) findViewById(R.id.txt_ImageName);
        btn_Save = (Button) findViewById(R.id.btn_Save);

        //-----------------------------------------------

        //BindImagesGrid();

        btn_TakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(txt_ImageName)){
                    txt_ImageName.setError("Image Name is required!");
                }
                else{
                    selectImage();
                }
            }
        });

    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this );
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = null;
                    try {
                        f = createImageFile();
                        txt_PicPath.setText(f.getAbsolutePath());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri apkURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", f);
                        //intent.setDataAndType(apkURI, "image/jpg");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, apkURI);
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    }

                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        File localFile = new File(picPath + "/" + txt_ImageName.getText() + ".jpg");
        return localFile;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Matrix matrix = new Matrix();
        pic.setImageBitmap(null);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                File f = new File(txt_PicPath.getText().toString());

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);

                    pic.setImageBitmap(bitmap);

                    Toast.makeText(this, "Image Saved Succesfully in ImageScan folder on storage",Toast.LENGTH_LONG).show();

                    BitMapToString(bitmap);
                    String path = txt_PicPath.getText().toString();
                    //f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }




    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);


        }
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    public void getPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA }, 1024);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {

        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (SDK_INT >= 23) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //custom_toast("Permission is granted2");
            return true;
        }
    }

    public  boolean isCameraPermissionGranted() {
        if (SDK_INT >= 23) {
            if (checkSelfPermission(CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                CAMERA },
                        PERMISSION_LOCATION_REQUEST_CODE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //custom_toast("Permission is granted2");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission: "+permissions[0]+ "was "+grantResults[0], Toast.LENGTH_LONG).show();
                }else{
                }
                break;
            case 2296:
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        // perform action when allow permission success
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        // your code.
        Intent myIntent = new Intent(MainActivity.this, home_page.class);
        startActivity(myIntent);
        finish();
    }


}