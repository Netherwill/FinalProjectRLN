package com.example.rln.finalprojectrln;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yicheng Zhao on 2/11/2016.
 */
public class registration extends AppCompatActivity {

    final String albumName = "@string/app_name";
    File imageFile;
    //SQLiteDatabase db;
    //String IMAGEPATH="";
    //String CAPTION="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        EditText phone_number = (EditText) findViewById(R.id.Phone_Number);
        EditText age = (EditText) findViewById(R.id.Age);
        EditText gender = (EditText) findViewById(R.id.Gender);
        EditText position = (EditText) findViewById(R.id.Position);
        Button take_protrait = (Button) findViewById(R.id.take_a_protrait);
        Button register = (Button) findViewById(R.id.Register);


        take_protrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent_1.resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(getApplicationContext(), "Cannot take pictures on this device!", Toast.LENGTH_SHORT).show();
                    return;
                }
                imageFile = createImageFile();
                intent_1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(intent_1, 1234);}

        });

        register.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               Intent intent_2 = new Intent(registration.this,menu.class);
               // should store all the registration data into the database and jump to menu.
               Toast.makeText(registration.this, "Registration Complete", Toast.LENGTH_SHORT).show();
               startActivity(intent_2);
           }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 1234) return;

        if (resultCode != Activity.RESULT_OK) {
            imageFile.delete();
            return;
        }

        try {
            InputStream is = new FileInputStream(imageFile);
            ImageView protrait_image = (ImageView) findViewById(R.id.Protrait_image);
            //ImageView imageView = (ImageView) findViewById(R.id.imageView);
            protrait_image.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //IMAGEPATH = imageFile.toString();
    }

    private File createImageFile() {
        File image = null;
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getAlbumStorageDir();
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

        } catch (Exception e) {
            Log.e("Leon", "failed to create image file.  We will crash soon!");
            // we should do some meaningful error handling here !!!
        }
        return image;
    }

    public File getAlbumStorageDir() {
        // Same as Environment.getExternalStorageDirectory() + "/Pictures/" + albumName
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (file.exists()) {
            Log.d("Leon", "Album directory exists");
        } else if (file.mkdirs()) {
            Log.i("Leon", "Album directory is created");
        } else {
            Log.e("Leon", "Failed to create album directory.  Check permissions and storage.");
        }
        return file;
    }
}

