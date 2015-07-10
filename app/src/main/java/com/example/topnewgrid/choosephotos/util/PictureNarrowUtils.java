package com.example.topnewgrid.choosephotos.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureNarrowUtils {
	public static String SDPATH = Environment.getExternalStorageDirectory() + "/formats/";
	public static void saveBitmap(Bitmap bm, String picName) {
		Log.e("", "保存图片");
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
            System.out.println("savepatn"+SDPATH+picName+".JPEG");
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 70, out);
			out.flush();
			out.close();
			Log.e("", "图片已保存");
		} catch (FileNotFoundException e) {
            System.out.println("保存失败");
            e.printStackTrace();
		} catch (IOException e) {
            System.out.println("保存失败");
			e.printStackTrace();
		}
	}
    public static void saveBitmap2(Bitmap bm, String picName) {
        Log.e("", "保存图片");
        try {
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            System.out.println("savepatn"+SDPATH+picName+".JPEG");
            File f = new File(SDPATH, picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
            Log.e("", "图片已保存");
        } catch (FileNotFoundException e) {
            System.out.println("保存失败");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("保存失败");
            e.printStackTrace();
        }
    }

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 鍒犻櫎鎵?湁鏂囦欢
			else if (file.isDirectory())
				deleteDir(); // 閫掕鐨勬柟寮忓垹闄ゆ枃浠跺す
		}
		dir.delete();// 鍒犻櫎鐩綍鏈韩
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

}
