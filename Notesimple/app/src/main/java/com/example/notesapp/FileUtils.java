package com.example.notesapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;

import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 30415
 */
public class FileUtils {
    public static void deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public static String fileSaveToInside(Context context, String fileName, Bitmap bitmap) {
        FileOutputStream fos = null;
        String path = null;
        try {
            File folder = context.getFilesDir();
            //判断目录是否存在
            //目录不存在时自动创建
            if (folder.exists() || folder.mkdir()) {
                File file = new File(folder, fileName);
                fos = new FileOutputStream(file);
                //写入文件
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                path = file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    //关闭流
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //返回路径
        return path;
    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String filePath;
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    public static Bitmap toTurn(Bitmap img, int deg) {
        Matrix matrix = new Matrix();
        matrix.postRotate(deg);
        int width = img.getWidth();
        int height = img.getHeight();
        return Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
