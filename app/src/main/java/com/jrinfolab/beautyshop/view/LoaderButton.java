package com.jrinfolab.beautyshop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrinfolab.beautyshop.R;

public class LoaderButton extends RelativeLayout {

    Context mContext;

    ProgressBar progressBar;
    TextView buttonText, loaderText;
    RelativeLayout loaderLayout;

    public void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_loader, this, true);
        progressBar = view.findViewById(R.id.progress);
        buttonText = view.findViewById(R.id.button_text);
        loaderText = view.findViewById(R.id.loading_text);
        loaderLayout = view.findViewById(R.id.layout_loader);
    }

    public LoaderButton(Context context) {
        super(context);
        initView(context);
    }

    public LoaderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoaderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public LoaderButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void setText(String buttonString, String loaderString, boolean showLoader) {
        buttonText.setText(buttonString);
        loaderText.setText(loaderString);
        showLoader(showLoader);
    }

    public void showLoader(boolean showLoader) {
        if (showLoader) {
            buttonText.setVisibility(View.GONE);
            loaderLayout.setVisibility(View.VISIBLE);
        } else {
            loaderLayout.setVisibility(View.GONE);
            buttonText.setVisibility(View.VISIBLE);
        }
    }
}
