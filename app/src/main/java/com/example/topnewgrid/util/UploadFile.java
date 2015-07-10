package com.example.topnewgrid.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;
public class UploadFile {
	public static String uploadUrl;
	public static String result;
	public UploadFile(String uploadUrl) {
		super();
		this.uploadUrl = uploadUrl;
	}
    /* 上传文件至Server，uploadUrl：接收文件的处理页面 */
    public static String uploadVideoFile(String filepathname, String filename) {
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
	public boolean defaultUploadMethod(byte[] data, String filename) throws IOException {
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
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + filename +"\"\r\n");
			ds.writeBytes("Content-Type:application/octet-stream\r\n\r\n");
			ds.write(data, 0, data.length);
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();
			/* 鍙栧緱Response鍐呭 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			result=b.toString();
			/* 鍏抽棴DataOutputStream */
			ds.close();
			return true;
		} catch (Exception e) {
            Log.v("tag", e.toString());
            return false;
		} finally {
			if (con != null)
				con.disconnect();
		}

	}

}