package de.ramelsberger.lmu.smartremoteapp;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import io.socket.client.Socket;

//import info.androidhive.materialtabs.R;


public class OneFragment extends Fragment {

    public static final String BLUE_COLOR = "#334D5C";
    public static final String ORANGE_COLOR = "#E27A3F";
    public static final String RED_COLOR = "#DF4949";
    private static String LIGHT_COLOR = RED_COLOR;


    private static ArrayList<ButtonObject> buttonObjects;

    private MainActivity thisActivity;
    private ButtonObject draggedButtonObject;
    TextView[] imageButtons;

    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    private int fragmentId;



    public OneFragment() {
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


        //TODO
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        View thisFragment = inflater.inflate(R.layout.fragment_one, container,false);
        thisActivity= (MainActivity) getActivity();
        imageButtons =
                new TextView[]{
                        (TextView) thisFragment.findViewById(R.id.imageButton),
                        (TextView) thisFragment.findViewById(R.id.imageButton2),
                        (TextView) thisFragment.findViewById(R.id.imageButton3),
                        (TextView) thisFragment.findViewById(R.id.imageButton4),
                        (TextView) thisFragment.findViewById(R.id.imageButton5),
                        (TextView) thisFragment.findViewById(R.id.imageButton6)};

        for (int i = 0; i < imageButtons.length; i++) {
            imageButtons[i].setTag(i+6*fragmentId);
        }

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

        Typeface fontAwesomeFont = Typeface.createFromAsset(thisActivity.getAssets(), "fontawesome-webfont.ttf");
        int minus=0;
        if(fragmentId>0)
            minus=1;
        for (int i = fragmentId*6+minus; i < (fragmentId+1)*6; i++) {
           if(i%6<buttonObjects.size())
            {
                if(buttonObjects.size()>i){
                int index=(buttonObjects.get(i).getButtonPosition()) % 6;
                if(imageButtons[index]!=null) {
                    imageButtons[index].setTypeface(fontAwesomeFont);
                    imageButtons[index].setText(buttonObjects.get(i).getButtonText());
                    imageButtons[index].setTag(buttonObjects.get(i).getButtonPosition());
                }
                }
            }
        }

        return thisFragment;
    }

    public void setFragmentId(int id){
        fragmentId=id;
    }
}

