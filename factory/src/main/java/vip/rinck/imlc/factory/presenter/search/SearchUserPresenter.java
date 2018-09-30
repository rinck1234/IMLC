package vip.rinck.imlc.factory.presenter.search;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;
import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.data.helper.UserHelper;
import vip.rinck.imlc.factory.model.card.UserCard;
import vip.rinck.imlc.factory.presenter.BasePresenter;

/**
 * 搜索人的逻辑实现
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
implements SearchContract.Presenter,DataSource.Callback<List<UserCard>>{

    private Call searchCall;

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        Call call = searchCall;
        if(searchCall!=null&&!searchCall.isCanceled()){
            //如果有上一次的请求还未完成，则取消本次请求
            call.cancel();
        }
        searchCall = UserHelper.search(content,this);
    }

    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        //搜索成功
        final SearchContract.UserView view = getView();
        if(view!=null)
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(userCards);
                }
            });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        //搜索失败
        final SearchContract.UserView view = getView();
        if(view!=null)
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
    }
}
