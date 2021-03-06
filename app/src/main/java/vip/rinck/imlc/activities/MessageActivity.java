package vip.rinck.imlc.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import vip.rinck.imlc.R;
import vip.rinck.imlc.common.app.Activity;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.factory.model.Author;
import vip.rinck.imlc.factory.model.db.Group;
import vip.rinck.imlc.factory.model.db.Message;
import vip.rinck.imlc.factory.model.db.Session;
import vip.rinck.imlc.fragments.message.ChatGroupFragment;
import vip.rinck.imlc.fragments.message.ChatUserFragment;

public class MessageActivity extends Activity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    //接收者ID 可以是人 也可以是群
    public static final String KEY_RECEIVER_ID="KEY_RECEIVER_ID";
    //是否是群
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;

    /**
     * 通过Session发起聊天
     * @param context
     * @param session
     */
    public static void show(Context context,Session session){

        if(session==null||context==null|| TextUtils.isEmpty(session.getId()))
            return;
        Intent intent = new Intent(context,MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID,session.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP,session.getReceiverType()== Message.RECEIVER_TYPE_GROUP);

        context.startActivity(intent);
    }

    /**
     * 显示人的聊天信息
     * @param context
     * @param author
     */
    public static void show(Context context,Author author){

        if(author==null||context==null|| TextUtils.isEmpty(author.getId()))
            return;
        Intent intent = new Intent(context,MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID,author.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP,false);

        context.startActivity(intent);
    }

    /**
     * 发起群聊天
     * @param context
     * @param group 群Model
     */
    public static void show(Context context,Group group){
        if(group==null||context==null|| TextUtils.isEmpty(group.getId()))
            return;
        Intent intent = new Intent(context,MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID,group.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP,true);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        Fragment fragment;
        if(mIsGroup)
            fragment = new ChatGroupFragment();
        else
            fragment = new ChatUserFragment();

        //从Activity传递参数到Fragment中
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID,mReceiverId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container,fragment)
                .commit();
    }
}
