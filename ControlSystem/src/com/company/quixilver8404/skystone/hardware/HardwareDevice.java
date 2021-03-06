package com.company.quixilver8404.skystone.hardware;

/**
 * Represents a generic hardware device that is mapped via a name
 */
public abstract class HardwareDevice {
    private final String deviceName;

    public HardwareDevice(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
