package de.ramelsberger.lmu.smartremoteapp;

import android.graphics.drawable.Drawable;
import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Fabian on 13.11.2015.
 */
public class ButtonObject implements Serializable {

    private static final String JSON_USER_ID = "userID";
    private static final String JSON_DEVICE_ID = "device";
    private static final String JSON_DEVICE_SUB_ID = "subID";
    private static final String JSON_NAME = "name";
    private static final String JSON_ACTION = "action";
    private static final String JSON_ICON = "icon";
    private static final String JSON_PROPOSAL_ID = "proposal";
    private static final int MAX_LENGTH = 8;
    private final String proposalId;
    private final String deviceName;

    private String userID;
    private String deviceID;
    private int deviceSubID;
    private String actionName;
    private String actionDescription;
    private String iconDescription;
    private String actionObject="id";

    private int buttonPosition;//Nur intern
    //private String actionObject="{color:'255,0,0'}";

    //complete Constructor


    //Constructor withoud device and action id
    public ButtonObject(String userId, String deviceID, String deviceName, String actionName, String actionDescription, String iconDescription, int buttonPosition, String proposalId) {
        this.userID=userId;
        this.deviceID=deviceID;
        this.deviceName=deviceName;
        this.actionDescription=actionDescription;
        this.actionName = actionName;
        this.iconDescription = iconDescription;
        this.buttonPosition=buttonPosition;
        this.proposalId=proposalId;

    }

    public JSONObject toJSON(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("_id",randomString());
            jsonObject.put(JSON_USER_ID,userID);
            jsonObject.put(JSON_DEVICE_ID, deviceID);
            jsonObject.put(JSON_NAME, actionName);
            jsonObject.put(JSON_DEVICE_SUB_ID, deviceSubID);
            jsonObject.put(JSON_ICON, iconDescription);
            jsonObject.put(JSON_ACTION, actionDescription);
            jsonObject.put(JSON_PROPOSAL_ID, proposalId);
            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return jsonObject;
        }
    }

    public static String randomString() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    public int getButtonPosition() {
        return buttonPosition;
    }

    public void setButtonPosition(int buttonPosition) {
        this.buttonPosition = buttonPosition;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getIconDescription() {
        return iconDescription;
    }

    public void setIconDescription(String iconDescription) {
        this.iconDescription = iconDescription;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public int getDeviceSubID() {
        return deviceSubID;
    }

    public void setDeviceSubID(int deviceSubID) {
        this.deviceSubID = deviceSubID;
    }

    public String getActionObject() {
        return actionObject;
    }

    public void setActionObject(String actionObject) {
        this.actionObject = actionObject;
    }

}
