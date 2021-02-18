package com.cookandroid.mini_notice;

public class ContentInfo {
    int num, readCount, likeCount, hateCount;
    String title, contents, reg_id, reg_date;

    public ContentInfo(String title, String reg_id, String reg_date){
        this.title = title;
        this.reg_id = reg_id;
        this.reg_date = reg_date;
    }

    public ContentInfo(int num, String title, String contents, int readCount, int likeCount,
                       int hateCount, String reg_id, String reg_date) {
        this.num = num;
        this.readCount = readCount;
        this.likeCount = likeCount;
        this.hateCount = hateCount;
        this.title = title;
        this.contents = contents;
        this.reg_id = reg_id;
        this.reg_date = reg_date;
    }
}
