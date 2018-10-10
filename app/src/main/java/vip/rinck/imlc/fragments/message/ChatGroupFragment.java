package vip.rinck.imlc.fragments.message;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vip.rinck.imlc.R;
import vip.rinck.imlc.factory.model.db.Group;
import vip.rinck.imlc.factory.presenter.message.ChatContract;

/**
 * 群聊天界面
 */
public class ChatGroupFragment extends ChatFragment<Group>
implements ChatContract.GroupView{


    public ChatGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }


    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}
