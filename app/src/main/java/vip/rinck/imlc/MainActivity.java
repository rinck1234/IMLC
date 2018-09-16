package vip.rinck.imlc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import vip.rinck.imlc.common.app.Activity;

public class MainActivity extends Activity {

    @BindView(R.id.tv_test)
    TextView mTestText;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTestText.setText("Test Hello.");
    }
}
