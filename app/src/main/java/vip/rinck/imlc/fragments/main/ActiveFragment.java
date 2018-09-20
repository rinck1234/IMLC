package vip.rinck.imlc.fragments.main;


import butterknife.BindView;
import vip.rinck.imlc.R;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.common.widget.GalleryView;


public class ActiveFragment extends Fragment {

    @BindView(R.id.galleryView)
    GalleryView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();

        mGalley.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
