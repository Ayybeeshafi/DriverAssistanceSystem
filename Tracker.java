package com.example.abdullah.opencvframeget;

/**
 * Created by Abdullah on 8/17/2016.
 */
public class Tracker {
    static {
        System.loadLibrary("main");
    }
    public static native boolean hello(long addr_src,int frame_count);
    public static native String hello1();
    public static native int tracking_engine(long addr_src,int frame_count,int track_chk,int roi_act);
    public static native int calibrate(long addr_frame);
}
