package de.ramelsberger.lmu.smartremoteapp;

import android.graphics.drawable.Drawable;
import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Fabian on 13.11.2015.
 */
public class ButtonObject implements Serializable {
    private int imageId;
    private int buttonDrawable;
    private String actionText;

    private int buttonPosition;

    //id
    //eingelogter User
    //deivce
    //name
    //action
    //icon

    public ButtonObject(int imageId, int buttonPosition, int buttonDrawable, String actionText) {
        this.imageId = imageId;
        this.buttonPosition = buttonPosition;
        this.buttonDrawable = buttonDrawable;
        this.actionText = actionText;
    }


    //-------------------------------- getter and setter



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

    public void setButtonText(int buttonDrawable) {
        this.buttonDrawable = buttonDrawable;
    }

    public int getButtonDrawable() {
        return buttonDrawable;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getActionText() {
        return actionText;
    }

}
