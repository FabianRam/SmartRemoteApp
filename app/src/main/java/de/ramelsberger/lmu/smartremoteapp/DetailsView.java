package de.ramelsberger.lmu.smartremoteapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Fabian on 13.11.2015.
 */
public class DetailsView extends Activity{

    private static int lastAddedPosition=0;

    private DetailsView thisActivity;
    private Spinner iconSpinner;
    private Spinner buttonPositionSpinner;
    private EditText editTextActionText;
    private EditText editTextButtonText;
    private ImageView iconView;

    private int[] productImages ={R.drawable.coffee_icon,R.drawable.light_icon,R.drawable.music_icon,R.drawable.standlight_blue,R.drawable.standlight_green,R.drawable.standlight_turkies,R.drawable.standlight_turkies,R.drawable.standlight_red};
    private String[] iconArray;

    private int currentProductImage=0;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view_layout);

        thisActivity=this;
        iconArray = getResources().getStringArray(R.array.icon_array);

        final Button button = (Button) findViewById(R.id.accept_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ButtonObject buttonObject = new ButtonObject(currentProductImage,selectedPosition,editTextButtonText.getText().toString(), editTextActionText.getText().toString());

                Intent intent1 = new Intent(thisActivity, MainActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("newButtonObject", buttonObject);
                intent1.putExtras(b);
                if(lastAddedPosition<5)//sets the button on the next space
                    lastAddedPosition++;
                else
                    lastAddedPosition=0;
                startActivity(intent1);

            }
        });

        iconView = (ImageView) thisActivity.findViewById(R.id.imageViewNewButtonIcon);
        editTextButtonText=(EditText)thisActivity.findViewById(R.id.edit_text_buttonName);
        editTextActionText=(EditText)thisActivity.findViewById(R.id.edit_text_ActionName);
        
        iconSpinner = (Spinner) thisActivity.findViewById(R.id.spinner);
        iconSpinner.setAdapter(new MyAdapter(thisActivity, R.layout.row, iconArray));

                iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        currentProductImage = productImages[position];
                        iconView.setImageResource(currentProductImage);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });

        buttonPositionSpinner = (Spinner) thisActivity.findViewById(R.id.positionSpinner);
        buttonPositionSpinner.setSelection(lastAddedPosition);
        buttonPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPosition =(int)id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId,   String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.rowIdIconName);

            label.setText(iconArray[position]);

            ImageView icon=(ImageView)row.findViewById(R.id.rowIconImage);
            icon.setImageResource(productImages[position]);

            return row;
        }
    }

}
