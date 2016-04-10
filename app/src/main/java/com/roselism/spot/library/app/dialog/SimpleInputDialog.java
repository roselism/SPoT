package com.roselism.spot.library.app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hero2 on 2016/2/20.
 */
public abstract class SimpleInputDialog extends android.app.DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void show(android.app.FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    /**
     * 当输入完成时回调
     */
    public interface OnInputFinishedListener {


        /**
         * 当输入完成时回调
         *
         * @param view 内容输入的View （如EditView）
         */
        void onInputFinished(View view);
    }

    /**
     * 建造一个dialog
     *
     * @return
     */
    protected abstract Dialog buildDialog();

}




