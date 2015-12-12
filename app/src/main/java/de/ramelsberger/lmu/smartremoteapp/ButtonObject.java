package de.ramelsberger.lmu.smartremoteapp;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Fabian on 13.11.2015.
 */
public class ButtonObject implements Serializable {
    private int imageId;
    private String buttonText;
    private String actionText;

    private int buttonPosition;


    public ButtonObject(int imageId, int buttonPosition, String buttonText, String actionText) {
        this.imageId = imageId;
        this.buttonPosition = buttonPosition;
        this.buttonText = buttonText;
        this.actionText = actionText;
    }


    //-------------------------------- getter and setter


    protected ButtonObject(Parcel in) {
        imageId = in.readInt();
        buttonText = in.readString();
        actionText = in.readString();
        buttonPosition = in.readInt();
    }

    public void setButtonPosition(int buttonPosition) {
        this.buttonPosition = buttonPosition;
    }

    public int getButtonPosition() {
        return buttonPosition;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getActionText() {
        return actionText;
    }

}
