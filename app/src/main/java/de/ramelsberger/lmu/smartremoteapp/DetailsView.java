package de.ramelsberger.lmu.smartremoteapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Fabian on 13.11.2015.
 */
public class DetailsView extends Activity {

    public static int lastAddedPosition = 0;

    private DetailsView thisActivity;
    private Spinner iconSpinner;
    private Spinner buttonPositionSpinner;
    private ImageView iconView;
    private Button backButton;
    private Button acceptButton;

    private String[] iconArray;

    private int currentProductImage = 0;
    private int selectedPosition;
    private Spinner deviceSpinner;
    private Spinner deviceActionSpinner;

    private String[] deviceString;
    public static int maxPos=0;
    private String[] actionArray;

    //-----------------
    private int[] icons;
    private int actionNumber;
    private int deviceNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view_layout);
        thisActivity = this;
        iconArray = getResources().getStringArray(R.array.icon_array);

        initializeViews();
        getExtrasFromButtonChooser();
        initializeListener();

        //Setup the image spinner
        ArrayAdapter adapter = new SpinnerAdapter(this, R.layout.simple_image_spinner_layout, actionArray, icons);
        iconSpinner.setAdapter(adapter);
        iconSpinner.setSelection(actionNumber);

        iconView.setImageResource(icons[actionNumber]);

        ArrayList<String> positions=new ArrayList<String>();
        for (int i =0;i<(ButtonChooser.clickedFragmentID+1)*6;i++){
            positions.add("Position "+(i+1)+" Page "+(i/6+1));
        }

        ArrayAdapter textAdapter = new ArrayAdapter(this, R.layout.simple_spinner_layout, positions);
        buttonPositionSpinner.setAdapter(textAdapter);
        buttonPositionSpinner.setSelection(ButtonChooser.clickedPosition + ButtonChooser.clickedFragmentID * 6);


        ArrayAdapter deviceTextAdapter = new ArrayAdapter(this, R.layout.simple_spinner_layout, deviceString);
        deviceSpinner = (Spinner) thisActivity.findViewById(R.id.deviceSpinner);
        deviceSpinner.setAdapter(deviceTextAdapter);
        deviceSpinner.setSelection(deviceNumber);

        ArrayAdapter actionTextAdapter = new ArrayAdapter(this, R.layout.simple_spinner_layout, actionArray);

        deviceActionSpinner = (Spinner) thisActivity.findViewById(R.id.deviceActionSpinner);
        deviceActionSpinner.setAdapter(actionTextAdapter);
        deviceActionSpinner.setSelection(actionNumber);
    }

    //------------------------json Converter

    public JSONObject toJSON(ButtonObject buttonObject){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name", buttonObject.getDeviceName());
            jsonObject.put("beschreibung", buttonObject.getActionName());
            jsonObject.put("icon", buttonObject.getIconDescription());
            //TODO positions?
            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return jsonObject;
        }

    }

    //-----------------------listeners

    private void initializeListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(thisActivity, ButtonChooser.class);
                setResult(3);
                finish();

            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ButtonObject buttonObject = new ButtonObject(deviceString[deviceNumber],actionArray[actionNumber],
                        getResources().getResourceName(icons[actionNumber]), selectedPosition);
                //Convert object to JSON in Android
                JSONObject jsonButton = toJSON(buttonObject);
                //TODO store actions?

                //
                Intent intent1 = new Intent(thisActivity, MainActivity.class);
                Bundle b;
                if (intent1.getExtras() != null) {
                    b = intent1.getExtras();
                } else {
                    b = new Bundle();
                }
                b.putSerializable("newButtonObject", buttonObject);//ButtonObject wich contains all important information about the button
                //TODO remove putSerialize?

                int highestPosition = buttonObject.getButtonPosition();
                if (highestPosition < maxPos) {
                    maxPos = highestPosition;
                }
                intent1.putExtras(b);
                lastAddedPosition++;
                startActivity(intent1);//TODO for result
            }
        });

        iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentProductImage = iconSpinner.getSelectedItemPosition();
                iconView.setImageResource(icons[currentProductImage]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        buttonPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPosition = (int) id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    //-----------------------Add variables from the button chooser

    private void getExtrasFromButtonChooser() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deviceString = extras.getStringArray("DeviceString");
            actionArray = extras.getStringArray("ActionStrings");
            deviceNumber = extras.getInt("deviceNumber");
            actionNumber = extras.getInt("actionNumber");
            icons = (int[])extras.get("IconDrawable");
        }
    }

    private void initializeViews() {
        backButton = (Button) findViewById(R.id.detailsBackButton);
        acceptButton = (Button) findViewById(R.id.accept_button);
        iconView = (ImageView) thisActivity.findViewById(R.id.imageViewNewButtonIcon);
        iconSpinner = (Spinner) thisActivity.findViewById(R.id.spinner);
        buttonPositionSpinner = (Spinner) thisActivity.findViewById(R.id.positionSpinner);
    }
}
