package vip.rinck.imlc.factory.presenter.message;

import vip.rinck.imlc.factory.data.helper.UserHelper;
import vip.rinck.imlc.factory.data.message.MessageDataSource;
import vip.rinck.imlc.factory.data.message.MessageRepository;
import vip.rinck.imlc.factory.model.db.Message;
import vip.rinck.imlc.factory.model.db.User;

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
implements ChatContract.Presenter{

    public ChatUserPresenter( ChatContract.UserView view, String receiverId) {
        //数据源，View，接收者，接收者的类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();

        //从本地获取该用户信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);
    }
}
