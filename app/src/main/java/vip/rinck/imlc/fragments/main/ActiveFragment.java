package vip.rinck.imlc.fragments.main;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import vip.rinck.imlc.common.widget.GalleryView;
import vip.rinck.imlc.common.widget.PortraitView;
import vip.rinck.imlc.common.widget.recycler.RecyclerAdapter;
import vip.rinck.imlc.factory.model.db.Session;
import vip.rinck.imlc.factory.model.db.User;
import vip.rinck.imlc.factory.presenter.message.SessionContract;
import vip.rinck.imlc.factory.presenter.message.SessionPresenter;
import vip.rinck.imlc.utils.DateTimeUtil;


public class ActiveFragment extends PresenterFragment<SessionContract.Presenter>
implements SessionContract.View{

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    //适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<Session> mAdapter;



    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });
        //点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session session) {
                //跳转到聊天界面
                MessageActivity.show(getContext(),session);
            }
        });

        //初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceholderView(mEmptyView);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        //进行一次数据加载
        mPresenter.start();
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceholderView.triggerOkOrEmpty(mAdapter.getItemCount()>0);
    }


    //界面数据渲染
    class ViewHolder extends RecyclerAdapter.ViewHolder<Session>{
        @BindView(R.id.iv_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.tv_username)
        TextView mUsername;

        @BindView(R.id.tv_content)
        TextView mContent;

        @BindView(R.id.tv_time)
        TextView mTime;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {

            mPortraitView.setup(Glide.with(ActiveFragment.this),session.getPicture());
            mUsername.setText(session.getTitle());
            mContent.setText(TextUtils.isEmpty(session.getContent())?"":session.getContent());
            mTime.setText(DateTimeUtil.getSampleDate(session.getModifyAt()));


        }

    }


}
