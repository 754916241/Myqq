package com.wyj.myqq.utils;

/**
 * Created by wyj on 2016/7/1.
 */

import java.net.URL;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import android.app.Activity;
import android.app.AlertDialog;
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
    protected static void pickImageFromalbum(Activity activity) {
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
            return ImageUtils.saveBitmap(bitmap);
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

    public static void imageZoom(Uri uri, Activity activity) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, Constant.CROP_REQUEST);
    }

    public static String sendImage(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
        byte[] bytes = stream.toByteArray();
        String image = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        return image;

    }


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

    public static Bitmap stringtoBitmap(String image) {
        byte[] bitmapArray;
        bitmapArray = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        return bitmap;
    }
}
