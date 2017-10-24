package com.wyj.myqq.utils;

/**
 * Created by wyj on 2016/7/1.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class ImageUtils extends Activity {

    public static Uri imageUriFromcamera;
    /**
     * 存放图片的url地址
     */
    public static Uri uri;

    /**
     * 显示获取照片不同法师的对话框
     */
    public static void showImagePickDialog(final Activity activity) {
        String[] items = new String[]{"拍照", "相册"};
        new AlertDialog.Builder(activity)
                .setTitle("选择获取图片的方式")
                .setItems(items, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        switch (arg1) {
                            case 0:
                                pickImageFromcamera(activity);
                                break;
                            case 1:
                                pickImageFromalbum(activity);
                                break;

                            default:
                                break;
                        }

                    }
                }).show();


    }

    //从相册中选取照片
    public static void pickImageFromalbum(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        activity.startActivityForResult(intent, Constant.REQUEST_CODE_FROM_ALBUM);

    }

    //打开相机拍照
    public static void pickImageFromcamera(final Activity activity) {

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//		imageUriFromcamera = createImageUri(activity);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromcamera);
//		intent.setType("image/*");

        activity.startActivityForResult(intent, Constant.REQUEST_CODE_FROM_CAMERA);

    }

    //保存拍照后的照片
    private static Uri createImageUri(Context context) {
        String name = "Image" + System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, name);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return uri;

    }

    public static void deleteImageUri(Context context, Uri uri) {
        context.getContentResolver().delete(imageUriFromcamera, null, null);
    }


    /**
     * 将content的uri数据格式转换为file的uri数据类型
     *
     * @return
     */

    public static Uri convertUri(Uri uri, Activity activity) {
        try {
            InputStream is = activity.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return saveBitmap(bitmap);
        } catch (FileNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            return null;
        }

    }

    public static Uri saveBitmap(Bitmap bm) {
        File file = new File(Environment.getExternalStorageDirectory() + "/com.wyj.qq");
        if (!file.exists()) {
            file.mkdir();
        }
        File img = new File(file.getAbsolutePath() + "qq.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 图片压缩，好像有点问题
     * @param uri
     * @param activity
     */
    public static void startImageZoom(Uri uri, Activity activity) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //实际宽高
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, Constant.CROP_REQUEST);
    }


    /**
     * 将bitmap转换为base64格式的字符串
     * @param bm
     * @return
     */
    public static String bitmapToString(Bitmap bm) {
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
            byte[] bytes = stream.toByteArray();
            String image = new String(Base64.encodeToString(bytes, Base64.DEFAULT));

            return image;
        }catch(Exception e){
            return "";
        }

    }

    /**
     * 将base64格式的image转换为bitmap
     * @param image
     * @return
     */
    public static Bitmap stringToBitmap(String image) {
        byte[] bitmapArray;
        bitmapArray = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        return bitmap;
    }

    /**
     * 给一个网络上的图片地址将其转换为bitmap格式的照片
     * @param imgUrl
     * @return
     */
    public static Bitmap receiveImage(String imgUrl) {
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(imgUrl);
            InputStream is = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
     * 计算位图的采样比例大小
     * @param options
     * @param imageView 控件(根据控件的大小进行压缩)
     * @return
     */
    private static int calculatInSampleSize(BitmapFactory.Options options, ImageView imageView) {
        //获取位图的原宽高
        final int w = options.outWidth;
        final int h = options.outHeight;

        if (imageView!=null){
            //获取控件的宽高
            final int reqWidth = imageView.getWidth();
            final int reqHeight = imageView.getHeight();

            //默认为一(就是不压缩)
            int inSampleSize = 1;
            //如果原图的宽高比需要的图片宽高大
            if (w > reqWidth || h > reqHeight) {
                if (w > h) {
                    inSampleSize = Math.round((float) h / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) w / (float) reqWidth);
                }
            }
            System.out.println("压缩比为:" + inSampleSize);
            return inSampleSize;

        }else {
            return 1;
        }
    }
    /**
     * 将Uri转换成Bitmap
     * @param context
     * @param uri
     * @param options
     * @return
     */
    public static Bitmap decodeBitmap(Context context, Uri uri, BitmapFactory.Options options) {
        Bitmap bitmap = null;

        if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            try {
                /**
                 * 将图片的Uri地址转换成一个输入流
                 */
                inputStream = cr.openInputStream(uri);

                /**
                 * 将输入流转换成Bitmap
                 */
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);

                assert inputStream != null;
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    /**
     * 对图片进行重新采样
     * @param context
     * @param uri 图片的Uri地址
     * @param imageView
     * @return
     */
    public static Bitmap compressBitmap(Context context, Uri uri, ImageView imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        decodeBitmap(context, uri, options);
        options = new BitmapFactory.Options();
        options.inSampleSize = calculatInSampleSize(options, imageView);
        Bitmap bitmap = null;

        try {
            bitmap = decodeBitmap(context, uri, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
