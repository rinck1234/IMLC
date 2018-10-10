package vip.rinck.imlc.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import java.util.List;

import vip.rinck.imlc.factory.data.helper.MessageHelper;
import vip.rinck.imlc.factory.data.message.MessageDataSource;
import vip.rinck.imlc.factory.model.api.message.MsgCreateModel;
import vip.rinck.imlc.factory.model.db.Message;
import vip.rinck.imlc.factory.persistence.Account;
import vip.rinck.imlc.factory.presenter.BaseSourcePresenter;
import vip.rinck.imlc.factory.utils.DiffUiDataCallback;

/**
 * 聊天Presenter的基础类
 */
public class ChatPresenter<View extends ChatContract.View>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.Presenter {

    //接收者Id 群Id 或用户Id
    protected String mReceiverId;
    //区分人或群
    protected int mReceiverType;

    public ChatPresenter(MessageDataSource source, View view,String receiverId,int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }


    @Override
    public void pushText(String content) {
        MsgCreateModel model =new MsgCreateModel.Builder()
                .receiver(mReceiverId,mReceiverType)
                .content(content,Message.TYPE_STR)
                .build();
        //进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void pushAudio(String path) {
        //TODO
    }

    @Override
    public void pushImages(String[] paths) {
        //TODO
    }

    @Override
    public boolean rePush(Message message) {
        //确定消息是可以重复发送的
        if(Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                &&message.getStatus()==Message.STATUS_FAILED){
            //更改状态
            message.setStatus(Message.STATUS_CREATED);
            //构建发送model
            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.View view = getView();
        if(view==null)
            return;

        //拿到老数据
        List<Message> old = view.getRecyclerAdapter().getItems();

        //差异计算
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old,messages);

        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //进行界面刷新
        refreshData(result,messages);
    }

}
