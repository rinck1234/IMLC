package vip.rinck.imlc.factory.presenter.contact;

import android.graphics.drawable.AnimationDrawable;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import vip.rinck.imlc.factory.Factory;
import vip.rinck.imlc.factory.data.helper.UserHelper;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.persistence.Account;
import vip.rinck.imlc.factory.presenter.BasePresenter;

public class PersonalPresenter extends BasePresenter<PersonalContract.View>
implements PersonalContract.Prsenter{

    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();



        //个人界面用户数据优先从网络获取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if(view!=null) {


                    String id =view.getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(view,user);
                }
            }
        });

    }

    private void  onLoaded(final PersonalContract.View view,final User user){
        this.user = user;
        //是否是自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        //是否已经关注
        final boolean isFollow = isSelf ||user.isFollow();
        final boolean allowSayHello = isFollow && !isSelf;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHello(allowSayHello);
            }
        });



    }

    @Override
    public User getUserPersonal() {
        return null;
    }
}
