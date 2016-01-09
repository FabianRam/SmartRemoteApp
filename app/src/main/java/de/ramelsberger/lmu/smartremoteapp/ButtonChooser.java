package de.ramelsberger.lmu.smartremoteapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ButtonChooser extends AppCompatActivity {

    private LinearLayout linearLayoutDrawer;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout horizontalButtonLayout;
    private ButtonChooser thisAktivity;

    public static int clickedPosition;
    public static int clickedFragmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttton_chooser);
        thisAktivity = this;
        linearLayoutDrawer = (LinearLayout) findViewById(R.id.drawer_linear_parent);
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final int[] lightIcons ={R.drawable.icon_lights_blue,R.drawable.icon_lights_green,//TODO get this informations from the server
                R.drawable.icon_lights_red,R.drawable.icon_lights_yellow,
                R.drawable.icon_lights_brighter,R.drawable.icon_lights_fewer,
                R.drawable.icon_lights_off,R.drawable.icon_lights_on
                };

        final  String[] lightIconString ={"Blue","Green","Red","Yellow",
                "Brighter Light", "Darker Light","Lights Off","Lights On"};
        final int[] steroIcons ={
                R.drawable.icon_stereo_fastforward,R.drawable.icon_stereo_lastsong,
                R.drawable.icon_stereo_play, R.drawable.icon_stereo_nextsong,R.drawable.icon_stereo_pause
        };

        final String[] steroStrings ={"Fastforward","Last song","Play","Next song", "Pause"};

        final String[] headingStrings={"HUE Lights","Stereo images"};

        final int[][] icons={lightIcons,steroIcons};
        final String[][] strings ={lightIconString,steroStrings};

        if(getIntent().getExtras()!=null) {
            clickedFragmentID = (int) getIntent().getExtras().get("FragmentId");
            clickedPosition = (int) getIntent().getExtras().get("buttonPosition");
        }

        for (int j = 0; j < icons.length; j++) {
            LinearLayout customBigButtonPanel = (LinearLayout) inflater.inflate(R.layout.pannel_big_buton, null);
            linearLayoutDrawer.addView(customBigButtonPanel);

            TextView deviceHeading =(TextView) customBigButtonPanel.getChildAt(0);
            deviceHeading.setText(headingStrings[j]);

            horizontalScrollView = (HorizontalScrollView) customBigButtonPanel.getChildAt(1);
            horizontalButtonLayout = (LinearLayout) horizontalScrollView.getChildAt(0);
            final int deviceNumber = j;

            for (int i = 0; i < icons[j].length; i++) {
                FrameLayout custom = (FrameLayout) inflater.inflate(R.layout.pannel_single_button, null);
                horizontalButtonLayout.addView(custom);
                LinearLayout v=(LinearLayout) custom.getChildAt(0);
                ImageView buttonImage =(ImageView) v.getChildAt(0);
                buttonImage.setImageDrawable(getDrawable(icons[j][i]));

                TextView headingView =(TextView) v.getChildAt(1);
                headingView.setText(strings[j][i]);

                final int actionNumber = i;
                //SetText

                custom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO Give data
                        Intent intent1 = new Intent(thisAktivity, DetailsView.class);
                        final int dNumber = deviceNumber;
                        final int aNumber = actionNumber;
                        intent1.putExtra("deviceNumber", dNumber);
                        intent1.putExtra("actionNumber", aNumber);
                        intent1.putExtra("DeviceString", headingStrings);
                        intent1.putExtra("ActionStrings",strings[dNumber]);
                        intent1.putExtra("IconDrawable", icons[dNumber]);

                        startActivityForResult(intent1, 2);//TODO
                    }
                });
            }
        }
    }
}
