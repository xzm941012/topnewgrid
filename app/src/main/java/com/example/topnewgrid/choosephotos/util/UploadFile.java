package com.example.topnewgrid.choosephotos.util;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import http.PostUrl;

public class UploadFile {
	public static String uploadUrl="http://"+ PostUrl.Media+":8080/Server/android/uploadImage.jsp?";
    public static String uploadVideoUrl="http://"+ PostUrl.Media+":8080/Server/android/uploadVideo.jsp?";
	public static String s;
	public UploadFile(String uploadUrl) {
		super();
		this.uploadUrl = uploadUrl;
	}
	public String defaultUploadMethod(byte[] data, String filename) throws IOException {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		// System.out.println(actionUrl.toString());
		HttpURLConnection con = null;
		URL url;
		try {
			url = new URL(uploadUrl);
			con = (HttpURLConnection) url.openConnection();
			/* 鍏佽Input銆丱utput锛屼笉浣跨敤Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 璁惧畾浼犻�鐨刴ethod=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			con.setConnectTimeout(300000);
			con.setReadTimeout(300000);
			/* 璁惧畾DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + filename + "\"\r\n");
			ds.writeBytes("Content-Type:application/octet-stream\r\n\r\n");
			ds.write(data, 0, data.length);
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();
			/* 鍙栧緱Response鍐呭 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer result = new StringBuffer();
			while ((ch = is.read()) != -1) {
				result.append((char) ch);
			}

			/* 鍏抽棴DataOutputStream */
			ds.close();
			return result.toString();
		} catch (Exception e) {
			return null;
		} finally {
			if (con != null)
				con.disconnect();
		}
	}
    /* 上传文件至Server，uploadUrl：接收文件的处理页面 */
    public static String uploadStreamFile(String filepathname, String filename) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
            // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
                    + filename + "\"" + end);
            dos.writeBytes(end);

            // 将要上传的内容写入流中

            FileInputStream srcis = new FileInputStream(filepathname);
            System.out.println("available:"+srcis.available());
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取文件
            while ((count = srcis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            srcis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            // 上传返回值
            String sl;
            String result="";
            while((sl = br.readLine()) != null)
                result = result+sl;
            br.close();
            is.close();
            return result;
            //dos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "网络出错!";
        }
    }
    public static String uploadVedioFile(String filepathname, String filename,Handler handler) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadVideoUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
            // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(Integer.MAX_VALUE);
            httpURLConnection.setReadTimeout(Integer.MAX_VALUE);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
                    + filename + "\"" + end);
            dos.writeBytes(end);

            // 将要上传的内容写入流中

            FileInputStream srcis = new FileInputStream(filepathname);
            double allLength=srcis.available();
            double nowLength=0;
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取文件
            while ((count = srcis.read(buffer)) != -1) {
                Message ms=new Message();
                Map<String,String>map=new HashMap<>();
                double n=(nowLength/allLength)*100;
                int n1=(int)n;
                map.put("videoNum",n1+"");
                ms.obj=map;
                ms.what=3;
                handler.sendMessage(ms);
                nowLength+=8192;
                dos.write(buffer, 0, count);
            }
            srcis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            // 上传返回值
            String sl;
            String result="";

            while((sl = br.readLine()) != null)
                result = result+sl;

            br.close();
            is.close();
            return result;
            //dos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "网络出错!";
        }
    }
	public static String uploadFile(String filepathname, String newName) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);
            System.out.println("图片的绝对地址为:"+filepathname);
			FileInputStream fStream = new FileInputStream(filepathname);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;	
			while ((length = fStream.read(buffer)) != -1) {				
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			fStream.close();
			ds.flush();
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			ds.close();
			s="图片全部成功上传";
			return  b.toString();
		} catch (Exception e) {
			s=e.toString();
			return null;
		}
	}
}