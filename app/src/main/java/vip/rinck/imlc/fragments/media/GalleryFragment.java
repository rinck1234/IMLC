package vip.rinck.imlc.fragments.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import net.qiujuer.genius.ui.Ui;

import vip.rinck.imlc.R;
import vip.rinck.imlc.common.tools.UiTool;
import vip.rinck.imlc.common.widget.GalleryView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangeListener{

    private GalleryView mGallery;
    private OnSelectedListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //先使用默认的
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(),this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if(count>0){
            //隐藏自己
            dismiss();
            if(mListener!=null){
                //得到所有选中的图片的路径
                String[] paths = mGallery.getSelectedPath();
                //返回第一张
                mListener.onSelectedImage(paths[0]);
                //取消和唤起者之间的应用，加快内存回收
                mListener = null;
            }
        }
    }

    /**
     * 设置事件监听并返回自己
     * @param listener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener){
        mListener = listener;
        return this;
    }


    /**
     * 选中图片的监听器
     */
    public interface  OnSelectedListener{
        void onSelectedImage(String path);
    }

    /**
     * 解决顶部状态栏变黑
     */
    public static class TransStatusBottomSheetDialog extends BottomSheetDialog{
        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window window = getWindow();
            if(window==null)
                return;
            //得到屏幕高度
            int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
            //得到状态栏高度
            int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());

            //计算dialog的高度
            int dialogHeight = screenHeight-statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight<=0?ViewGroup.LayoutParams.MATCH_PARENT:dialogHeight);
        }
    }
}
