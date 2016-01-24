package de.ramelsberger.lmu.smartremoteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static de.ramelsberger.lmu.smartremoteapp.MainActivity.deviceObjects;
import static de.ramelsberger.lmu.smartremoteapp.MainActivity.proposals;

public class ButtonChooserAktivity extends AppCompatActivity {

    private LinearLayout linearLayoutDrawer;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout horizontalButtonLayout;
    private ButtonChooserAktivity thisAktivity;

    public static int clickedPosition;
    public static int clickedFragmentID;
    private ArrayList<String> headingStrings;
    private ArrayList<String> deviceId;

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

        ArrayList differentTypes= new ArrayList();
        boolean typeIsNew;
        int currentType=0;

        if(proposals!=null) {
            for (int i = 0; i < proposals.size(); i++) {
                typeIsNew = true;
                for (int j = 0; j < differentTypes.size(); j++) {
                    if (proposals.get(i).getType() == differentTypes.get(j)) {
                        typeIsNew = false;
                    }
                }
                if (typeIsNew)
                    currentType++;
                differentTypes.add(proposals.get(i));
            }
        }

        //----------------------------------------------headings //initialize variables
        headingStrings=new ArrayList<>();
        deviceId= new ArrayList<>();
        ArrayList<ArrayList> listOfAllDevices=new ArrayList();
        if(deviceObjects.size()>0)
        for(int i=0;i<deviceObjects.size();i++) {
            headingStrings.add(deviceObjects.get(i).getDeviceName());
            //if (listOfAllDevices.get(i) == null) {
                ArrayList<ProposalObject> deviceActions = new ArrayList();
                listOfAllDevices.add(deviceActions);
            //}
            for (int j = 0; j < proposals.size(); j++) {

                //if (headingStrings.get(i) == proposals.get(j).getType()) {//TODO
                    listOfAllDevices.get(i).add(proposals.get(j));
                //}

            }
        }

        //final int[][] icons={lightIcons,steroIcons};
        final String[][] strings ={lightIconString,steroStrings};

        if(getIntent().getExtras()!=null) {
            clickedFragmentID = (int) getIntent().getExtras().get("FragmentId");
            clickedPosition = (int) getIntent().getExtras().get("buttonPosition");
        }

        for (int j = 0; j < headingStrings.size(); j++) {
            LinearLayout customBigButtonPanel = (LinearLayout) inflater.inflate(R.layout.pannel_big_buton, null);
            linearLayoutDrawer.addView(customBigButtonPanel);

            TextView deviceHeading =(TextView) customBigButtonPanel.getChildAt(0);
            deviceHeading.setText(headingStrings.get(j));

            horizontalScrollView = (HorizontalScrollView) customBigButtonPanel.getChildAt(1);
            horizontalButtonLayout = (LinearLayout) horizontalScrollView.getChildAt(0);
            final int deviceNumber = j;

            for (int i = 0; i < listOfAllDevices.get(j).size(); i++) {
                FrameLayout custom = (FrameLayout) inflater.inflate(R.layout.pannel_single_button, null);
                horizontalButtonLayout.addView(custom);
                LinearLayout v=(LinearLayout) custom.getChildAt(0);
                ImageView buttonImage =(ImageView) v.getChildAt(0);

                String fileName= ((ProposalObject)listOfAllDevices.get(j).get(i)).getIcon();
                String separatedString = fileName.substring(0, fileName.lastIndexOf('.'));
                int resId = getResources().getIdentifier(separatedString, "drawable", getPackageName());
                buttonImage.setImageResource(resId);

                TextView headingView =(TextView) v.getChildAt(1);

                ProposalObject proposalObject=((ProposalObject) listOfAllDevices.get(j).get(i));


                headingView.setText(proposalObject.getName());

                TextView detailView =(TextView) v.getChildAt(2);

                detailView.setText(proposalObject.getAction());

                final int actionNumber = i;
                //SetText
                final ArrayList<String> proposalActions=new ArrayList<String>();
                for (int p=0;p<proposals.size();p++) {
                    proposalActions.add(proposals.get(p).getName() + " " + proposals.get(p).getAction());
                }

                final ArrayList<String> icons=new ArrayList<String>();
                for (int p=0;p<proposals.size();p++) {
                    icons.add(proposals.get(p).getIcon());
                }

                custom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(thisAktivity, DetailsView.class);
                        final int dNumber = deviceNumber;
                        final int aNumber = actionNumber;
                        intent1.putExtra("deviceId",deviceId);
                        intent1.putExtra("deviceNumber", dNumber);
                        intent1.putExtra("actionNumber", aNumber);
                        intent1.putExtra("DeviceString", headingStrings);
                        intent1.putExtra("ActionStrings",proposalActions);
                        intent1.putExtra("IconDrawable", icons);
                        startActivityForResult(intent1, 2);//TODO
                    }
                });
            }
        }
    }
}
