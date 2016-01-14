
package de.ramelsberger.lmu.smartremoteapp;

/**
 * Created by Fabian on 13.01.2016.
 */
public class DeviceObject {

    private String deviceId;
    private String deviceName;
    private String deviceType;
    private String subID;

    public DeviceObject(String deviceId, String deviceName, String deviceType) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }

    public String getSubID() {
        return subID;
    }
}
