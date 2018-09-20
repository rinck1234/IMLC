package vip.rinck.imlc.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import vip.rinck.imlc.R;
import vip.rinck.imlc.common.app.Activity;
import vip.rinck.imlc.common.app.Fragment;
import vip.rinck.imlc.fragments.account.UpdateInfoFragment;

public class AccountActivity extends Activity {

    private Fragment mCurFragment;

    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
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