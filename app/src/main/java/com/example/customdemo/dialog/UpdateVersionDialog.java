package com.example.customdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.customdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangliqiang on 2017/8/23.
 */

public class UpdateVersionDialog extends Dialog {

    TextView updateInfoText;

    Button updateBtn;

    TextView versionText;
    TextView tvNextTime;

    private boolean isForce;
    private String updateUrl;
    private Context context;

    public UpdateVersionDialog(@NonNull Context context) {
        super(context, R.style.Dialog_normal);
        this.context = context;
        initView(context);
    }

    public UpdateVersionDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        versionText = (TextView) findViewById(R.id.tv_app_version);
        updateInfoText = (TextView) findViewById(R.id.tv_update_info);
        updateBtn = (Button) findViewById(R.id.btn_update);
        tvNextTime = (TextView) findViewById(R.id.tv_nextTime);

        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        //window.setWindowAnimations(R.style.anim_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        params.width = (int) (p.x * 0.7f);

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUpdate();
            }
        });

        tvNextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextTimeUpdate();
            }
        });
    }

    public void setNewVersion(String version) {
        this.versionText.setText("V" + version);
    }

    public void setUpdateInfo(String content) {
        this.updateInfoText.setText(content);
    }

    public void setUpdateUrl(String url) {
        this.updateUrl = url;
    }

    public void isForce(boolean isForce) {
        this.isForce = isForce;
        setCancelable(false);
        if (isForce) {
//            setCancelable(false);
            tvNextTime.setVisibility(View.GONE);
        } else {
//            setCanceledOnTouchOutside(true);
            tvNextTime.setVisibility(View.VISIBLE);
        }
    }

    public void gotoUpdate() {
        DownloadDialog downloadDialog = new DownloadDialog(context);
        downloadDialog.isForce(isForce);
        downloadDialog.download(updateUrl);
        dismiss();
    }

    public void nextTimeUpdate() {
        dismiss();
    }

}
