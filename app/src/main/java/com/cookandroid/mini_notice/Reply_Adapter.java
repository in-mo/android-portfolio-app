package com.cookandroid.mini_notice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.mini_notice.NetWork.ContentParser;
import com.cookandroid.mini_notice.NetWork.FavorParser;
import com.cookandroid.mini_notice.NetWork.NetworkFavorSetInfo;
import com.cookandroid.mini_notice.NetWork.NetworkReplyDelete;
import com.cookandroid.mini_notice.NetWork.NetworkReplyFavorCountGet;
import com.cookandroid.mini_notice.NetWork.NetworkReplyFavorUpdate;
import com.cookandroid.mini_notice.NetWork.NetworkReplyGet;
import com.cookandroid.mini_notice.NetWork.NetworkReplyUpdate;
import com.cookandroid.mini_notice.NetWork.ReplyParser;

import java.util.ArrayList;

public class Reply_Adapter extends BaseAdapter {
    private ContentActivity mAct;
    LayoutInflater mInflater;
    ArrayList<ReplyInfo> mReplyInfoObjArr;
    int mListLayout;

    public Reply_Adapter(ContentActivity a, int listLayout, ArrayList<ReplyInfo> contentsInfoObjArrayListT) {
        mAct = a;
        mListLayout = listLayout;
        mReplyInfoObjArr = contentsInfoObjArrayListT;
        mInflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDatas(ArrayList<ReplyInfo> arrayList) {
        mReplyInfoObjArr = arrayList;
    }

    @Override
    public int getCount() {
        return mReplyInfoObjArr.size();
    }

    @Override
    public Object getItem(int i) {
        return mReplyInfoObjArr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = mInflater.inflate(mListLayout, viewGroup, false);

        final TextView rWriterView = (TextView) view.findViewById(R.id.rWriterView);
        rWriterView.setText("작성자 : " + String.valueOf(mReplyInfoObjArr.get(i).reg_id));

        final TextView rDateView = (TextView) view.findViewById(R.id.rDateView);
        String[] strDate = mReplyInfoObjArr.get(i).reg_date.split("\n");
        rDateView.setText(strDate[0] + " " + strDate[1]);

        final TextView rContents = (TextView) view.findViewById(R.id.rContents);
        rContents.setText(mReplyInfoObjArr.get(i).contents);

        final Button btn_delete = (Button) view.findViewById(R.id.btn_reDelete);
        final Button btn_btnUpdate = (Button) view.findViewById(R.id.btn_reUpdate);

        final ImageView replyLikeBtn = (ImageView) view.findViewById(R.id.replyLikeBtn);
        final ImageView replyHateBtn = (ImageView) view.findViewById(R.id.replyHateBtn);
        final TextView replyLikeCount = (TextView) view.findViewById(R.id.replyLikeCount);
        final TextView replyHateCount = (TextView) view.findViewById(R.id.replyHateCount);


        try {
            String board_num = String.valueOf(mReplyInfoObjArr.get(i).reg_gNum);
            String reply_num = String.valueOf(mReplyInfoObjArr.get(i).num);

            // Content 좋아요/싫어요 갯수 처리
            String resFavorCount = new NetworkReplyFavorCountGet().execute(board_num, reply_num).get();
            String[] replyInfo = ReplyParser.getReplysInfoJson(resFavorCount, new String[8]);

            replyLikeCount.setText((replyInfo[2] == null ? "0" : replyInfo[2]) + " 회");
            replyHateCount.setText((replyInfo[3] == null ? "0" : replyInfo[3]) + " 회");

            // 좋아요 or 싫어요 현황 처리
            if (!MainActivity.id.equals("")) {
                ArrayList<FavorInfo> favorInfo = new ArrayList<FavorInfo>();
                String resFavor = new NetworkFavorSetInfo().execute(board_num, reply_num, "0", "", MainActivity.id, "get").get();
                ArrayList<FavorInfo> arrFavor = FavorParser.getFavorsInfoJson(resFavor, favorInfo);
                if (arrFavor.size() > 0)
                    if (arrFavor.get(0).isReply.equals("0")) { // 댓글 일 때,
                        if (arrFavor.get(0).isLike.equals("1")) { // 좋아요 선택했었을 때
                            replyLikeBtn.setImageResource(R.drawable.like_select_btn);
                            replyHateBtn.setImageResource(R.drawable.hate_btn);
                        } else if (arrFavor.get(0).isLike.equals("0")) { // 아무것도 누르지 않은 상태
                            replyLikeBtn.setImageResource(R.drawable.like_btn);
                            replyHateBtn.setImageResource(R.drawable.hate_btn);
                        } else { // 싫어요 선택했었을 때
                            replyLikeBtn.setImageResource(R.drawable.like_btn);
                            replyHateBtn.setImageResource(R.drawable.hate_select_btn);
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int index = i;

        replyLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable likeBtnDraw = replyLikeBtn.getDrawable();
                Drawable likeBtnDrawRes = v.getResources().getDrawable(R.drawable.like_select_btn);
                Drawable hateBtnDraw = replyHateBtn.getDrawable();
                Drawable hateBtnRes = v.getResources().getDrawable(R.drawable.hate_select_btn);

                Bitmap likeBitmap = ((BitmapDrawable) likeBtnDraw).getBitmap();
                Bitmap likeBitmapRes = ((BitmapDrawable) likeBtnDrawRes).getBitmap();

                Bitmap hateBitmap = ((BitmapDrawable) hateBtnDraw).getBitmap();
                Bitmap hateBitmapRes = ((BitmapDrawable) hateBtnRes).getBitmap();

                String[] strLike = replyLikeCount.getText().toString().split(" ");
                String[] strHate = replyHateCount.getText().toString().split(" ");

                String reg_gnum = String.valueOf(mReplyInfoObjArr.get(index).reg_gNum);
                String reply_num = String.valueOf(mReplyInfoObjArr.get(index).num);
                String current_id = MainActivity.id;

                int countLike, countHate;
                // 댓글 좋아요 취소시
                if (likeBitmap.equals(likeBitmapRes)) {
                    replyLikeBtn.setImageResource(R.drawable.like_btn);
                    countLike = (Integer.parseInt(strLike[0]) - 1) == -1 ? 0 : Integer.parseInt(strLike[0]) - 1;
                    replyLikeCount.setText(countLike + " 회");

                    try {
                        String resLikeFavor = new NetworkReplyFavorUpdate().execute(reply_num, "-1", "like").get();
                        int countResLikeFavor = ContentParser.getResultJson(resLikeFavor);
                        if (countResLikeFavor == 1) {
                            try {
                                String resGet = new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "0", current_id, "is_exist").get();
                                int countGet = ReplyParser.getResultJson(resGet);
                                if (countGet == 0) {
                                    new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "0", current_id, "insert");
                                } else if (countGet == 1) {
                                    new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "0", current_id, "update");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 좋아요 누를시
                else if (!likeBitmap.equals(likeBitmapRes)) {
                    replyLikeBtn.setImageResource(R.drawable.like_select_btn);
                    countLike = Integer.parseInt(strLike[0]) + 1;
                    replyLikeCount.setText(countLike + " 회");
                    if (hateBitmap.equals(hateBitmapRes)) {
                        replyHateBtn.setImageResource(R.drawable.hate_btn);
                        countHate = (Integer.parseInt(strHate[0]) - 1) == -1 ? 0 : Integer.parseInt(strHate[0]) - 1;
                        replyHateCount.setText(countHate + " 회");

                        new NetworkReplyFavorUpdate().execute(reply_num, "-1", "hate");
                    }

                    try {
                        String resLikeFavor = new NetworkReplyFavorUpdate().execute(reply_num, "1", "like").get();
                        int countResLikeFavor = ReplyParser.getResultJson(resLikeFavor);
                        if (countResLikeFavor == 1) {
                            try {
                                String resGet = new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "1", current_id, "is_exist").get();
                                int countGet = ReplyParser.getResultJson(resGet);
                                if (countGet == 0) {
                                    new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "1", current_id, "insert");
                                } else if (countGet == 1) {
                                    new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "1", current_id, "update");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        replyHateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable hateBtnDraw = replyHateBtn.getDrawable();
                Drawable hateBtnRes = v.getResources().getDrawable(R.drawable.hate_select_btn);

                Drawable likeBtnDraw = replyLikeBtn.getDrawable();
                Drawable likeBtnDrawRes = v.getResources().getDrawable(R.drawable.like_select_btn);

                Bitmap hateBitmap = ((BitmapDrawable) hateBtnDraw).getBitmap();
                Bitmap hateBitmapRes = ((BitmapDrawable) hateBtnRes).getBitmap();

                Bitmap likeBitmap = ((BitmapDrawable) likeBtnDraw).getBitmap();
                Bitmap likeBitmapRes = ((BitmapDrawable) likeBtnDrawRes).getBitmap();

                String[] strHate = replyHateCount.getText().toString().split(" ");
                String[] strLike = replyLikeCount.getText().toString().split(" ");

                String reg_gnum = String.valueOf(mReplyInfoObjArr.get(index).reg_gNum);
                String reply_num = String.valueOf(mReplyInfoObjArr.get(index).num);
                String current_id = MainActivity.id;

                int countLike, countHate;

                // 댓글 싫어요 취소시
                if (hateBitmap.equals(hateBitmapRes)) {
                    replyHateBtn.setImageResource(R.drawable.hate_btn);
                    countHate = (Integer.parseInt(strHate[0]) - 1) == -1 ? 0 : Integer.parseInt(strHate[0]) - 1;
                    replyHateCount.setText(countHate + " 회");

                    try {
                        String resHateFavor = new NetworkReplyFavorUpdate().execute(reply_num, "-1", "hate").get();
                        int countResHateFavor = ContentParser.getResultJson(resHateFavor);
                        if (countResHateFavor == 1) {
                            try {
                                String resGet = new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "0", current_id, "is_exist").get();
                                int countGet = ReplyParser.getResultJson(resGet);
                                if (countGet == 0) {
                                    new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "0", current_id, "insert");
                                } else if (countGet == 1) {
                                    new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "0", current_id, "update");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 싫어요 눌렀을 시
                else {
                    replyHateBtn.setImageResource(R.drawable.hate_select_btn);
                    countHate = Integer.parseInt(strHate[0]) + 1;
                    replyHateCount.setText(countHate + " 회");

                    if (likeBitmap.equals(likeBitmapRes)) {
                        replyLikeBtn.setImageResource(R.drawable.like_btn);
                        countLike = (Integer.parseInt(strLike[0]) - 1) == -1 ? 0 : Integer.parseInt(strLike[0]) - 1;
                        replyLikeCount.setText(countLike + " 회");

                        new NetworkReplyFavorUpdate().execute(reply_num, "-1", "like");
                    }

                    try {
                        String resHateFavor = new NetworkReplyFavorUpdate().execute(reply_num, "1", "hate").get();
                        int countResHateFavor = ContentParser.getResultJson(resHateFavor);
                        if (countResHateFavor == 1) {
                            try {
                                String resGet = new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "-1", current_id, "is_exist").get();
                                int countGet = ReplyParser.getResultJson(resGet);
                                if (countGet == 0) {
                                    new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "-1", current_id, "insert");
                                } else if (countGet == 1) {
                                    new NetworkFavorSetInfo().execute(reg_gnum, reply_num, "0", "-1", current_id, "update");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (MainActivity.id.equals("")) {
            replyLikeBtn.setClickable(false);
            replyHateBtn.setClickable(false);
        } else {
            replyLikeBtn.setClickable(true);
            replyHateBtn.setClickable(true);
        }

        if (!mReplyInfoObjArr.get(i).reg_id.equals(MainActivity.id)) {
            btn_delete.setVisibility(View.INVISIBLE);
            btn_btnUpdate.setVisibility(View.INVISIBLE);
        } else {
            btn_delete.setVisibility(View.VISIBLE);
            btn_btnUpdate.setVisibility(View.VISIBLE);
        }

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strId = mReplyInfoObjArr.get(index).reg_id;
                AlertDialog.Builder ad = new AlertDialog.Builder(mAct);
                ad.setTitle("삭제하기");
                ad.setMessage(strId + "님의 댓글을 정말 삭제하시겠습니까?");

                ad.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String resDelete = new NetworkReplyDelete()
                                    .execute(mReplyInfoObjArr.get(index).num, mReplyInfoObjArr.get(index).reg_gNum).get();
                            int countDelete = ReplyParser.getResultJson(resDelete);
                            if (countDelete == 1) {
                                Toast.makeText(mAct, "삭제하였습니다", Toast.LENGTH_SHORT).show();
                                new NetworkFavorSetInfo().execute(mReplyInfoObjArr.get(index).reg_gNum,
                                        mReplyInfoObjArr.get(index).num, "0", "0", MainActivity.id, "delete");
                                try {
                                    ArrayList<ReplyInfo> replyInfos = new ArrayList<ReplyInfo>();
                                    String resGet = new NetworkReplyGet(Reply_Adapter.this)
                                            .execute(mReplyInfoObjArr.get(index).reg_gNum).get();
                                    int countGet = ReplyParser.getReplysInfoJson(resGet, replyInfos);
                                    mAct.replyCountView.setText("댓글 " + countGet + "개");
                                    Reply_Adapter.this.setDatas(replyInfos);
                                    Reply_Adapter.this.notifyDataSetInvalidated();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                ad.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mAct, "취소하였습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                ad.show();
            }
        });

        btn_btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = mInflater.inflate(R.layout.dialog_reply_update, null);
                String strContents = mReplyInfoObjArr.get(index).contents;
                ((TextView) view.findViewById(R.id.replyUpdateEdt)).setText(strContents);

                new androidx.appcompat.app.AlertDialog.Builder(mAct)
                        .setTitle("댓글 수정")
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String num = mReplyInfoObjArr.get(index).num;
                                String contents = ((EditText) view.findViewById(R.id.replyUpdateEdt)).getText().toString();
                                String reg_gnum = mReplyInfoObjArr.get(index).reg_gNum;

                                try {
                                    String res = new NetworkReplyUpdate(Reply_Adapter.this).execute(num, contents, reg_gnum).get();
                                    int count = ReplyParser.getResultJson(res);
                                    if (count == 1)
                                        Toast.makeText(mAct, "수정하였습니다", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.cancel();
                                Toast.makeText(mAct, "취소하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .show();

            }
        });
        return view;
    }
}
