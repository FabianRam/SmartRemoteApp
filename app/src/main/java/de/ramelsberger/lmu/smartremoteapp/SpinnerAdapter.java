package de.ramelsberger.lmu.smartremoteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<String> {
    ArrayList<String> items;
    ArrayList<String> images;
    Context context;
    int rid;
    TextView textview;
    ImageView imageview;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, int rid, ArrayList<String> items,ArrayList<String> images) {
        super(context, rid, items);
        this.items = items;
        this.context = context;
        this.rid = rid;
        this.images=images;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return items.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertview, ViewGroup parent) {

        if (convertview == null) {
            convertview = inflater.inflate(R.layout.simple_image_spinner_layout, parent, false);
            textview = (TextView) convertview
                    .findViewById(R.id.spinneritem_txt);
            imageview = (ImageView) convertview
                    .findViewById(R.id.spinneritem_iv);


            String separatedString = images.get(position).substring(0, images.get(position).lastIndexOf('.'));
            int resId = getContext().getResources().getIdentifier(separatedString, "drawable", getContext().getPackageName());
            imageview.setBackgroundResource(resId);
            textview.setText(items.get(position));

        }

        return convertview;
    }

}
