package vip.rinck.imlc.factory.presenter.contact;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.data.helper.UserHelper;
import vip.rinck.imlc.factory.model.card.UserCard;
import vip.rinck.imlc.factory.presenter.BasePresenter;

public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter,DataSource.Callback<UserCard>{
    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();
        UserHelper.follow(id,this);
    }

    @Override
    public void onDataLoaded(final UserCard userCard) {
        //成功
        final FollowContract.View view = getView();
        if(view!=null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSuccess(userCard);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final FollowContract.View view = getView();
        if(view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
