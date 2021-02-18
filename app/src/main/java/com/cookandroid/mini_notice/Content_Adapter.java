package com.cookandroid.mini_notice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.mini_notice.NetWork.ContentParser;
import com.cookandroid.mini_notice.NetWork.FavorParser;
import com.cookandroid.mini_notice.NetWork.NetworkContentDelete;
import com.cookandroid.mini_notice.NetWork.NetworkContentFavorCountGet;
import com.cookandroid.mini_notice.NetWork.NetworkContentFavorUpdate;
import com.cookandroid.mini_notice.NetWork.NetworkContentUpdate;
import com.cookandroid.mini_notice.NetWork.NetworkFavorSetInfo;
import com.cookandroid.mini_notice.NetWork.NetworkReplyGet;
import com.cookandroid.mini_notice.NetWork.NetworkReplyInsert;
import com.cookandroid.mini_notice.NetWork.ReplyParser;

import java.util.ArrayList;

public class Content_Adapter extends BaseAdapter {
    private ContentActivity mAct;
    LayoutInflater mInflater;
    ArrayList<ContentInfo> mContentInfoObjArr;

    Reply_Adapter replyAdapter;
    int mListLayout;

    Intent intent;

    public Content_Adapter(ContentActivity a, int listLayout, ArrayList<ContentInfo> contentsInfoObjArrayListT,
                           Reply_Adapter replyAdapter) {
        mAct = a;
        mListLayout = listLayout;
        mContentInfoObjArr = contentsInfoObjArrayListT;
        mInflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.replyAdapter = replyAdapter;
    }

    public void setDatas(ArrayList<ContentInfo> arrayList) {
        mContentInfoObjArr = arrayList;
    }

    @Override
    public int getCount() {
        return mContentInfoObjArr.size();
    }

    @Override
    public Object getItem(int i) {
        return mContentInfoObjArr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = mInflater.inflate(mListLayout, viewGroup, false);

        SpannableString contentTextView = new SpannableString(String.valueOf(mContentInfoObjArr.get(i).title));
        contentTextView.setSpan(new UnderlineSpan(), 0, String.valueOf(mContentInfoObjArr.get(i).title).length(), 0);
        final TextView titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(contentTextView);

        final TextView writerView = (TextView) view.findViewById(R.id.writerView);
        writerView.setText(mContentInfoObjArr.get(i).reg_id);

        final TextView countView = (TextView) view.findViewById(R.id.countView);
        countView.setText(" ㆍ 조회수 " + String.valueOf(mContentInfoObjArr.get(i).readCount) + "회 ㆍ ");

        final TextView dateView = (TextView) view.findViewById(R.id.dateView);
        String[] strDate = mContentInfoObjArr.get(i).reg_date.split("\n");
        dateView.setText(strDate[0] + " " + strDate[1]);

        final TextView contentsView = (TextView) view.findViewById(R.id.contentsView);
        contentsView.setText(String.valueOf(mContentInfoObjArr.get(i).contents));

        final ImageView modifyBtn = (ImageView) view.findViewById(R.id.modifyBtn);
        final ImageView loginBtn = (ImageView) view.findViewById(R.id.contentLoginBtn);
        final ImageView deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);

        final EditText replyEdit = (EditText) view.findViewById(R.id.replyEdt);
        final Button replyInputBtn = (Button) view.findViewById(R.id.replyInputBtn);

        final ImageView contentLikeBtn = (ImageView) view.findViewById(R.id.contentLikeBtn);
        final TextView contentLikeCount = (TextView) view.findViewById(R.id.contentLikeCount);
        final ImageView contentHateBtn = (ImageView) view.findViewById(R.id.contentHateBtn);
        final TextView contentHateCount = (TextView) view.findViewById(R.id.contentHateCount);

        loginBtn.setVisibility(View.GONE);

