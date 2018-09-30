package vip.rinck.imlc.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import vip.rinck.imlc.R;
import vip.rinck.imlc.common.app.Activity;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.common.app.ToolbarActivity;
import vip.rinck.imlc.fragments.search.SearchGroupFragment;
import vip.rinck.imlc.fragments.search.SearchUserFragment;

public class SearchActivity extends ToolbarActivity {

    private static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_USER = 1;//搜索人
    public static final int TYPE_GROUP = 2;//搜索群

    //具体需要显示的类型
    private int type;

    private SearchFragment mSearchFragment;

    /**
     * 显示搜索界面
     * @param context
     * @param type
     */
    public static void show(Context context,int type){
        Intent intent = new Intent(context,SearchActivity.class);
        intent.putExtra(EXTRA_TYPE,type);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        type = bundle.getInt(EXTRA_TYPE);
        return type==TYPE_USER||type==TYPE_GROUP;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //显示对应的Fragment
        Fragment fragment;
        if(type==TYPE_USER){
            SearchUserFragment searchUserFragment = new SearchUserFragment();
            fragment = searchUserFragment;
            mSearchFragment = searchUserFragment;
        }else {
            SearchGroupFragment searchGroupFragment = new SearchGroupFragment();
            fragment = searchGroupFragment;
            mSearchFragment = searchGroupFragment;
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container,fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //初始化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search,menu);

        //找到搜索菜单
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        if(searchView!=null){
            //得到搜索管理器
            SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));;
            //添加搜索监听
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //点击了提交按钮时
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //当文字改变的时候
                    if(TextUtils.isEmpty(newText)){
                        search("");
                        return true;
                    }
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);

    }

    //搜索的发起点
    private void search(String query){
        if(mSearchFragment==null){
            return;
        }
        mSearchFragment.search(query);
    }

    public interface SearchFragment{
        void search(String content);
    }
}
