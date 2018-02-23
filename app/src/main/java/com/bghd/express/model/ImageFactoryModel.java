package com.bghd.express.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.bghd.express.R;

import com.bghd.express.core.Constance;
import com.bghd.express.entiy.TellEntity;
import com.bghd.express.utils.base.BaseViewModel;

import com.bghd.express.utils.tools.ToolUtil;
import com.cazaea.sweetalert.SweetAlertDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by lixu on 2018/1/23.
 * @author lixu
 */

public class ImageFactoryModel{


    private String mHeadBase64 = "data:image/png;base64,";


    private Context mContext;
    private List<String> myList = new ArrayList<>();

    public ImageFactoryModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     *
     * @param imgList   寄件
     * @param imgList2  收件
     */
    public void compressImg(final List<String> imgList,final List<String> imgList2) {
        showProgressDialog(mContext, "正在处理图片...");

        if(!ToolUtil.isEmpty(imgList) && !ToolUtil.isEmpty(imgList2)){
            Observable.create(new ObservableOnSubscribe<List<String>>() {

                @Override
                public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                    List<String> imgListTest = new ArrayList<>();
                    for (String imgUrl : imgList) {
                        imgListTest.add(mHeadBase64+bitmapToString(imgUrl));
                    }
                    emitter.onNext(imgListTest);
                   // emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<String> strings) {
                            myList.clear();
                            myList.addAll(strings);
                            Observable.create(new ObservableOnSubscribe<List<String>>() {

                                @Override
                                public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                                    List<String> imgListTest = new ArrayList<>();
                                    for (String imgUrl : imgList2) {
                                        imgListTest.add(mHeadBase64+bitmapToString(imgUrl));
                                    }
                                    emitter.onNext(imgListTest);
                                    emitter.onComplete();
                                }
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<List<String>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(List<String> strings) {
                                            onCallBackListener.onSuccess(myList,strings,2);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            dismissProgressDialog();
                                            onCallBackListener.onErro(Constance.getMsgByException(e));
                                        }

                                        @Override
                                        public void onComplete() {
                                            dismissProgressDialog();
                                        }
                                    });

                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissProgressDialog();
                            onCallBackListener.onErro(Constance.getMsgByException(e));
                        }

                        @Override
                        public void onComplete() {
                        }
                    });






        }else if(!ToolUtil.isEmpty(imgList)){
            Observable.create(new ObservableOnSubscribe<List<String>>() {

                @Override
                public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                    List<String> imgListTest = new ArrayList<>();
                    for (String imgUrl : imgList) {
                        imgListTest.add(mHeadBase64+bitmapToString(imgUrl));
                    }
                    emitter.onNext(imgListTest);
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<String> strings) {
                            onCallBackListener.onSuccess(strings,null,0);
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissProgressDialog();
                            onCallBackListener.onErro(Constance.getMsgByException(e));
                        }

                        @Override
                        public void onComplete() {
                            dismissProgressDialog();
                        }
                    });


        }else if(!ToolUtil.isEmpty(imgList2)){
            Observable.create(new ObservableOnSubscribe<List<String>>() {

                @Override
                public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                    List<String> imgListTest = new ArrayList<>();
                    for (String imgUrl : imgList2) {
                        imgListTest.add(mHeadBase64+bitmapToString(imgUrl));
                    }
                    emitter.onNext(imgListTest);
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<String> strings) {
                            onCallBackListener.onSuccess(null,strings,1);
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissProgressDialog();
                            onCallBackListener.onErro(Constance.getMsgByException(e));
                        }

                        @Override
                        public void onComplete() {
                            dismissProgressDialog();
                        }
                    });

        }

    }


    private OnErroListener onCallBackListener;

    public void setOnChangeCallback(OnErroListener onItemClickListener) {
        this.onCallBackListener = onItemClickListener;
    }


    public interface OnErroListener {
        //type 0:寄件  1：收件  2:all
        void onSuccess(List<String> imgList,List<String> imgList2,int type);
        void onErro(String erroMsg);
    }


    private SweetAlertDialog pDialog;

    public void showProgressDialog(Context mContext, String title) {
        try {
            pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.colorPrimary);
            pDialog.setTitleText(title);
            pDialog.setCancelable(false);
            pDialog.show();
        } catch (Exception e) {
        }
    }

    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }


    /**
     * 转为base64
     *
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }



}
