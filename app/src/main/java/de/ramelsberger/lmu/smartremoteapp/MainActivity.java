package de.ramelsberger.lmu.smartremoteapp;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String BLUE_COLOR = "#334D5C";
    public static final String ORANGE_COLOR = "#E27A3F";
    public static final String RED_COLOR = "#DF4949";
    private static String LIGHT_COLOR = RED_COLOR;


    public static ArrayList<ButtonObject> buttonObjects;
    public static ArrayList<DeviceObject> deviceObjects;
    public static ArrayList<ProposalObject> proposals;


    private MainActivity thisActivity;
    private ButtonObject draggedButtonObject;
    ImageButton[] imageButtons;

    private SocketIOService mService;
    private boolean mBound;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //--------------------- setup costum font
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "JosefinSans-Regular.ttf");

        //--------------------- toolbar

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        thisActivity=this;
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //---------------------
        Log.i("service", "create intent");
        Intent intent = new Intent(this, SocketIOService.class);
        Log.i("service", "start intent service");
        startService(intent);
        Log.i("service", "bind service");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //--------------------- test method without server

        testWithoutServer();

    }

    private void testWithoutServer() {
        buttonObjects = new ArrayList<>();

        try {
            String stringArray="[{\"_id\":\"irgubrkgb\",\"userID\":\"123abc\",\"device\":\"fsheufh\",\"subID\":0,\"name\":\"Red\",\"action\":\"turn bathroom-light red\",\"icon\":\"icon_lights_red.png\",\"proposal\":\"bsrgr8g\",\"__v\":0}]";
            JSONArray reader = new JSONArray(stringArray);
            Log.i("actionJsonArray %s", reader.toString());
            onReceiveActions(reader);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        deviceObjects = new ArrayList<>();

        try {
            String stringArray="[{\"_id\":\"fsheufh\",\"subID\":0,\"name\":\"bathroom-light\",\"type\":\"hue-light\"},{\"_id\":\"fsheufh\",\"subID\":1,\"name\":\"bedroom-light\",\"type\":\"hue-light\"}]\n";
            JSONArray reader = new JSONArray(stringArray);
            Log.i("actionJsonArray %s", reader.toString());
            storeReceivedDevices(reader);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        proposals = new ArrayList<>();

        try {
            String stringArray="[{\"_id\":\"bsrgr8g\",\"name\":\"Red\",\"type\":\"hue-light\",\"icon\":\"icon_lights_red.png\",\"action\":{\"rgb\":\"255,0,0\"},\"__v\":0},{\"_id\":\"kughsr4\",\"name\":\"Blue\",\"type\":\"hue-light\",\"icon\":\"icon_lights_blue.png\",\"action\":{\"rgb\":\"0,0,255\"},\"__v\":0},{\"_id\":\"izt498\",\"name\":\"Green\",\"type\":\"hue-light\",\"icon\":\"icon_lights_green.png\",\"action\":{\"rgb\":\"0,255,0\"},\"__v\":0}]";
            JSONArray reader = new JSONArray(stringArray);
            Log.i("actionJsonArray %s", reader.toString());
            onStoreReceivedProposals(reader);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private final class MyLongTouchListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {
            for (int i = 0; i < buttonObjects.size(); i++) {
                if (view.getTag() == buttonObjects.get(i).getButtonPosition())
                    draggedButtonObject = buttonObjects.get(i);
            }
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);

            return false;
        }
    }

    private class StartActionTouchListener implements View.OnTouchListener {//TODO give rigth information to the server
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            ButtonObject actionObject =null; //TODO getTouchedButton
            //mService.send("storeAction", actionObject.toJSON());
            return false;
        }
    }

    public void storeAction(JSONObject jsonObject){//Send currently created button to server
        if(mService!=null)
        mService.send("storeAction", jsonObject);
    }

    //------------------------------------------------ listeners

    private void onInitializeButtonListeners() {
        for (final ImageButton imageButton : imageButtons) {
            imageButton.setOnTouchListener(new StartActionTouchListener());
            imageButton.setOnLongClickListener(new MyLongTouchListener());
            imageButton.setOnDragListener(new MyDragListener());
        }
    }


    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;

                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    ImageButton oldButton = (ImageButton) event.getLocalState();
                    oldButton.setVisibility(View.VISIBLE);
                    ImageButton targetButton = (ImageButton) v;

                    if (oldButton != targetButton && targetButton != null && draggedButtonObject != null) {
                        oldButton.setImageResource(android.R.color.transparent);

                        Resources res = getResources();
                        String mDrawableName = draggedButtonObject.getIconDescription();
                        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
                        Drawable drawable = res.getDrawable(resID );
                        targetButton.setImageDrawable(drawable);

                        Integer newPos = (Integer) targetButton.getTag();
                        draggedButtonObject.setButtonPosition(newPos);
                    }

                case DragEvent.ACTION_DRAG_ENDED:
                    ImageButton button = (ImageButton) event.getLocalState();
                    button.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
            return true;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        int amountOfPages=DetailsView.lastAddedPosition/6+1;//because of the default view

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        PageFragment defaultFragment = new PageFragment();
        adapter.addFrag(defaultFragment, "Default Page");
        defaultFragment.setFragmentId(0);

        for (int i =0;i<amountOfPages;i++) {
            if(DetailsView.maxPos%6==0) {//do not create a new page when the default page if full
                PageFragment oneFragment = new PageFragment();
                oneFragment.setFragmentId((i+1));//i +1 because of the default page
                adapter.addFrag(oneFragment, "Page " + (i+1));
            }
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SocketIOService.SocketIOBinder binder = (SocketIOService.SocketIOBinder) service;
            mService = binder.getService();
            binder.setListener(new SocketIOService.SocketIOListener() {
                @Override
                public void onConnected() {
                    onInitializeButtonListeners();
                }

                @Override
                public void onDevicesReceived(JSONArray jsonArray) {
                    storeReceivedDevices(jsonArray);

                }

                @Override
                public void onActionsReceived(JSONArray jsonArray) {
                    //TODO rausparsen
                    onReceiveActions(jsonArray);
                }


                @Override
                public void onActionStored(JSONObject jsonObject) {//stored actions
                    //TODO action with id
                    try {
                        String userId = jsonObject.getString("userID");
                        String deviceId = jsonObject.getString("deviceID");
                        String deviceName = jsonObject.getString("name");
                        String actionName = jsonObject.getString("beschreibung");
                        String iconBeschreibung = jsonObject.getString("icon");
                        ButtonObject buttonObject = new ButtonObject(deviceId, deviceName, actionName, "Desc", iconBeschreibung, buttonObjects.size() + 1);
                        buttonObjects.add(buttonObject);

                        //upadte Fragments
                        //...TODO
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProposals(JSONArray jArray) {
                    onStoreReceivedProposals(jArray);
                }
            });
            mBound = true;
        }
    }
            ;

    private void onStoreReceivedProposals(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jsonobject = null;
            if(proposals==null)
                proposals= new ArrayList();
            try {
                jsonobject = jArray.getJSONObject(i);
                String porposalId = jsonobject.getString("_id");
                String name = jsonobject.getString("name");
                String type = jsonobject.getString("type");
                String icon = jsonobject.getString("icon");
                String Receivedaction =jsonobject.getString("action");
                ProposalObject proposalObject = new ProposalObject(porposalId, name, type, icon,Receivedaction);
                proposals.add(proposalObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void storeReceivedDevices(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                String id = (String) jsonobject.get("_id");
                String name = (String) jsonobject.get("name");
                String type = (String) jsonobject.get("type");
                DeviceObject deviceObject = new DeviceObject(id, name, type);
                if (jsonobject.has("subID")) {
                    deviceObject.setSubID(jsonobject.getString("subID"));
                }
                deviceObjects.add(deviceObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void onReceiveActions(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                String userId = jsonobject.getString("userID");//TestUser="1234abc"
                String deviceId = jsonobject.getString("device");
                String deviceName = jsonobject.getString("name");
                String actionName = jsonobject.getString("action");
                String iconBeschreibung = jsonobject.getString("icon");
                int position = i;
                ButtonObject buttonObject = new ButtonObject(userId, deviceId, deviceName, actionName, iconBeschreibung, position);
                buttonObjects.add(buttonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
