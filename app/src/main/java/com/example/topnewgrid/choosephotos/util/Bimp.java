package com.example.topnewgrid.choosephotos.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.topnewgrid.util.GraphicsBitmapUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Bimp {
    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//�ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���
            baos.reset();//����baos�����baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//����ѹ��50%����ѹ��������ݴ�ŵ�baos��
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
        float hh = 800f;//�������ø߶�Ϊ800f
        float ww = 480f;//�������ÿ��Ϊ480f
        //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
        int be = 1;//be=1��ʾ������
        if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//�������ű���
        //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
        isBm = new ByteArrayInputStream(baos.toByteArray());

        return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��
    }
    public static Bitmap getimage(String srcPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//��ʱ����bmΪ��

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
        float hh = 800f;//�������ø߶�Ϊ800f
        float ww = 480f;//�������ÿ��Ϊ480f
        //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
        int be = 1;//be=1��ʾ������
        if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//�������ű���
        //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        int degree = GraphicsBitmapUtils.readPictureDegree(srcPath);
        bitmap=GraphicsBitmapUtils.rotaingImageView(degree, bitmap);
        //return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��
        return bitmap;
    }
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
        int options = 80;
        while ( baos.toByteArray().length / 1024>100) {  //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
            baos.reset();//����baos�����baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��
            options -= 10;//ÿ�ζ�����10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ
        return bitmap;
    }
    public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 800) && (options.outHeight >> i <= 400)) {
				in = new BufferedInputStream(new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
        int degree = GraphicsBitmapUtils.readPictureDegree(path);
        bitmap=GraphicsBitmapUtils.rotaingImageView(degree, bitmap);
        return bitmap;
	}
}
