package com.cynobit.trialapp.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cynobit.trialapp.R;

public class ListAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 10;
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
            view = inflater.inflate(R.layout.list_view_item, parent, false);
        }
        if (position == 1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((TextView) view.findViewById(R.id.kongaTextView)).setCompoundDrawables(null, null, parent.getContext().getResources().getDrawable(R.mipmap.ic_check, parent.getContext().getTheme()), null);
            } else {
                ((TextView) view.findViewById(R.id.kongaTextView)).setCompoundDrawables(null, null, parent.getContext().getResources().getDrawable(R.mipmap.ic_check), null);
            }
        } else {
            ((TextView) view.findViewById(R.id.kongaTextView)).setCompoundDrawables(null, null, null, null);
        }
        return view;
    }
}
