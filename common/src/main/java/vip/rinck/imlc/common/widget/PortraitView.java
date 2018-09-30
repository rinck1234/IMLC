package vip.rinck.imlc.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;

import de.hdodenhof.circleimageview.CircleImageView;
import vip.rinck.imlc.common.R;
import vip.rinck.imlc.factory.model.Author;

public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager,int resourceId,String url){
        if(url==null)
            url="";
        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()//CircleImageView中显示动画，会导致显示延迟
                .into(this);
    }

    public void setup(RequestManager manager,String url){
        setup(manager, R.drawable.default_portrait,url);
    }

    public void setup(RequestManager manager,Author author){
        if(author==null){
            return;
        }
        setup(manager, author.getPortrait());
    }
}
