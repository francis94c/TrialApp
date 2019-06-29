package com.cynobit.trialapp;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.cynobit.trialapp.adapters.GridAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        File root = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + "/trial_app/");
        FilenameFilter imageFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg");
            }
        };
        GridAdapter adapter = new GridAdapter(Arrays.asList(root.listFiles(imageFilter)));
        ((GridView) findViewById(R.id.gridView)).setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void gridItem_Click(View view) {
        if (!view.getTag().toString().equals("NO:IMAGE")) {
            Intent pictureIntent = new Intent(getApplicationContext(), PictureActivity.class);
            pictureIntent.putExtra("absolute", true);
            pictureIntent.putExtra("image", view.getTag().toString());
            startActivity(pictureIntent);
        } else {
            Toast.makeText(getApplicationContext(), "No Image!", Toast.LENGTH_SHORT).show();
        }
    }
}
