package de.ramelsberger.lmu.smartremoteapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fabian on 13.11.2015.
 */
public class DetailsView extends Activity {

    public static int lastAddedPosition = 0;

    private DetailsView thisActivity;
    private Spinner iconSpinner;
    private Spinner buttonPositionSpinner;
    private TextView iconView;

    private int[] productImages = {R.drawable.coffee_icon, R.drawable.light_icon};
    private String[] iconArray;

    private int currentProductImage = 0;
    private int selectedPosition;
    private Spinner deviceSpinner;
    private Spinner deviceActionSpinner;

    private String[] deviceString;
    public static int maxPos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view_layout);

        thisActivity = this;
        iconArray = getResources().getStringArray(R.array.icon_array);


        final Button backButton = (Button) findViewById(R.id.detailsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(thisActivity, ButtonChooser.class);
                startActivityForResult(intent1,3);
            }
        });

        final Button acceptButton = (Button) findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ButtonObject buttonObject = new ButtonObject(currentProductImage, selectedPosition, iconView.getText().toString(), "TODO");
                Intent intent1 = new Intent(thisActivity, MainActivity.class);
                Bundle b;
                if(intent1.getExtras()!=null) {
                    b = intent1.getExtras();
                }else
                {
                    b= new Bundle();
                }

                b.putSerializable("newButtonObject", buttonObject);
                int highestPosition=buttonObject.getButtonPosition();
                if(highestPosition<maxPos)
                {
                    maxPos=highestPosition;
                    //highestPosition=b.getInt("highestPosition");
                }else{

                }

                intent1.putExtras(b);
                //if (lastAddedPosition < 5)//sets the button on the next space
                lastAddedPosition++;
                //else
                 //   lastAddedPosition = 0;
                startActivity(intent1);

            }
        });

        iconView = (TextView) thisActivity.findViewById(R.id.imageViewNewButtonIcon);
        //editTextButtonText=(EditText)thisActivity.findViewById(R.id.edit_text_buttonName);
        //editTextActionText=(EditText)thisActivity.findViewById(R.id.edit_text_ActionName);


        iconSpinner = (Spinner) thisActivity.findViewById(R.id.spinner);

        ArrayAdapter adapter = new SpinnerAdapter(this, R.layout.simple_image_spinner_layout, iconArray, productImages);
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



        ArrayList<String> positions=new ArrayList<String>();
        int multiply=1;

        for (int i=0;i<lastAddedPosition;i++)
        {
            if(i+1==6){
                multiply++;
            }
        }
        for (int i =0;i<multiply*6;i++){
            positions.add("Position "+i);
        }

        ArrayAdapter textAdapter = new ArrayAdapter(this, R.layout.simple_spinner_layout, positions);
        buttonPositionSpinner.setAdapter(textAdapter);
        if(buttonPositionSpinner.getChildCount()<=lastAddedPosition-1)
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

        //Add variables from the button chooser
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deviceString = extras.getStringArray("TestString");
        }
        int deviceNumber = extras.getInt("deviceNumber");
        int actionNumber = extras.getInt("actionNumber");
        String iconString =extras.getString("IconString");

        ArrayAdapter deviceTextAdapter = new ArrayAdapter(this, R.layout.simple_spinner_layout, deviceString);

        deviceSpinner = (Spinner) thisActivity.findViewById(R.id.deviceSpinner);
        deviceSpinner.setAdapter(deviceTextAdapter);
        deviceSpinner.setSelection(deviceNumber);

        deviceActionSpinner = (Spinner) thisActivity.findViewById(R.id.deviceActionSpinner);
        deviceActionSpinner.setAdapter(textAdapter);
        deviceActionSpinner.setSelection(actionNumber);

        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        iconView.setTypeface(fontAwesomeFont);
        iconView.setText(iconString);
    }


}
