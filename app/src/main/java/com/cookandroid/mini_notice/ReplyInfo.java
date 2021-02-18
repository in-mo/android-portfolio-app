package com.cookandroid.mini_notice;

public class ReplyInfo {
    String num, contents, likeCount, hateCount, reg_id, reg_gNum, reg_date;

    public ReplyInfo(String num, String contents, String likeCount,
                     String hateCount, String reg_id, String reg_gNum, String reg_date) {

        this.num = num;
        this.contents = contents;
        this.likeCount = likeCount;
        this.hateCount = hateCount;
        this.reg_id = reg_id;
        this.reg_gNum = reg_gNum;
        this.reg_date = reg_date;
    }
}
