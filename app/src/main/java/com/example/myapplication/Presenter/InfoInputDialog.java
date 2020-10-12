package com.example.myapplication.Presenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myapplication.R;

public class InfoInputDialog extends AppCompatDialogFragment {
    private EditText name, info;
    private InfoInputDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);
        name = view.findViewById(R.id.editTextTextphotoName);
        info = view.findViewById(R.id.editTextTextInfo);

        builder.setView(view)
        .setTitle("Information")
        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String photoname = name.getText().toString();
                String descr = info.getText().toString();
                listener.applyText(photoname,descr);

            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (InfoInputDialogListener) context;

    }

    public interface InfoInputDialogListener{
        void applyText(String name, String info);
    }
}
