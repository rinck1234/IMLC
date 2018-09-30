package vip.rinck.imlc.fragments.search;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import vip.rinck.imlc.R;
import vip.rinck.imlc.activities.PersonalActivity;
import vip.rinck.imlc.activities.SearchActivity;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.common.app.PresenterFragment;
import vip.rinck.imlc.common.widget.EmptyView;
import vip.rinck.imlc.common.widget.PortraitView;
import vip.rinck.imlc.common.widget.recycler.RecyclerAdapter;
import vip.rinck.imlc.factory.model.card.UserCard;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.presenter.contact.FollowContract;
import vip.rinck.imlc.factory.presenter.contact.FollowPresenter;
import vip.rinck.imlc.factory.presenter.search.SearchContract;
import vip.rinck.imlc.factory.presenter.search.SearchUserPresenter;


/**
 * 搜索人的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
implements SearchActivity.SearchFragment,SearchContract.UserView{

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;



    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {

    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>(){
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
        //初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceholderView(mEmptyView);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initData() {
        super.initData();
        //发起首次搜索
        search("");
    }

    @Override
    public void search(String content) {
        //Activity->Fragment->Presenter->Net
        mPresenter.search(content);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        //初始化Presenter
        return new SearchUserPresenter(this);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        //数据成功 返回数据
        mAdapter.replace(userCards);
        //有数据ok，没数据显示空
        mPlaceholderView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
    implements FollowContract.View{
        @BindView(R.id.iv_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.tv_username)
        TextView mUsername;

        @BindView(R.id.iv_follow)
        ImageView mFollow;

        FollowContract.Presenter mPresenter;

        public ViewHolder(View itemView) {
            super(itemView);
            //当前View和Presenter
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            mPortraitView.setup(Glide.with(SearchUserFragment.this),userCard);
            mUsername.setText(userCard.getUsername());
            mFollow.setEnabled(!userCard.isFollow());
        }

        @OnClick(R.id.iv_portrait)
        void onPortraitClick(){
            PersonalActivity.show(getContext(),mData.getId());
        }

        @Override
        public void onFollowSuccess(UserCard userCard) {
            if(mFollow.getDrawable() instanceof LoadingDrawable){
                ((LoadingDrawable)mFollow.getDrawable()).stop();
                //设置为默认的
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }
            //发起更新
            updateData(userCard);
        }

        @OnClick(R.id.iv_follow)
        void onFollowClick(){
            //发起关注
            mPresenter.follow(mData.getId());
        }

        @Override
        public void showError(int str) {
            //更改当前界面状态
            if(mFollow.getDrawable() instanceof LoadingDrawable){
                //失败则停止动画，并显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable)mFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(),22);
            int maxSize = (int) Ui.dipToPx(getResources(),30);
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize,maxSize);
            drawable.setBackgroundColor(0);
            int[] color = new int[]{UiCompat.getColor(getResources(),R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            //设置进去
            mFollow.setImageDrawable(drawable);
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }
    }


}
