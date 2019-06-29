package com.cynobit.trialapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cynobit.trialapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private List<File> imageFiles = new ArrayList<>();

    public GridAdapter (List<File> imageFiles) {
        if (imageFiles != null && imageFiles.size() > 0) this.imageFiles = imageFiles;
    }

    @Override
    public int getCount() {
        return imageFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.grid_item, parent, false);
        }
        File imageFile = imageFiles.get(position);
        if (imageFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imageFile);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                ((ImageView) view).setImageBitmap(bitmap);
                fis.close();
            } catch (Exception e) {
                ((ImageView) view).setImageResource(R.mipmap.me);
                e.printStackTrace();
            } finally {
                try {
                    assert fis != null;
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            view.setTag(imageFile.getAbsolutePath());
        } else {
            ((ImageView) view).setImageResource(R.mipmap.me);
            view.setTag("NO:IMAGE");
        }
        return view;
    }
}
