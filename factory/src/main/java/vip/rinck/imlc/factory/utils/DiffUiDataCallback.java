package vip.rinck.imlc.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {
    private List<T> mOldList,mNewList;

    public DiffUiDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        //旧的数据大小
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        //新的数据大小
        return mNewList.size();
    }

    //两条记录是否为同一项
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);

        return beanNew.isSame(beanOld);
    }

    //相同项中不同内容
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }

    //进行比较的数据类型
    public interface UiDataDiffer<T>{
        //传递一个旧的数据，判断是否为同一个数据
        boolean isSame(T old);
        //和旧的数据对比，内容是否相同
        boolean isUiContentSame(T old);
    }
}
