package com.roselism.spot.library.app.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.roselism.spot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 两个进度条的对话框
 * <p/>
 * Created by hero2 on 2016/2/1.
 */
public class DetailProgressDialog extends DialogFragment implements View.OnClickListener {

    @Bind(R.id.secondary_progress_text) TextView mSecondaryProgressText;
    @Bind(R.id.secondary_progress) ProgressBar mSecondaryProgress;
    @Bind(R.id.main_progress) ProgressBar mMainProgress;
    @Bind(R.id.main_progress_text) TextView mMainProgressText;
    @Bind(R.id.confirm_button) Button mConfirmButton;
    @Bind(R.id.cancle_button) Button mCancleButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_double_progress, container);
        ButterKnife.bind(this, view);
        this.setStyle(STYLE_NO_TITLE, 0);
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 设置次进度条的最大值
     *
     * @param value
     */
    public void setSecondaryProgressMax(int value) {
        mSecondaryProgress.setMax(value);
    }

    /**
     * 设置次进度条的进度(本例子当中为上面的那个进度条)
     *
     * @param value
     */
    public void setSecondaryProgress(int value) {
        mSecondaryProgress.setProgress(value);
    }

    /**
     * 设置主进度条的最大值
     *
     * @param value
     */
    public void setMainProgressMax(int value) {
        mMainProgress.setMax(value);
    }

    /**
     * 设置主进度条的进度
     *
     * @param value
     */
    public void setMainProgress(int value) {
        mMainProgress.setProgress(value);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_button:
                this.dismiss(); // 隐藏该对话框
                break;

            case R.id.cancle_button:
                break;
        }
    }
}