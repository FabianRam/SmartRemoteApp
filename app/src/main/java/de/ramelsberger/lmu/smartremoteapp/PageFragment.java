package de.ramelsberger.lmu.smartremoteapp;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static de.ramelsberger.lmu.smartremoteapp.MainActivity.buttonObjects;

//import info.androidhive.materialtabs.R;


public class PageFragment extends Fragment {

    private static final int DEFAULT_PAGE = 0;

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
        if (bundle != null) if (bundle.containsKey("newButtonJSON")) {
            if (buttonObjects == null)
                buttonObjects = new ArrayList<>();

            JSONObject newButtonObject;

            try {
                newButtonObject = new JSONObject(bundle.getString("newButtonJSON"));
                ((MainActivity) getActivity()).storeAction(newButtonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Intent mainAktivityIntent =
            //buttonObjects.add(newButtonObject);
            //buttonObjects.add(newButtonObject);

        }

        for (int i = 0; i < remoteButtonsId.length; i++) {

            remoteButtons[i] = (ImageButton) thisFragment.findViewById(remoteButtonsId[i]);

            if(buttonObjects!=null)
            for(int j=0;j<buttonObjects.size();j++) {
                if (buttonObjects.get(j).getButtonPosition() == i+6*fragmentId)
                {

                    Context context = remoteButtons[i].getContext();
                    Resources res = context.getResources();

                    String mDrawableName = buttonObjects.get(j).getIconDescription();
                    String separatedString = mDrawableName.substring (0, mDrawableName.lastIndexOf('.'));
                    int resID = res.getIdentifier(separatedString, "drawable", context.getPackageName());
                    remoteButtons[i].setImageResource(resID);
                }
            }
            final int buttonPosition=i;
            remoteButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(thisActivity, ButtonChooserAktivity.class);
                    intent1.putExtra("buttonPosition",buttonPosition);
                    intent1.putExtra("FragmentId",fragmentId);
                    startActivityForResult(intent1,1);
                }
            });
        }
        return thisFragment;
    }

    public void setFragmentId(int id) {
        fragmentId = id;
    }
}

