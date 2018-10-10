package vip.rinck.imlc.fragments.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

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
import vip.rinck.imlc.common.widget.convention.PlaceHolderView;
import vip.rinck.imlc.common.widget.recycler.RecyclerAdapter;
import vip.rinck.imlc.factory.model.card.GroupCard;
import vip.rinck.imlc.factory.model.card.UserCard;
import vip.rinck.imlc.factory.presenter.contact.FollowContract;
import vip.rinck.imlc.factory.presenter.contact.FollowPresenter;
import vip.rinck.imlc.factory.presenter.search.SearchContract;
import vip.rinck.imlc.factory.presenter.search.SearchGroupPresenter;

/**
 * 搜索群的界面实现
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter>
implements SearchActivity.SearchFragment,SearchContract.GroupView{


    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<GroupCard> mAdapter;

    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        //初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<GroupCard>() {
            @Override
            protected int getItemViewType(int position, GroupCard groupCard) {
                return R.layout.cell_search_group_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new SearchGroupFragment.ViewHolder(root);
            }
        });
        mEmptyView.bind(mRecycler);
        setPlaceholderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        //发起首次搜索
        search("");
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    public void setPlaceholderView(PlaceHolderView placeholderView) {
        super.setPlaceholderView(placeholderView);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        //查找成功返回数据
        mAdapter.replace(groupCards);
        //没有数据显示空布局
        mPlaceholderView.triggerOkOrEmpty(mAdapter.getItemCount()>0);

    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> {
        @BindView(R.id.iv_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.tv_name)
        TextView mName;

        @BindView(R.id.iv_join)
        ImageView mJoin;

        public ViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        protected void onBind(GroupCard groupCard) {
            mPortraitView.setup(Glide.with(SearchGroupFragment.this), groupCard.getPicture());
            mName.setText(groupCard.getName());
            //加入时间判断是否加入群
            mJoin.setEnabled(groupCard.getJoinAt()==null);
        }

        @OnClick(R.id.iv_join)
        void onJoinClick(){
            PersonalActivity.show(getContext(),mData.getOwnerId());
        }

    }
}
