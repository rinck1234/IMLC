package vip.rinck.imlc.fragments.panel;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;

import butterknife.BindView;
import vip.rinck.imlc.R;
import vip.rinck.imlc.common.widget.recycler.RecyclerAdapter;
import vip.rinck.imlc.face.Face;

public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {

    @BindView(R.id.iv_face)
    ImageView mFace;

    public FaceHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null &&
                //drawable资源Id
                ((bean.preview instanceof Integer)
                        //face zip包资源路径
                        || bean.preview instanceof String)) {
            Glide.with(mFace.getContext())
                    .load(bean.preview)
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)//设置解码格式，保证清晰度
                    .into(mFace);
        }
    }
}
