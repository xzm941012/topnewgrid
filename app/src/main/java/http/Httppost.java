package http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.topnewgrid.obj.User;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 真爱de仙 on 2015/1/19.
 */
public class Httppost {
    private String serverIp =PostUrl.Url;
    private String serverPort = "8080";
    private String uploadUrl = "http://"+PostUrl.Url+":8080/RegisterAndLogin/UploadServlet";

    public String regist(String email, String password1, String password2, String name)  {
        if ("".equals(name)) {
            //用户输入用户名为空

        } else if ("".equals(password1)) {
            //密码不能为空

        } else if ("".equals(password2)) {
            //密码不能为空

        } else if ("".equals(email)) {
            //密码不能为空

        } else if (!password1.equals(password2)) {
            //密码不能为空

        }
        String httpStr = "http://";
        String postStr = httpStr + serverIp + ":" + serverPort
                + "/Server/android/RegisterServlet.jsp";
        String questStr = "{REGISTER:{username:'" + name + "',password1:'"
                + password1 + "',password2:'" + password1 + "',email:'"
                + email + "'}}";
        System.out.println("====questStr====" + questStr);
        System.out.println("====postStr====" + postStr);
        try {

            HttpParams httpParams = new BasicHttpParams();
            // 设置连接超时
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    timeoutConnection);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(postStr);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", name));
            nvps.add(new BasicNameValuePair("email", email));
            nvps.add(new BasicNameValuePair("password1", password1));
            nvps.add(new BasicNameValuePair("password2", password2));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            String isUser = ConvertStreamToString(is);

            System.out.println(isUser);
            return isUser;
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public User login(String email, String password1) {
        // 获取用户输入的用户名,密码

        String username = email.trim();
        String password = password1.trim();
        String httpstr = "http://";

        String postStr = httpstr + serverIp + ":" + serverPort
                + "/Server/android/LoginServlet.jsp";
        // questStr = "{LOGIN:{username:'}" + username + "',password:'"
        // + password + "'}}";
        String questStr = "{LOGIN:{username:'" + username + "',password:'" + password
                + "'}}";

        System.out.println("====questStr====" + questStr);
        System.out.println("====postStr====" + postStr);

        try {
            // 设置连接超时
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(postStr);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", username));
            nvps.add(new BasicNameValuePair("password", password));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            String isUser = ConvertStreamToString(is);
            System.out.println("asdfsdf" + isUser);

            if ("false".equals(isUser)) {
                //用户已经被注册
                System.out.println("登录失败");
                return null;
            } else {
                User user=new User();
                //表示用户注册成功
                JSONObject obj;
                try {
                    System.out.println(isUser);

                    obj = new JSONObject(isUser);
                    StringBuffer result = new StringBuffer();
                    user.setUserId(obj.getString("id"));
                    user.setInformation(obj.getString("information"));
                    user.setSex(obj.getString("sex"));
                    user.setJoinNum(obj.getInt("joinNum"));
                    user.setPublicNum(obj.getInt("publicNum"));
                    user.setProfession(obj.getString("profession"));
                    user.setEmail(obj.getString("email"));
                    user.setName(obj.getString("username"));
                    user.setPassword(obj.getString("password"));
                    user.setRegisterTime(obj.getString("registerTime"));
                    /*
                    if(obj.getString("publicActivity")!=null&&!obj.getString("publicActivity").equals("")){
                        List<String>publicActivityList=new ArrayList<String>();
                        String[]activitys=obj.getString("publicActivity").split(";");
                        for(String activity:activitys){
                            publicActivityList.add(activity);
                        }
                        System.out.println("publicAvtivity:"+publicActivityList);
                        user.setPublicActivity(publicActivityList);
                    }
                    */
                    if(!obj.getString("album").equals("")){
                        List<String>album=new ArrayList<>();
                        String []albums=obj.getString("album").split(";");
                        for(String a:albums){
                            album.add(a);
                        }
                        user.setAlbum(album);
                    }
                    List<String>huodongList=new ArrayList<String>();
                    if(obj.getString("activityTag")!=null&&!obj.getString("activityTag").equals("")){
                        System.out.println("我擦"+obj.optString("activityTag"));
                        String[] huodongTitle=obj.optString("activityTag").split(";");
                        huodongList.add("推荐");
                        for(String a:huodongTitle){
                            huodongList.add(a);
                        }
                    }

                    if(huodongList==null||huodongList.size()==0){
                        huodongList.add("推荐");
                        huodongList.add("运动");
                        huodongList.add("学习");
                        huodongList.add("社团");
                        huodongList.add("游戏");
                        huodongList.add("登山");
                        huodongList.add("水上运动");
                        huodongList.add("拼车");
                        huodongList.add("蹭饭");

                        huodongList.add("");
                    }
                    user.setHuodongTitle(huodongList);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return user;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Bitmap readImg(String id) {

        String uri="http://"+ PostUrl.Media+":8080/upload/"+id+".jpg";
        HttpClient client=new HttpClient();
        client.getParams().setContentCharset("utf-8");
        client.getParams().setSoTimeout(10000);
        client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        GetMethod getMethod = new GetMethod(uri);
        System.out.println("1");
        try {
            if(client.executeMethod(getMethod)== HttpStatus.SC_OK){
                System.out.println("2");
                InputStream inputStream = getMethod.getResponseBodyAsStream();
                System.out.println("3");
                Bitmap img= BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                return img;
            }

        } catch (HttpException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }finally{
            getMethod.releaseConnection();
        }
        System.out.println("图片下载不了");
        return null;
    }
    public static Boolean urlIsTrue(String id){
        String uri="http://"+ PostUrl.Media+":8080/upload/"+id+".jpg";
        HttpClient client=new HttpClient();
        client.getParams().setContentCharset("utf-8");
        client.getParams().setSoTimeout(10000);
        client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        GetMethod getMethod = new GetMethod(uri);
        System.out.println("1");
        try {
            if (client.executeMethod(getMethod) == HttpStatus.SC_OK) {
                return true;
            }
        }catch (HttpException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }finally{
            getMethod.releaseConnection();
        }
        System.out.println("图片下载不了");
        return false;
    }
    // 读取字符流
    public String ConvertStreamToString(InputStream is) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String returnStr = "";
        try {
            while ((returnStr = br.readLine()) != null) {
                sb.append(returnStr);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        final String result = sb.toString();

        System.out.println(result);
        return result;
    }
}
