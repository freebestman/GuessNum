package com.best.bruce.guessnum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {


    //    private TextView tvTip, tvCount;
//    private EditText editNum;
    private Guess guess;
    private int inputNum = 4;
    private int randNum;
    private int count = 0;
    private boolean flagOfReplay = false;
    private AlertDialog.Builder builder;
    private ListView msgListView;
    private EditText inputText;
    private Button send, replay;
    private MsgAdapter adapter;
    private Vibrator vibrator;
    private List<Msg> msgList = new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        guess = new Guess();
        inits();//初始化数据
        adapter = new MsgAdapter(MainActivity.this, R.layout.msg_item, msgList);
        inputText = (EditText) findViewById(R.id.inputText);
        send = (Button) findViewById(R.id.send);
        replay = (Button) findViewById(R.id.replay);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);

        //添加两个按钮[send]-[replay]的事件监听器
        send.setOnClickListener(this);
        replay.setOnClickListener(this);
        builder = new AlertDialog.Builder(MainActivity.this);

        //添加两个按钮[Back]-[Help]的事件监听器
        findViewById(R.id.titleBack).setOnClickListener(this);
        findViewById(R.id.titleHelp).setOnClickListener(this);

    }

    private void inits() {
        Msg msg1 = new Msg("欢迎来玩游戏! 有什么不清楚可以按[Help]寻求帮助", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        randNum = guess.getRandomNum();
    }

    private void removeAllMsgs() {
        msgList = new ArrayList<Msg>();

    }

    @Override
    public void onClick(View v) {
        vibrator.vibrate(100);
        switch (v.getId()) {
            case R.id.titleBack:
                //退出程序

                this.finish();
                break;
            case R.id.titleHelp:
//                vibrator.vibrate(new long[]{10,100,20,200},-1);
                //弹出帮助提示文档
                builder.setTitle(R.string.helpTitle).
                        setMessage(R.string.helpText).
                        setPositiveButton("确定", null).show();
                break;
            case R.id.send:
                Log.d("GuessNum","Send");
                String content = inputText.getText().toString();
                if (content.length() == 0) {
                    Toast.makeText(MainActivity.this, R.string.notNull, Toast.LENGTH_SHORT).show();
                    break;
                }
                inputNum = Integer.parseInt(content);
                if (inputNum > 100 || inputNum < 1) {
                    Toast.makeText(MainActivity.this, R.string.invalidNum, Toast.LENGTH_SHORT).show();
                    break;
                }
                if (flagOfReplay) {
                    msgList.add(new Msg("不要淘气，按[Replay]键重新开始游戏", Msg.TYPE_RECEIVED));
                } else {
                    Msg msgs = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msgs);
                    //判断输入的数字是否合法
                    String rcvString = guess.transResult(guess.judeResult(inputNum, randNum));
                    Msg msgr = new Msg(rcvString, Msg.TYPE_RECEIVED);
                    msgList.add(msgr);
                    count++;
                    if (guess.judeResult(inputNum, randNum) == 0) {
                        flagOfReplay = true;
                        String rcv1String = "你总共花了" + String.valueOf(count).toString() + "次猜中";
                        msgList.add(new Msg(rcv1String, Msg.TYPE_RECEIVED));
                    }
                }

                adapter.notifyDataSetChanged();
                msgListView.setSelection(msgList.size());
                inputText.setText("");


                break;

            case R.id.replay:
                if(!flagOfReplay && count != 0){
                    builder.setTitle(R.string.tips).
                            setMessage(R.string.sureOfReplay).
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    flagOfReplay = true;
                                }
                            }).
                            setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    flagOfReplay = false;
                                }
                            }).show();

                }
                if(flagOfReplay){
                count = 0;
                flagOfReplay = false;
                removeAllMsgs();
                inits();
                msgListView.setAdapter(null);
                adapter = new MsgAdapter(MainActivity.this, R.layout.msg_item, msgList);
                msgListView.setAdapter(adapter);
                }
                break;
        }

    }
}
