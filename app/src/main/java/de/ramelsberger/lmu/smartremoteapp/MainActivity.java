package de.ramelsberger.lmu.smartremoteapp;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static de.ramelsberger.lmu.smartremoteapp.R.id.imageButton;
import static de.ramelsberger.lmu.smartremoteapp.R.id.imageButton4;
import static de.ramelsberger.lmu.smartremoteapp.R.id.imageButton5;

public class MainActivity extends AppCompatActivity  {

    private static ArrayList<ButtonObject> buttonObjects;

    private MainActivity thisActivity;
    private ButtonObject draggedButtonObject;
    ImageButton[] imageButtons;

    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    private Socket socket;

    private String hostIP = "";
    private int port = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        thisActivity =this;
        imageButtons =
                new ImageButton[]{
                        (ImageButton) this.thisActivity.findViewById(R.id.imageButton),
                        (ImageButton) this.thisActivity.findViewById(R.id.imageButton2),
                        (ImageButton) this.thisActivity.findViewById(R.id.imageButton3),
                        (ImageButton) this.thisActivity.findViewById(R.id.imageButton4),
                        (ImageButton) this.thisActivity.findViewById(R.id.imageButton5),
                        (ImageButton) this.thisActivity.findViewById(R.id.imageButton6)};


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(thisActivity,DetailsView.class);
                startActivity(intent1);
            }
        });

        // Creates a new ImageView
        for (final ImageButton imageButton : imageButtons) {
            imageButton.setOnTouchListener(new MyTouchListener());
            imageButton.setOnDragListener(new MyDragListener());
        }
        for(int i=0;i<imageButtons.length;i++){
            imageButtons[i].setTag(i);
        }

        Bundle bundle = getIntent().getExtras();

        //Get saved buttonObject from DetailsView
        if(buttonObjects==null)
        buttonObjects=new ArrayList<ButtonObject>();

        //TODO Add server mehtod
        if(bundle!=null) {
            if (bundle.containsKey("newButtonObject")) {
                if(buttonObjects==null)
                    buttonObjects=new ArrayList<ButtonObject>();

                ButtonObject newButtonObject = (ButtonObject) bundle.getSerializable("newButtonObject");
                buttonObjects.add(newButtonObject);

            }
        }else{
        }
        for(int i=0;i<buttonObjects.size();i++){
            imageButtons[buttonObjects.get(i).getButtonPosition()].setImageResource(buttonObjects.get(i).getImageId());
            imageButtons[buttonObjects.get(i).getButtonPosition()].setTag(buttonObjects.get(i).getButtonPosition());
        }

        mNsdManager = (NsdManager)getSystemService(Context.NSD_SERVICE);
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

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                for (int i=0;i<buttonObjects.size();i++) {
                    if (view.getTag() == buttonObjects.get(i).getButtonPosition())
                        draggedButtonObject = buttonObjects.get(i);
                }
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
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
                // A service was found!  Do something with it.
                Log.i("service", service.toString());
                if(service.getServiceName().equals("Smart-Remote-Server")){
                    if(mNsdManager!=null){
                        mNsdManager.resolveService(service, new NsdManager.ResolveListener() {
                            @Override
                            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                                Log.i("service not resolved", serviceInfo.toString()+" " + errorCode);
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
                Log.i("service", "service lost: "+service);
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

    private void setupSocketIO(){
        try {
            Log.i("socket", "http://"+hostIP+":"+port+"/");
            socket = IO.socket("http://" + hostIP + ":" + port + "/");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.i("INFO", "connected");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    class MyDragListener implements View.OnDragListener{

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

                    if(oldButton!=targetButton&&targetButton!=null&&draggedButtonObject!=null) {
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
}
