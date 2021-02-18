package com.cookandroid.mini_notice;

public class FavorInfo {
    String board_num, reply_num, isReply, isLike, reg_id;

    public FavorInfo(String board_num, String reply_num,
                     String isReply, String isLike, String reg_id) {
        this.board_num = board_num;
        this.reply_num = reply_num;
        this.isReply = isReply;
        this.isLike = isLike;
        this.reg_id = reg_id;
    }
}
