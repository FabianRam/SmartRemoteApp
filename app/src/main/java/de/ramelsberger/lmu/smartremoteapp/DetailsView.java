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
public class DetailsView extends Activity {

    private static int lastAddedPosition = 0;

    private DetailsView thisActivity;
    private Spinner iconSpinner;
    private Spinner buttonPositionSpinner;
    private EditText editTextActionText;
    private EditText editTextButtonText;
    private TextView iconView;

    private int[] productImages = {R.drawable.coffee_icon, R.drawable.light_icon, R.drawable.music_icon, R.drawable.standlight_blue, R.drawable.standlight_green, R.drawable.standlight_turkies, R.drawable.standlight_turkies, R.drawable.standlight_red};
    private String[] iconArray;

    private int currentProductImage = 0;
    private int selectedPosition;
    private Spinner deviceSpinner;
    private Spinner deviceActionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view_layout);

        thisActivity = this;
        iconArray = getResources().getStringArray(R.array.icon_array);

        final Button button = (Button) findViewById(R.id.accept_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ButtonObject buttonObject = new ButtonObject(currentProductImage, selectedPosition, "TODO", "TODO");

                Intent intent1 = new Intent(thisActivity, MainActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("newButtonObject", buttonObject);
                intent1.putExtras(b);
                if (lastAddedPosition < 5)//sets the button on the next space
                    lastAddedPosition++;
                else
                    lastAddedPosition = 0;
                startActivity(intent1);

            }
        });

        iconView = (TextView) thisActivity.findViewById(R.id.imageViewNewButtonIcon);
        //editTextButtonText=(EditText)thisActivity.findViewById(R.id.edit_text_buttonName);
        //editTextActionText=(EditText)thisActivity.findViewById(R.id.edit_text_ActionName);


        iconSpinner = (Spinner) thisActivity.findViewById(R.id.spinner);

        ArrayAdapter adapter = new SpinnerAdapter(this, R.layout.simple_image_spinner_layout,iconArray,productImages);
        iconSpinner.setAdapter(adapter);
        //iconSpinner.setAdapter(new MyAdapter(this, R.layout.simple_spinner_layout, iconArray));

        iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentProductImage = productImages[position];
                //iconView.setImageResource(currentProductImage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        buttonPositionSpinner = (Spinner) thisActivity.findViewById(R.id.positionSpinner);
        String[] positions =  {"Position 1","Position 2","Position 3","Position 4","Position 5","Position 6"};
        ArrayAdapter textAdapter = new ArrayAdapter(this, R.layout.simple_spinner_layout,positions);
        buttonPositionSpinner.setAdapter(textAdapter);
        buttonPositionSpinner.setSelection(lastAddedPosition);
        buttonPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPosition = (int) id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        deviceSpinner = (Spinner) thisActivity.findViewById(R.id.deviceSpinner);
        deviceSpinner.setAdapter(textAdapter);
        deviceActionSpinner = (Spinner) thisActivity.findViewById(R.id.deviceActionSpinner);
        deviceActionSpinner.setAdapter(textAdapter);

    }



}
