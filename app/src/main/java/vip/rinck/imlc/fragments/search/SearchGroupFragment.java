package vip.rinck.imlc.fragments.search;


import vip.rinck.imlc.R;
import vip.rinck.imlc.activities.SearchActivity;
import vip.rinck.imlc.common.app.Fragment;

/**
 * 搜索群的界面实现
 */
public class SearchGroupFragment extends Fragment
implements SearchActivity.SearchFragment{


    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}
