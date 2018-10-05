package vip.rinck.imlc.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;

import vip.rinck.imlc.factory.utils.DiffUiDataCallback;

/**
 * App中的一个基础BaseDbModel
 * DbFlow中的基础类
 * 同时定义需要的方法
 */
public abstract class BaseDbModel<Model> extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model> {
}
