package com.student.aynu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.student.aynu.R;
import com.student.aynu.base.BaseActivity;
import com.student.aynu.entity.Base_entity;
import com.student.aynu.fragment.FindFragment;
import com.student.aynu.fragment.ForumFragment;
import com.student.aynu.fragment.HomeFragment;
import com.student.aynu.fragment.InfoFragment;
import com.student.aynu.fragment.MineFragment;
import com.student.aynu.nohttp.HttpListener;
import com.student.aynu.util.IpUtil;
import com.student.aynu.util.Sha1Util;
import com.student.aynu.util.ToastUtil;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    //首页五个fragment
    private Fragment mTab1, mTab2, mTab3, mTab4, mTab5;
    //首页五个选择项的内容
    @BindView(R.id.footer_1_text)
    TextView mHomeText;
    @BindView(R.id.footer_2_text)
    TextView mForumText;
    @BindView(R.id.footer_3_text)
    TextView mFindText;
    @BindView(R.id.footer_4_text)
    TextView mInfoText;
    @BindView(R.id.footer_5_text)
    TextView mMineText;
    //首页五个选择项的图片
    @BindView(R.id.footer_1_image)
    ImageView mHomeImage;
    @BindView(R.id.footer_2_image)
    ImageView mForumImage;
    @BindView(R.id.footer_3_image)
    ImageView mFindImage;
    @BindView(R.id.footer_4_image)
    ImageView mInfoImage;
    @BindView(R.id.footer_5_image)
    ImageView mMineImage;
    //常量
    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSelect(1);
    }

    @OnClick({R.id.footer_1_linear, R.id.footer_2_linear, R.id.footer_3_linear, R.id.footer_4_linear, R.id.footer_5_linear})
    public void onFooterClick(View v) {
        resetTextImg();
        switch (v.getId()) {
            case R.id.footer_1_linear:
                setSelect(1);
                break;
            case R.id.footer_2_linear:
                setSelect(2);
                break;
            case R.id.footer_3_linear:
                setSelect(3);
                break;
            case R.id.footer_4_linear:
                setSelect(4);
                break;
            case R.id.footer_5_linear:
                setSelect(5);
                break;
        }
    }

    //点击事件处理
    public void setSelect(int select) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (select) {
            case 1:
                if (mTab1 == null) {
                    mTab1 = new HomeFragment();
                    fragmentTransaction.add(R.id.main_content, mTab1);
                } else {
                    fragmentTransaction.show(mTab1);
                }
                mHomeImage.setImageResource(R.mipmap.foot_1_hover);
                mHomeText.setTextColor(Color.rgb(153, 0, 0));
                break;
            case 2:
                if (mTab2 == null) {
                    mTab2 = new ForumFragment();
                    fragmentTransaction.add(R.id.main_content, mTab2);
                } else {
                    fragmentTransaction.show(mTab2);
                }
                mForumImage.setImageResource(R.mipmap.foot_2_hover);
                mForumText.setTextColor(Color.rgb(153, 0, 0));
                break;
            case 3:
                if (mTab3 == null) {
                    mTab3 = new FindFragment();
                    fragmentTransaction.add(R.id.main_content, mTab3);
                } else {
                    fragmentTransaction.show(mTab3);
                }
                mFindImage.setImageResource(R.mipmap.foot_3_hover);
                mFindText.setTextColor(Color.rgb(153, 0, 0));
                break;
            case 4:
                if (mTab4 == null) {
                    mTab4 = new InfoFragment();
                    fragmentTransaction.add(R.id.main_content, mTab4);
                } else {
                    fragmentTransaction.show(mTab4);
                }
                mInfoImage.setImageResource(R.mipmap.foot_4_hover);
                mInfoText.setTextColor(Color.rgb(153, 0, 0));
                break;
            case 5:
                //进入个人中心前，检测token是否过期。
                if (!checkIsLogin(MainActivity.this)) {
                    //没有登录
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("flag", 5);
                    startActivityForResult(intent, REQUEST_CODE);
                    ToastUtil.showFaliureToast(MainActivity.this, "请重新登录");
                } else {
                    //没有过期
                    if (mTab5 == null) {
                        mTab5 = new MineFragment();
                        fragmentTransaction.add(R.id.main_content, mTab5);
                    } else {
                        fragmentTransaction.show(mTab5);
                    }
                    mMineImage.setImageResource(R.mipmap.foot_5_hover);
                    mMineText.setTextColor(Color.rgb(153, 0, 0));
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mTab1 != null) {
            fragmentTransaction.hide(mTab1);
        }
        if (mTab2 != null) {
            fragmentTransaction.hide(mTab2);
        }
        if (mTab3 != null) {
            fragmentTransaction.hide(mTab3);
        }
        if (mTab4 != null) {
            fragmentTransaction.hide(mTab4);
        }
        if (mTab5 != null) {
            fragmentTransaction.hide(mTab5);
        }
    }

    //图片文字初始化
    public void resetTextImg() {
        mHomeImage.setImageResource(R.mipmap.foot_1);
        mForumImage.setImageResource(R.mipmap.foot_2);
        mFindImage.setImageResource(R.mipmap.foot_3);
        mInfoImage.setImageResource(R.mipmap.foot_4);
        mMineImage.setImageResource(R.mipmap.foot_5);

        mHomeText.setTextColor(Color.rgb(153, 153, 153));
        mForumText.setTextColor(Color.rgb(153, 153, 153));
        mFindText.setTextColor(Color.rgb(153, 153, 153));
        mInfoText.setTextColor(Color.rgb(153, 153, 153));
        mMineText.setTextColor(Color.rgb(153, 153, 153));
    }

    //退出时间
    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Log.d("tag", "-----");
            if (resultCode == RESULT_OK && data != null) {
                Log.d("tag", "++++++++");
                //登陆成功
                int select = data.getIntExtra("select", 1);
                Log.d("tag", select + "");
                setSelect(select);
            }
        }
    }
}
