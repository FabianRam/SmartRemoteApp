package de.ramelsberger.lmu.smartremoteapp;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

//import info.androidhive.materialtabs.R;


public class PageFragment extends Fragment {

    private static final int DEFAULT_PAGE = 0;
    private static ArrayList<ButtonObject> buttonObjects;

    private MainActivity thisActivity;
    private ButtonObject draggedButtonObject;
    ImageButton[] imageButtons;

    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    private int fragmentId;


    public PageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(fragmentId==DEFAULT_PAGE)//
            setDefaultButtons(); //TODO get information from the server


        //TODO
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        View thisFragment = inflater.inflate(R.layout.fragment_button_page, container, false);

        int[] remoteButtonsId = {R.id.remote_button1, R.id.remote_button2, R.id.remote_button3,
                R.id.remote_button4, R.id.remote_button5, R.id.remote_button6};
        ImageButton[] remoteButtons = new ImageButton[6];

        thisActivity = (MainActivity) getActivity();
        Bundle bundle = thisActivity.getIntent().getExtras();

        //Get saved buttonObject from DetailsView
        if (buttonObjects == null)
            buttonObjects = new ArrayList<>();

        //TODO Add server mehtod
        if (bundle != null) {
            if (bundle.containsKey("newButtonObject")) {
                if (buttonObjects == null)
                    buttonObjects = new ArrayList<>();

                ButtonObject newButtonObject = (ButtonObject) bundle.getSerializable("newButtonObject");
                buttonObjects.add(newButtonObject);

            }
        }

        for (int i = 0; i < remoteButtonsId.length; i++) {

            remoteButtons[i] = (ImageButton) thisFragment.findViewById(remoteButtonsId[i]);

            if(buttonObjects!=null)
            for(int j=0;j<buttonObjects.size();j++) {
                if (buttonObjects.get(j).getButtonPosition() == i+6*fragmentId)
                {
                    remoteButtons[i].setImageResource(buttonObjects.get(j).getButtonDrawable());
                }
            }
            final int buttonPosition=i;
            remoteButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(thisActivity, ButtonChooser.class);
                    intent1.putExtra("buttonPosition",buttonPosition);
                    intent1.putExtra("FragmentId",fragmentId);
                    startActivityForResult(intent1,1);

                }
            });
        }
        return thisFragment;
    }

    private void setDefaultButtons() {
        /*buttonObjects.add(new ButtonObject(5,0,R.drawable.icon_lights_fewer,"fewerLight"));
        buttonObjects.add(new ButtonObject(0,1,R.drawable.icon_lights_blue,"blue"));
        buttonObjects.add(new ButtonObject(6,2,R.drawable.icon_lights_off,"lightsOff"));
        buttonObjects.add(new ButtonObject(4,3,R.drawable.icon_stereo_pause,"pause"));
        buttonObjects.add(new ButtonObject(4,4,R.drawable.icon_stereo_pause,"pause"));
        buttonObjects.add(new ButtonObject(4,5,R.drawable.icon_stereo_pause,"pause"));
        */
    }

    public void setFragmentId(int id) {
        fragmentId = id;
    }
}

