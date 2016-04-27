package com.roselism.spot.library.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.roselism.spot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hero2 on 2016/2/1.
 */
public class FolderNameDialog extends DialogFragment {

    @Bind(R.id.et_folder_name) EditText folderNameEditView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_folder, null);
        ButterKnife.bind(this, view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setPositiveButton(R.string.btn_create_folder, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 回掉函数？
                FolderNameInputListener inputListener = (FolderNameInputListener) getActivity();
                inputListener.onInputFinished(folderNameEditView.getText().toString());
            }
        }).setNegativeButton(R.string.btn_canle, null);
        builder.setTitle(R.string.folder_dialog_title);

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface FolderNameInputListener {
        void onInputFinished(String name);
    }

}