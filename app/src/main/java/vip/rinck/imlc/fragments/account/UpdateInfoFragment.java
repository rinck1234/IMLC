package vip.rinck.imlc.fragments.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import vip.rinck.imlc.R;
import vip.rinck.imlc.common.app.Application;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.common.widget.PortraitView;
import vip.rinck.imlc.fragments.media.GalleryFragment;



/**
 * 用户更新信息界面
 */
public class UpdateInfoFragment extends Fragment {
    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;


    public UpdateInfoFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.iv_portrait)
    void onPortraitClick(){
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        //设置图片处理的格式
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置压缩后的图片精度
                        options.setCompressionQuality(96);

                        File dPath = Application.getPortraitTmpFile();
                        UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(dPath))
                                .withAspectRatio(1,1)
                                .withMaxResultSize(520,520)//返回最大尺寸
                                .withOptions(options)
                                .start(getActivity());

                    }
                })
                // show的时候建议使用getChildFragmentManager
                .show(getChildFragmentManager(),GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        }else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void loadPortrait(Uri uri){
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);
    }



}