        try {
            // Content 좋아요/싫어요 갯수 처리
            String reg_gnum = String.valueOf(mContentInfoObjArr.get(i).num);
            String resFavorCount = new NetworkContentFavorCountGet().execute(reg_gnum).get();
            String[] contentInfo = ContentParser.getContentsInfoArr(resFavorCount, new String[8]);

            contentLikeCount.setText((contentInfo[4] == null ? "0" : contentInfo[4]) + " 회");
            contentHateCount.setText((contentInfo[5] == null ? "0" : contentInfo[5]) + " 회");

            // Content 댓글 갯수 처리
            ArrayList<ReplyInfo> replyInfos = new ArrayList<ReplyInfo>();
            String resGet = new NetworkReplyGet(replyAdapter).execute(reg_gnum).get();
            int countGet = ReplyParser.getReplysInfoJson(resGet, replyInfos);
            mAct.replyCountView.setText("댓글 " + countGet + "개");

            // 좋아요 or 싫어요 현황 처리
            if (!MainActivity.id.equals("")) {
                ArrayList<FavorInfo> favorInfo = new ArrayList<FavorInfo>();
                String resFavor = new NetworkFavorSetInfo().execute(reg_gnum, "0", "1", "", MainActivity.id, "get").get();
                ArrayList<FavorInfo> arrFavor = FavorParser.getFavorsInfoJson(resFavor, favorInfo);
                if (arrFavor.size() > 0)
                    if (arrFavor.get(0).isReply.equals("1")) { // 주글일때,
                        if (arrFavor.get(0).isLike.equals("1")) { // 좋아요 선택했었을 때
                            contentLikeBtn.setImageResource(R.drawable.like_select_btn);
                            contentHateBtn.setImageResource(R.drawable.hate_btn);
                        } else if (arrFavor.get(0).isLike.equals("0")) { // 아무것도 누르지 않은 상태
                            contentLikeBtn.setImageResource(R.drawable.like_btn);
                            contentHateBtn.setImageResource(R.drawable.hate_btn);
                        } else { // 싫어요 선택했었을 때
                            contentLikeBtn.setImageResource(R.drawable.like_btn);
                            contentHateBtn.setImageResource(R.drawable.hate_select_btn);
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int index = i;
        contentLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable likeBtnDraw = contentLikeBtn.getDrawable();
                Drawable likeBtnDrawRes = v.getResources().getDrawable(R.drawable.like_select_btn);
                Drawable hateBtnDraw = contentHateBtn.getDrawable();
                Drawable hateBtnRes = v.getResources().getDrawable(R.drawable.hate_select_btn);

                Bitmap likeBitmap = ((BitmapDrawable) likeBtnDraw).getBitmap();
                Bitmap likeBitmapRes = ((BitmapDrawable) likeBtnDrawRes).getBitmap();

                Bitmap hateBitmap = ((BitmapDrawable) hateBtnDraw).getBitmap();
                Bitmap hateBitmapRes = ((BitmapDrawable) hateBtnRes).getBitmap();

                String[] strLike = contentLikeCount.getText().toString().split(" ");
                String[] strHate = contentHateCount.getText().toString().split(" ");
                String num = String.valueOf(mContentInfoObjArr.get(index).num);
                String current_id = MainActivity.id;

                int countLike, countHate;

                // 게시물 좋아요 취소시
                if (likeBitmap.equals(likeBitmapRes)) {
                    contentLikeBtn.setImageResource(R.drawable.like_btn);
                    countLike = (Integer.parseInt(strLike[0]) - 1) == -1 ? 0 : Integer.parseInt(strLike[0]) - 1;
                    contentLikeCount.setText(countLike + " 회");

                    try {
                        String resLikeFavor = new NetworkContentFavorUpdate().execute(num, "-1", "like").get();
                        int countResLikeFavor = ContentParser.getResultJson(resLikeFavor);
                        if (countResLikeFavor == 1) {
                            try {
                                String resGet = new NetworkFavorSetInfo().execute(num, "0", "1", "0", current_id, "is_exist").get();
                                int countGet = ContentParser.getResultJson(resGet);
                                if (countGet == 0) {
                                    new NetworkFavorSetInfo().execute(num, "0", "1", "0", current_id, "insert");
                                } else if (countGet == 1) {
                                    new NetworkFavorSetInfo().execute(num, "0", "1", "0", current_id, "update");
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
                    contentLikeBtn.setImageResource(R.drawable.like_select_btn);
                    countLike = Integer.parseInt(strLike[0]) + 1;
                    contentLikeCount.setText(countLike + " 회");

                    if (hateBitmap.equals(hateBitmapRes)) {
                        contentHateBtn.setImageResource(R.drawable.hate_btn);
                        countHate = (Integer.parseInt(strHate[0]) - 1) == -1 ? 0 : Integer.parseInt(strHate[0]) - 1;
                        contentHateCount.setText(countHate + " 회");

                        new NetworkContentFavorUpdate().execute(num, "-1", "hate");

                    }

                    try {
                        String resLikeFavor = new NetworkContentFavorUpdate().execute(num, "1", "like").get();
                        int countResLikeFavor = ContentParser.getResultJson(resLikeFavor);
                        if (countResLikeFavor == 1) {
                            try {
                                String resGet = new NetworkFavorSetInfo().execute(num, "0", "1", "1", current_id, "is_exist").get();
                                int countGet = ContentParser.getResultJson(resGet);
                                if (countGet == 0) {
                                    new NetworkFavorSetInfo().execute(num, "0", "1", "1", current_id, "insert");
                                } else if (countGet == 1) {
                                    new NetworkFavorSetInfo().execute(num, "0", "1", "1", current_id, "update");
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

        contentHateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable hateBtnDraw = contentHateBtn.getDrawable();
                Drawable hateBtnRes = v.getResources().getDrawable(R.drawable.hate_select_btn);

                Drawable likeBtnDraw = contentLikeBtn.getDrawable();
                Drawable likeBtnDrawRes = v.getResources().getDrawable(R.drawable.like_select_btn);

                Bitmap hateBitmap = ((BitmapDrawable) hateBtnDraw).getBitmap();
                Bitmap hateBitmapRes = ((BitmapDrawable) hateBtnRes).getBitmap();

                Bitmap likeBitmap = ((BitmapDrawable) likeBtnDraw).getBitmap();
                Bitmap likeBitmapRes = ((BitmapDrawable) likeBtnDrawRes).getBitmap();

                String[] strHate = contentHateCount.getText().toString().split(" ");
                String[] strLike = contentLikeCount.getText().toString().split(" ");
                String num = String.valueOf(mContentInfoObjArr.get(index).num);
                String current_id = MainActivity.id;

                int countLike, countHate;

                // 싫어요 취소시
                if (hateBitmap.equals(hateBitmapRes)) {
                    contentHateBtn.setImageResource(R.drawable.hate_btn);
                    countHate = (Integer.parseInt(strHate[0]) - 1) == -1 ? 0 : Integer.parseInt(strHate[0]) - 1;
                    contentHateCount.setText(countHate + " 회");

                    try {
                        String resHateFavor = new NetworkContentFavorUpdate().execute(num, "-1", "hate").get();
                        int countResHateFavor = ContentParser.getResultJson(resHateFavor);
                        if (countResHateFavor == 1) {
                            try {
                                String resGet = new NetworkFavorSetInfo().execute(num, "0", "1", "0", current_id, "is_exist").get();
                                int countGet = ContentParser.getResultJson(resGet);
                                if (countGet == 0) {
                                    new NetworkFavorSetInfo().execute(num, "0", "1", "0", current_id, "insert");
                                } else if (countGet == 1) {
                                    new NetworkFavorSetInfo().execute(num, "0", "1", "0", current_id, "update");
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
                    contentHateBtn.setImageResource(R.drawable.hate_select_btn);
                    countHate = Integer.parseInt(strHate[0]) + 1;
                    contentHateCount.setText(countHate + " 회");

                    if (likeBitmap.equals(likeBitmapRes)) {
                        contentLikeBtn.setImageResource(R.drawable.like_btn);
                        countLike = (Integer.parseInt(strLike[0]) - 1) == -1 ? 0 : Integer.parseInt(strLike[0]) - 1;
                        contentLikeCount.setText(countLike + " 회");

                        new NetworkContentFavorUpdate().execute(num, "-1", "like");
                    }

                    try {
                        String resHateFavor = new NetworkContentFavorUpdate().execute(num, "1", "hate").get();
                        int countResHateFavor = ContentParser.getResultJson(resHateFavor);
                        if (countResHateFavor == 1) {
                            try {
                                String resGet = new NetworkFavorSetInfo().execute(num, "0", "1", "-1", current_id, "is_exist").get();
                                int countGet = ContentParser.getResultJson(resGet);
                                if (countGet == 0) {
                                    new NetworkFavorSetInfo().execute(num, "0", "1", "-1", current_id, "insert");
                                } else if (countGet == 1) {
                                    new NetworkFavorSetInfo().execute(num, "0", "1", "-1", current_id, "update");
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
            modifyBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            replyEdit.setEnabled(false);
            contentLikeBtn.setClickable(false);
            contentHateBtn.setClickable(false);
            replyInputBtn.setEnabled(false);
        } else {
            replyEdit.setEnabled(true);
            contentLikeBtn.setClickable(true);
            contentHateBtn.setClickable(true);
            replyInputBtn.setEnabled(true);
        }

        if (!mContentInfoObjArr.get(i).reg_id.equals(MainActivity.id)) {
            modifyBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
        } else {
            modifyBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
        }

//        fileUploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                mAct.startActivityForResult(intent, 101);
//            }
//        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = mInflater.inflate(R.layout.content_insert, null);
                String strNum = String.valueOf(mContentInfoObjArr.get(index).num);
                String strTitle = String.valueOf(mContentInfoObjArr.get(index).title);
                String strContents = String.valueOf(mContentInfoObjArr.get(index).contents);
                ((TextView) view.findViewById(R.id.input_title)).setText(strTitle);
                ((TextView) view.findViewById(R.id.input_contents)).setText(strContents);
                ((Button) view.findViewById(R.id.submitBtn)).setVisibility(View.GONE);
                ((TextView) view.findViewById(R.id.contentWriteTitle)).setVisibility(View.GONE);
                ((ImageView) view.findViewById(R.id.contentWriteBackBtn)).setVisibility(View.GONE);
                new androidx.appcompat.app.AlertDialog.Builder(mAct)
                        .setTitle("게시판 수정")
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = ((EditText) view.findViewById(R.id.input_title)).getText().toString();
                                String contents = ((EditText) view.findViewById(R.id.input_contents)).getText().toString();
                                ((Button) view.findViewById(R.id.submitBtn)).setVisibility(View.VISIBLE);
                                new NetworkContentUpdate(Content_Adapter.this).execute(strNum, title, contents);
                                Toast.makeText(mAct, "게시글을 수정하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                ((Button) view.findViewById(R.id.submitBtn)).setVisibility(View.VISIBLE);
                                dialogInterface.cancel();
                                Toast.makeText(mAct, "취소하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .show();

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNum = String.valueOf(mContentInfoObjArr.get(index).num);
                String strTitle = String.valueOf(mContentInfoObjArr.get(index).title);
                android.app.AlertDialog.Builder ad = new android.app.AlertDialog.Builder(mAct);
                ad.setTitle("삭제하기");
                ad.setMessage(strTitle + " 를(을) 정말 삭제하시겠습니까?");

                ad.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String res = new NetworkContentDelete().execute(strNum).get();
                            int count = ContentParser.getResultJson(res);
                            if (count == 1) {
                                String resFavorDelete = new NetworkFavorSetInfo().execute(strNum, "0", "1", "0", MainActivity.id, "delete").get();
                                int countFavorDelete = FavorParser.getResultJson(resFavorDelete);
                                if (countFavorDelete >= 1) {
                                    mAct.finish();
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

        replyInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contents = replyEdit.getText().toString();
                String reg_id = MainActivity.id;
                String reg_gnum = String.valueOf(mContentInfoObjArr.get(index).num);
                ArrayList<ReplyInfo> replyInfos = new ArrayList<ReplyInfo>();
                try {
                    String resInsert = new NetworkReplyInsert().execute(contents, reg_id, reg_gnum).get();
                    int countInsert = ReplyParser.getResultJson(resInsert);
                    if (countInsert == 1) {
                        try {
                            String resGet = new NetworkReplyGet(replyAdapter).execute(reg_gnum).get();
                            int countGet = ReplyParser.getReplysInfoJson(resGet, replyInfos);
                            mAct.replyCountView.setText("댓글 " + countGet + "개");

                            Toast.makeText(mAct, "댓글을 추가하셨습니다.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                replyEdit.setText("");
            }
        });

        return view;
    }
}
