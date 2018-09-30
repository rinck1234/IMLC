package vip.rinck.imlc.fragments.main;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import vip.rinck.imlc.R;
import vip.rinck.imlc.activities.MessageActivity;
import vip.rinck.imlc.activities.PersonalActivity;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.common.app.PresenterFragment;
import vip.rinck.imlc.common.widget.EmptyView;
import vip.rinck.imlc.common.widget.PortraitView;
import vip.rinck.imlc.common.widget.recycler.RecyclerAdapter;
import vip.rinck.imlc.factory.model.card.UserCard;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.presenter.BaseContract;
import vip.rinck.imlc.factory.presenter.contact.ContactContract;
import vip.rinck.imlc.factory.presenter.contact.ContactPresenter;
import vip.rinck.imlc.fragments.search.SearchUserFragment;


public class ContactFragment extends PresenterFragment<ContactContract.Presenter>
implements ContactContract.View{


    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    //适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<User> mAdapter;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User userCard) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });
        //点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, User user) {
                //跳转到聊天界面
                MessageActivity.show(getContext(),user);
            }
        });

        //初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceholderView(mEmptyView);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        //初始化Presenter
        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        //进行界面操作
        mPlaceholderView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<User>{
        @BindView(R.id.iv_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.tv_username)
        TextView mUsername;

        @BindView(R.id.tv_desc)
        TextView mDesc;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {

            mPortraitView.setup(Glide.with(ContactFragment.this),user);
            mUsername.setText(user.getUsername());
            mDesc.setText(user.getDesc());

        }

        @OnClick(R.id.iv_portrait)
        void onPortraitClick(){
            PersonalActivity.show(getContext(),mData.getId());
        }
    }



}
