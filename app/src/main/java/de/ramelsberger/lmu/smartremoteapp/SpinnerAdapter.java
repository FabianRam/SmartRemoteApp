package de.ramelsberger.lmu.smartremoteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {
    String[] items;
    int[] images;
    Context context;
    int rid;
    TextView textview;
    ImageView imageview;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, int rid, String[] items,int[] images) {
        super(context, rid, items);
        this.items = items;
        this.context = context;
        this.rid = rid;
        this.images=images;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        return items.length;
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
            imageview.setBackgroundResource(images[position]);
            textview.setText(items[position]);

        }

        return convertview;
    }

}
