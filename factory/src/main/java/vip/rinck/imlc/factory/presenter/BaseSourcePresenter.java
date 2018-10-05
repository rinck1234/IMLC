package vip.rinck.imlc.factory.presenter;

import java.util.List;

import vip.rinck.imlc.factory.data.DataSource;
import vip.rinck.imlc.factory.data.DbDataSource;

/**
 * 基础的仓库源的Presenter定义
 */
public abstract class BaseSourcePresenter<Data,ViewModel,
        Source extends DbDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel,View>
        implements DataSource.SucceedCallback<List<Data>>{

    protected Source mSource;
    public BaseSourcePresenter(Source source,View view) {
        super(view);
        this.mSource=source;
    }

    @Override
    public void start() {
        super.start();
        if(mSource!=null)
        mSource.load(this);
    }

    @Override
    public void destory() {
        super.destory();
        mSource.dispose();
        mSource = null;
    }


}
