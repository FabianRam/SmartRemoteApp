package de.ramelsberger.lmu.smartremoteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class ButtonChooser extends AppCompatActivity {

    private LinearLayout linearLayoutDrawer;
    private FrameLayout bigButtonPannel;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout horizontalButtonLayout;
    private int amountOfBigPannels;
    private int amountOfSingleButtonPannels;
    private ButtonChooser thisAktivity;

    private String[] testArray = {"1aöjflkf", "2kjlödasföld", "3kölölkfasd", "4döljsa", "5dsölafd", "6daöskl"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttton_chooser);
        //Typeface font = Typeface.createFromAsset( getAssets(), "../fontawesome-webfont.ttf");

        thisAktivity = this;
        // Button button = (Button)findViewById( R.id.panelAwsomeIcon );
        //  button.setTypeface(font);
        linearLayoutDrawer = (LinearLayout) findViewById(R.id.drawer_linear_parent);


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        amountOfBigPannels = 4;
        for (int j = 0; j < amountOfBigPannels; j++) {
            LinearLayout customBigButtonPanel = (LinearLayout) inflater.inflate(R.layout.big_buton_pannel, null);
            linearLayoutDrawer.addView(customBigButtonPanel);
            horizontalScrollView = (HorizontalScrollView) customBigButtonPanel.getChildAt(1);
            horizontalButtonLayout = (LinearLayout) horizontalScrollView.getChildAt(0);
            final int deviceNumber = j;

            amountOfSingleButtonPannels = 5;
            for (int i = 0; i < amountOfSingleButtonPannels; i++) {
                FrameLayout custom = (FrameLayout) inflater.inflate(R.layout.single_button_pannel, null);
                horizontalButtonLayout.addView(custom);
                final int actionNumber = i;

                custom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO Give data
                        Intent intent1 = new Intent(thisAktivity, DetailsView.class);
                        final int dNumber = deviceNumber;
                        final int aNumber = actionNumber;
                        intent1.putExtra("deviceNumber", dNumber);
                        intent1.putExtra("actionNumber", aNumber);
                        intent1.putExtra("TestString", testArray);


                        startActivity(intent1);
                    }
                });
            }
        }
    }
}
