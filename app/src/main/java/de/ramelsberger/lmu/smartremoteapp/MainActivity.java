package de.ramelsberger.lmu.smartremoteapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {

    public static final String BLUE_COLOR = "#334D5C";
    public static final String ORANGE_COLOR = "#E27A3F";
    public static final String RED_COLOR = "#DF4949";
    private static String LIGHT_COLOR = RED_COLOR;


    private static ArrayList<ButtonObject> buttonObjects;

    private MainActivity thisActivity;
    private ButtonObject draggedButtonObject;
    ImageButton[] imageButtons;

    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    private Socket socket;

    private String hostIP = "";
    private int port = 0;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //--------------------- setup costum font

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        thisActivity=this;
        //---------------------


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(thisActivity, ButtonChooser.class);
                startActivity(intent1);
            }
        });


        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        initializeDiscoveryListener();
        mNsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
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

    private class StartActionTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //TODO call action


            ImageButton button = (ImageButton) view;
            int resourceName = button.getId();
            switch (resourceName) {
                case R.id.imageButton:
                    LIGHT_COLOR = RED_COLOR;
                    break;

                case R.id.imageButton2:
                    LIGHT_COLOR = ORANGE_COLOR;
                    break;

                case R.id.imageButton3:
                    LIGHT_COLOR = "#B23D2A";
                    break;

                case R.id.imageButton4:
                    LIGHT_COLOR = BLUE_COLOR;
                    break;

                case R.id.imageButton5:
                    LIGHT_COLOR = "#EFC94C";
                    break;

                case R.id.imageButton6:
                    LIGHT_COLOR = "#42B264";
                    break;


                default:
                    LIGHT_COLOR = ORANGE_COLOR;
                    break;
            }


            socket.emit("setLight", LIGHT_COLOR);
            return false;
        }
    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.i("service", "discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.i("service", service.toString());
                if (service.getServiceName().equals("Smart-Remote-Server")) {
                    if (mNsdManager != null) {
                        mNsdManager.resolveService(service, new NsdManager.ResolveListener() {
                            @Override
                            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                                Log.i("service not resolved", serviceInfo.toString() + " " + errorCode);
                            }

                            @Override
                            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                                hostIP = serviceInfo.getHost().getHostAddress();
                                port = serviceInfo.getPort();
                                setupSocketIO();
                                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
                                Log.i("service resolved", "Resolve Succeeded. " + serviceInfo);


                            }
                        });
                    }
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.i("service", "service lost: " + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i("service", "discovery stopped");
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.i("service", "discovery start failed");
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.i("service", "discovery stop failed");
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    private void setupSocketIO() {
        try {
            Log.i("socket", "http://" + hostIP + ":" + port + "/");
            socket = IO.socket("http://" + hostIP + ":" + port + "/");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.i("INFO", "connected");
                    //StartListener
                    onInitializeButtonListeners();
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
            socket.on("devices", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONArray jArray = (JSONArray) args[0];
                }
            });

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
                        targetButton.setImageResource(draggedButtonObject.getImageId());
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "ONE");
        adapter.addFrag(new OneFragment(), "TWO");
        adapter.addFrag(new OneFragment(), "THREE");
        adapter.addFrag(new OneFragment(), "FOUR");
           viewPager.setAdapter(adapter);
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
}
