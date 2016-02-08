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

    private ArrayList<String> deviceString;
    public static int maxPos=0;
    private ArrayList<String> actionArray;

    //-----------------
    private ArrayList<String> icons;
    private int actionNumber;
    private int deviceNumber;
    private String actionDescription="";//TODO
    private String deviceID;
    private String proposalId;
    private ArrayList<String> actionNames;
    private String userId;


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
        ArrayAdapter<String> adapter = new SpinnerAdapter(this, R.layout.simple_image_spinner_layout, actionNames, icons);
        iconSpinner.setAdapter(adapter);
        iconSpinner.setSelection(actionNumber);

        String separatedString = icons.get(actionNumber).substring(0, icons.get(actionNumber).lastIndexOf('.'));
        int resId = getResources().getIdentifier(separatedString, "drawable", getPackageName());
        iconView.setImageResource(resId);

        ArrayList<String> positions=new ArrayList<>();
        for (int i =0;i<(ButtonChooserAktivity.clickedFragmentID+1)*6;i++){
            positions.add("Position "+(i+1)+" Page "+(i/6+1));
        }

        ArrayAdapter<String> textAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_layout, positions);
        buttonPositionSpinner.setAdapter(textAdapter);
        buttonPositionSpinner.setSelection(ButtonChooserAktivity.clickedPosition + ButtonChooserAktivity.clickedFragmentID * 6);


        ArrayAdapter<String> deviceTextAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_layout, deviceString);
        deviceSpinner = (Spinner) thisActivity.findViewById(R.id.deviceSpinner);
        deviceSpinner.setAdapter(deviceTextAdapter);
        deviceSpinner.setSelection(deviceNumber);

        ArrayAdapter<String> actionTextAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_layout, actionArray);

        deviceActionSpinner = (Spinner) thisActivity.findViewById(R.id.deviceActionSpinner);
        deviceActionSpinner.setAdapter(actionTextAdapter);
        deviceActionSpinner.setSelection(actionNumber);
    }


    //-----------------------listeners

    private void initializeListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(thisActivity, ButtonChooserAktivity.class);
                setResult(3);
                finish();

            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//TODO
                ButtonObject buttonObject = new ButtonObject("1",deviceID,deviceString.get(deviceNumber),actionNames.get(actionNumber),actionArray.get(actionNumber),//
                       icons.get(actionNumber), selectedPosition,proposalId);
                //Convert object to JSON in Android
                JSONObject jsonButton = buttonObject.toJSON();


                Intent intent1 = new Intent(thisActivity, MainActivity.class);
                Bundle b;
                if (intent1.getExtras() != null) {
                    b = intent1.getExtras();
                } else {
                    b = new Bundle();
                }
                b.putSerializable("newButtonObject", buttonObject);//ButtonObject wich contains all important information about the button
                b.putString("newButtonJSON",jsonButton.toString());
                //TODO remove putSerialize?


                int highestPosition = buttonObject.getButtonPosition();
                if (highestPosition < maxPos) {
                    maxPos = highestPosition;
                }
                intent1.putExtras(b);
                if(ButtonChooserAktivity.clickedFragmentID!=0)//because of default page
                  lastAddedPosition++;
                startActivity(intent1);//TODO for result
            }
        });

        iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentProductImage = iconSpinner.getSelectedItemPosition();

                String separatedString = icons.get(currentProductImage).substring(0, icons.get(currentProductImage).lastIndexOf('.'));
                int resId = getResources().getIdentifier(separatedString, "drawable", getPackageName());
                iconView.setImageResource(resId);
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
            deviceID = extras.getString("deviceId");
            actionNames = extras.getStringArrayList("actionNames");
            deviceString = extras.getStringArrayList("DeviceString");
            actionArray = extras.getStringArrayList("ActionStrings");
            deviceNumber = extras.getInt("deviceNumber");
            actionNumber = extras.getInt("actionNumber");
            icons = extras.getStringArrayList("IconDrawable");
            proposalId = extras.getString("proposalId");
            userId=extras.getString("UserId");
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
