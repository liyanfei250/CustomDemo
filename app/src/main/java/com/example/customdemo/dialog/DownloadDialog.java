package com.example.customdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.FileProvider;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.customdemo.R;
import com.example.customdemo.utils.DownloadUtil;
import com.example.customdemo.utils.FilePath;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangliqiang on 2017/8/28.
 */

public class DownloadDialog extends Dialog {

//    @BindView(R.id.img_dialog_close)
//    ImageView closeImage;
//
//    @BindView(R.id.invisible_view)
//    ImageView invisibleView;

    TextView tx_progress_info;

    ProgressBar progressBar;

    LinearLayout btn_download;

    TextView tv_download_status;

    TextView btn_install;

    private Button btnProblem;

    private boolean isForce;
    private Context mContext;
    private long id;
    private String mFileUrl;
    private String updateUrl;

    public DownloadDialog(@NonNull Context context) {
        super(context, R.style.Dialog_normal);
        this.mContext = context;
        initView(context);
    }

    public DownloadDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_download, null);
        setContentView(view);
        ButterKnife.bind(this, view);


        tv_download_status = (TextView) findViewById(R.id.tv_download_status);
        tx_progress_info = (TextView) findViewById(R.id.tx_progress_info);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        btn_download = (LinearLayout) findViewById(R.id.btn_download);
        btn_install = (Button) findViewById(R.id.btn_install);
        btnProblem = (Button) findViewById(R.id.btn_problem);

        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        params.width = (int) (p.x * 0.7f);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        btnProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoWebDownload();

            }
        });
        btn_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoInstall();

            }
        });
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        DownloadUtil.getInstance().cancleDownload(id);
        dismiss();
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
        DownloadUtil.getInstance().cancleDownload(id);
        dismiss();
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        DownloadUtil.getInstance().cancleDownload(id);
        dismiss();
        super.setOnDismissListener(listener);
    }

//    @OnClick(R.id.img_dialog_close)
//    public void closeDialog() {
//        DownloadUtil.getInstance().cancleDownload(id);
//        dismiss();
//    }

    public void isForce(boolean isForce) {
        this.isForce = isForce;
        if (isForce) {
            setCancelable(false);
        } else {
            setCanceledOnTouchOutside(true);
        }
    }

    public void gotoWebDownload() {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(updateUrl);
            intent.setData(content_url);
            mContext.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public void gotoInstall() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".fileProvider", new File(mFileUrl));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(mFileUrl)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }

    public void download(String updateUrl) {
        this.updateUrl = updateUrl;
        show();
        id = DownloadUtil.getInstance().download(updateUrl, FilePath.getAppRootPath(), FilePath.appName,
                new DownloadUtil.DownloadCallBack() {
                    @Override
                    public void complete(String fileUrl) {
                        tv_download_status.setText("下载成功");
                        mFileUrl = fileUrl;
                        btn_download.setVisibility(View.VISIBLE);
                        btn_install.setVisibility(View.VISIBLE);
                        gotoInstall();
                    }

                    @Override
                    public void downloadProgress(int progress, String fileTotalLen, String fileCountLen) {
                        tv_download_status.setText("正在下载中");
                        btn_download.setVisibility(View.GONE);
                        progressBar.setProgress(progress);
                        tx_progress_info.setText(fileCountLen + "/" + fileTotalLen);
                    }

                    @Override
                    public void failure() {
                        tv_download_status.setText("下载失败");
                        btn_download.setVisibility(View.VISIBLE);
                        btn_install.setVisibility(View.GONE);
                    }
                });
    }

}
