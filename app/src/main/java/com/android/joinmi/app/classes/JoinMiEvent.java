package com.android.joinmi.app.classes;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ccheung on 2016-10-23.
 */
public class JoinMiEvent {

    private LatLng m_location;

    public LatLng getlocation() {
        return m_location;
    }

    public void setlocation(LatLng location) {
        this.m_location = location;
    }
}
