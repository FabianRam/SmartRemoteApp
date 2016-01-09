package de.ramelsberger.lmu.smartremoteapp;

import android.graphics.drawable.Drawable;
import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Fabian on 13.11.2015.
 */
public class ButtonObject implements Serializable {

    private int userID;
    private int deviceID;
    private String deviceName;
    private String actionName;
    private String iconDescription;

    private int buttonPosition;

    //complete Constructor


    //Constructor withoud device and action id
    public ButtonObject(String deviceName, String actionName, String iconDescription,int buttonPosition) {
        this.deviceName = deviceName;
        this.actionName = actionName;
        this.iconDescription = iconDescription;
        this.buttonPosition=buttonPosition;

    }

    public ButtonObject(int userId, int deviceId, String deviceName, String actionName, String iconBeschreibung, int position) {
        this.userID=userId;
        this.deviceID=deviceId;
        this.deviceName=deviceName;
        this.actionName=actionName;
        this.iconDescription=iconBeschreibung;
        this.buttonPosition=position;
    }

    public int getButtonPosition() {
        return buttonPosition;
    }

    public void setButtonPosition(int buttonPosition) {
        this.buttonPosition = buttonPosition;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
}
