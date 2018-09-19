package vip.rinck.imlc.common.widget.recycler;

import android.support.v7.widget.RecyclerView;

public interface AdapterCallback<Data> {

    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
