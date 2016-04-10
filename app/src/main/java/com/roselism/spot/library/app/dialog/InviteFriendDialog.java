package com.roselism.spot.library.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.roselism.spot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hero2 on 2016/2/19.
 */
public class InviteFriendDialog extends SimpleInputDialog implements View.OnFocusChangeListener {

    @Bind(R.id.friendEmail_editText) EditText friendEmailEditText;

//    private String mTitle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = buildDialog();
        initListener(); // 初始化监听器
        friendEmailEditText.requestFocus(); // 获取焦点

        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected Dialog buildDialog() {

        // 构建dialog的内容
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_invite_people, null);
        ButterKnife.bind(this, view);

        // 创建dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InviteFriendDialog.this.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InviteFriendDialog.this.dismiss();
                OnInputFinishedListener listener = (OnInputFinishedListener) getActivity();
                listener.onInputFinished(friendEmailEditText);  // 回调Acitivity的输入完成监听器
            }
        });
        builder.setView(view).setTitle("邀请好友");
        return builder.create();
    }

    private void initListener() {
        friendEmailEditText.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.i("TAG", "onFocusChange: -->");
        View.OnFocusChangeListener listener = (View.OnFocusChangeListener) getActivity();
        listener.onFocusChange(v, hasFocus);
    }
}