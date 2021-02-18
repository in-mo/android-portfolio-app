package com.cookandroid.mini_notice;

public class AttachInfo {
    int num;
    String filename;
    String uploadpath;
    int reg_gnum;

    public AttachInfo(int num, String filename, String uploadpath, int reg_gnum) {
        this.num = num;
        this.filename = filename;
        this.uploadpath = uploadpath;
        this.reg_gnum = reg_gnum;
    }
}
