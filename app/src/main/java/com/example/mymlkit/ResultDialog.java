package com.example.mymlkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

public class ResultDialog extends DialogFragment {

    AppCompatButton btnClick;
    TextView tvRes;

    public View onCreateView(LayoutInflater inflater, ViewGroup container , Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_resultdialog,container,false);

        tvRes = view.findViewById(R.id.tvLCOFace);
        btnClick = view.findViewById(R.id.btnClick);

        Bundle bundle = getArguments();

        String resultText = "";

        resultText = bundle.getString(LCOFaceDetection.resultTxt);

        tvRes.setText(resultText);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;




    }

}
