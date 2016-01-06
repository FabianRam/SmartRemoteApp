package de.ramelsberger.lmu.smartremoteapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
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
        Intent intent = new Intent(this, SocketIOService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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
            ImageButton button = (ImageButton) view;
            int resourceName = button.getId();
            switch (resourceName) {
                case R.id.remote_button1:
                    LIGHT_COLOR = RED_COLOR;
                    break;

                case R.id.remote_button2:
                    LIGHT_COLOR = ORANGE_COLOR;
                    break;

                case R.id.remote_button3:
                    LIGHT_COLOR = "#B23D2A";
                    break;

                case R.id.remote_button4:
                    LIGHT_COLOR = BLUE_COLOR;
                    break;

                case R.id.remote_button5:
                    LIGHT_COLOR = "#EFC94C";
                    break;

                case R.id.remote_button6:
                    LIGHT_COLOR = "#42B264";
                    break;


                default:
                    LIGHT_COLOR = ORANGE_COLOR;
                    break;
            }


            mService.send("setLight", LIGHT_COLOR);
            return false;
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
        int amountOfPages=DetailsView.lastAddedPosition/6+1;

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i =0;i<amountOfPages;i++) {
            if(DetailsView.maxPos%6==0) {
                OneFragment oneFragment = new OneFragment();
                oneFragment.setFragmentId(i);
                adapter.addFrag(oneFragment, "Page " + (i+1));
            }
        }
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
                }

                @Override
                public void onActionsReceived(JSONArray jsonArray) {

                }
            });
            mBound = true;
        }
    };
}
