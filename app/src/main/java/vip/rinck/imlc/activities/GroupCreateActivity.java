package vip.rinck.imlc.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import vip.rinck.imlc.R;
import vip.rinck.imlc.common.app.Application;
import vip.rinck.imlc.common.app.PresenterToolbarActivity;
import vip.rinck.imlc.common.app.ToolbarActivity;
import vip.rinck.imlc.common.widget.PortraitView;
import vip.rinck.imlc.common.widget.recycler.RecyclerAdapter;
import vip.rinck.imlc.factory.presenter.group.GroupCreateContract;
import vip.rinck.imlc.factory.presenter.group.GroupCreatePresenter;
import vip.rinck.imlc.fragments.media.GalleryFragment;

public class GroupCreateActivity extends PresenterToolbarActivity<GroupCreateContract.Presenter>
implements GroupCreateContract.View{

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.et_name)
    EditText mName;

    @BindView(R.id.et_desc)
    EditText mDesc;

    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;

    private String mPortraitPath;
    private Adapter mAdapter;

    public static void show(Context context){
        context.startActivity(new Intent(context,GroupCreateActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        /*mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new Adapter());
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @OnClick(R.id.iv_portrait)
    void onPortraitClick(){
        hideSoftKeyboard();
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        //设置图片处理的格式
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置压缩后的图片精度
                        options.setCompressionQuality(96);
                        //得到头像的缓存地址
                        File dPath = Application.getPortraitTmpFile();
                        //发起剪切
                        UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(dPath))
                                .withAspectRatio(1,1)
                                .withMaxResultSize(520,520)//返回最大尺寸
                                .withOptions(options)
                                .start(GroupCreateActivity.this);

                    }
                })
                .show(getSupportFragmentManager(),GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //收到从Activity传递过来的回调，取出其中的值进行图片加载
        //如果是我能够处理的类型
        if (requestCode == UCrop.REQUEST_CROP) {
            //通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);

            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        }else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown);
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 加载Uri到当前
     * @param uri
     */
    private void loadPortrait(Uri uri){
        //得到头像地址
        mPortraitPath = uri.getPath();

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_create,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_create){
            //进行创建
            onCreateClick();
        }
        return super.onOptionsItemSelected(item);
    }

    //进行创建操作
    private void onCreateClick(){
        hideSoftKeyboard();
        String name = mName.getText().toString().trim();
        String desc = mDesc.getText().toString().trim();
        mPresenter.create(name,desc,mPortraitPath);
    }

    //隐藏软键盘
    private void hideSoftKeyboard(){
        //当前焦点的View
        View view = getCurrentFocus();
        if(view==null)
            return;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    protected GroupCreateContract.Presenter initPresenter() {
        return new GroupCreatePresenter(this);
    }

    @Override
    public void onCreateSucceed() {
        //提示成功
        hideLoading();
        Application.showToast(R.string.label_group_create_succeed);
        finish();
    }

    @Override
    public RecyclerAdapter<GroupCreateContract.ViewModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideDialogLoading();
    }

    private class Adapter extends RecyclerAdapter<GroupCreateContract.ViewModel>{
        @Override
        protected int getItemViewType(int position, GroupCreateContract.ViewModel viewModel) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<GroupCreateContract.ViewModel> onCreateViewHolder(View root, int viewType) {
            return new GroupCreateActivity.ViewHolder(root);
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCreateContract.ViewModel>{

        @BindView(R.id.iv_portrait)
        PortraitView mPortrait;

        @BindView(R.id.tv_username)
        TextView mUsername;

        @BindView(R.id.cb_select)
        CheckBox mSelect;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @OnCheckedChanged(R.id.cb_select)
        void onCheckedChanged(boolean checked){
            //进行状态更改
            mPresenter.changeSelect(mData,checked);
        }

        @Override
        protected void onBind(GroupCreateContract.ViewModel viewModel) {
            mPortrait.setup(Glide.with(GroupCreateActivity.this),viewModel.author);
            mUsername.setText(viewModel.author.getUsername());
            mSelect.setChecked(viewModel.isSelected);
        }
    }
}
