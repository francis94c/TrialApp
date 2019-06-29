package com.cynobit.trialapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cynobit.trialapp.adapters.ListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ((TextView) findViewById(R.id.resultsTextView)).setTypeface(Typeface.DEFAULT_BOLD);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.pictureImageView).setClipToOutline(true);
        }
        ListAdapter adapter = new ListAdapter();
        ((ListView)findViewById(R.id.listView)).setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (!getIntent().hasExtra("image")) return;
        String fileName = getIntent().getStringExtra("image");
        if (fileName.equals("")) return;
        File imageFile;
        if (!getIntent().getBooleanExtra("absolute", false)) {
            imageFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString() + "/trial_app/", fileName);
        } else {
           imageFile = new File(fileName);
        }
        if (!imageFile.exists()) return;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageFile);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            ((ImageView) findViewById(R.id.pictureImageView)).setImageBitmap(bitmap);
            fis.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Reading Image File", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            try {
                assert fis != null;
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void back_Click(View view) {
        finish();
    }
}
