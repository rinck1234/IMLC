package vip.rinck.imlc.activities;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;

import vip.rinck.imlc.R;
import vip.rinck.imlc.factory.model.Author;

public class MessageActivity extends Activity {

    /**
     * 显示人的聊天信息
     * @param context
     * @param author
     */
    public static void show(Context context,Author author){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

}
