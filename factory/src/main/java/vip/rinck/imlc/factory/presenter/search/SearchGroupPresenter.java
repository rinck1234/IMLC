package vip.rinck.imlc.factory.presenter.search;

import vip.rinck.imlc.factory.presenter.BasePresenter;

/**
 * 搜索群的逻辑实现
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
implements SearchContract.Presenter{
    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
