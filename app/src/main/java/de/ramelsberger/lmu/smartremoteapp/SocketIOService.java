package de.ramelsberger.lmu.smartremoteapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIOService extends Service {

    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    private Socket socket;
    private SocketIOListener mListener;

    private String hostIP = "";
    private int port = 0;

    private SocketIOBinder mBinder = new SocketIOBinder();

    public SocketIOService() {
        Log.i("service", "created service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        initializeDiscoveryListener();
        mNsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("service", "started service");
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service", "bound service");
        return mBinder;
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
                    mListener.onConnected();
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
                    if (mListener != null) {
                        mListener.onDevicesReceived(jArray);
                    }
                }
            });
            socket.on("actions", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONArray jArray = (JSONArray) args[0];
                    if(mListener != null){
                        mListener.onActionsReceived(jArray);
                    }
                }
            });

            socket.on("storedAction", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject jArray = (JSONObject) args[0];
                    if(mListener != null){
                        mListener.onActionStored(jArray);
                    }
                }
            });

            socket.on("proposals", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONArray jArray = (JSONArray) args[0];
                    if(mListener != null){
                        mListener.onProposals(jArray);
                    }
                }
            });

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void send(String event, String message){
        socket.emit(event,message);
    }

    public void send(String event, JSONArray message){
        socket.emit(event,message);
    }

    public void send(String event, JSONObject message){
        socket.emit(event,message);
    }

    public class SocketIOBinder extends Binder {

        public SocketIOService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SocketIOService.this;
        }

        public void setListener(SocketIOListener listener) {
            mListener = listener;
        }
    }

    public interface SocketIOListener{
        public void onConnected();
        public void onDevicesReceived(JSONArray jsonArray);
        public void onActionsReceived(JSONArray jsonArray);
        public void onActionStored(JSONObject jsonObject);
        public void onProposals(JSONArray jArray);
    }
}
