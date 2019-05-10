package com.example.customdemo.utils;


import com.example.customdemo.bean.DownloadResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huangliqiang on 2017/8/28.
 */

public class DownloadUtil {

    private OkHttpClient okHttpClient;

    private HashMap<Long, Boolean> cancleDownloadMap = new HashMap<>();

    private static DownloadUtil instance;

    public static DownloadUtil getInstance(){
        if(instance == null){
            instance = new DownloadUtil();
        }
        return instance;
    }

    private DownloadUtil(){
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        String host = url.host();
                        cookieStore.put(host, cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
    }

    public void cancleDownload(long downloadID){
        cancleDownloadMap.put(downloadID, true);
    }

    public long download(final String url, final String destFileDir, final String fiileName, final DownloadCallBack callBack){
        final long id = System.currentTimeMillis();
        cancleDownloadMap.put(id, false);
         Observable.create(new ObservableOnSubscribe<DownloadResult>() {
            @Override
            public void subscribe(final ObservableEmitter<DownloadResult> emitter) {
                final Request request = new Request.Builder().url(url).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        DownloadResult downloadEntity = new DownloadResult();
                        try {
                            is = response.body().byteStream();
                            long contentLength = response.body().contentLength();
                            File file = new File(destFileDir, fiileName);
                            fos = new FileOutputStream(file);
                            int countLength = 0;
                            downloadEntity.setFileUrl(destFileDir+"/"+fiileName);
                            downloadEntity.setFileTotalLen(contentLength/(1024*1024)+"MB");
                            while ((len = is.read(buf)) != -1)
                            {
                                if(cancleDownloadMap.get(id)){
                                    emitter.onError(new Exception());
                                    return;
                                }
                                countLength = len + countLength;
                                float progress = (float)countLength / (float)contentLength;
                                downloadEntity.setCurrentCountLen(countLength/(1024*1024)+"MB");
                                downloadEntity.setProgress((int) (progress*100));
                                emitter.onNext(downloadEntity);
                                fos.write(buf, 0, len);

                            }
                            fos.flush();
                            if (is != null) is.close();
                            if (fos != null) fos.close();
                            if(contentLength == countLength){
                                emitter.onComplete();
                            }else {
                                emitter.onError(new Exception());
                            }
                        } catch (IOException e){
                            emitter.onError(e);
                        }
                    }
                });
            }
        })
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(new Observer<DownloadResult>() {
             @Override
             public void onSubscribe(Disposable d) {

             }

             @Override
             public void onNext(DownloadResult value) {
                if(callBack != null){
                    callBack.downloadProgress(value.getProgress(), value.getFileTotalLen(), value.getCurrentCountLen());
                }
             }

             @Override
             public void onError(Throwable e) {
                if(callBack != null){
                    callBack.failure();
                    cancleDownloadMap.remove(id);
                }
             }

             @Override
             public void onComplete() {
                if(callBack != null){
                    callBack.complete(destFileDir+"/"+fiileName);
                    cancleDownloadMap.remove(id);
                }
             }
         });
        return id;
    }

    public interface DownloadCallBack{
        void complete(String fileUrl);

        void downloadProgress(int progress, String fileTotalLen, String fileCountLen);

        void failure();
    }

}
