package vip.rinck.imlc.activities;

import android.content.Intent;

import vip.rinck.imlc.R;
import vip.rinck.imlc.common.app.Activity;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.fragments.user.UpdateInfoFragment;

public class UserActivity extends Activity {
    private Fragment mCurFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode, resultCode, data);

    }
}
