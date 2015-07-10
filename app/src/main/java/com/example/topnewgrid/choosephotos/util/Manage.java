package com.example.topnewgrid.choosephotos.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Manage {
	/** Called when the activity is first created. */
	public static ArrayList<String> pathList=new ArrayList();
    public static String add(String title, String fenlei,String picturePath,String neirong,String ifNeedTupian,String renshu,String endTime,String userId) throws Exception {
        // 使用Map封装请求参数
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", title);
        map.put("fenlei", fenlei);
        map.put("picturePath",picturePath);
        map.put("neirong",neirong);
        map.put("ifNeedTupian",ifNeedTupian);
        map.put("renshu",renshu);
        map.put("endtime",endTime);
        map.put("userId",userId);
        // 定义请求将会发送到addKind.jsp页面
        String url = com.example.topnewgrid.util.HttpUtil.BASE_URL + "AddActivity.jsp";
        // 发送请求
        return com.example.topnewgrid.util.HttpUtil.postRequest(url, map);
    }

	public static String delete(String id) throws Exception {
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		String url = HttpUtil.BASE_URL + "DeleteActivity.jsp";
		// 发送请求
		return  HttpUtil.postRequest(url, map);
	}

	public static String viewById(String id) throws Exception {
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		String url = HttpUtil.BASE_URL + "ViewActivityById.jsp";
		// 发送请求
		String body=HttpUtil.postRequest(url, map);
		JSONObject obj=new JSONObject(body);
		StringBuffer result= new StringBuffer();
		result.append("id:").append(obj.getInt("id")).append("\t");
		result.append("personName:").append(obj.getString("personName")).append("\t");
		result.append("date:").append(obj.getString("date")).append("\r\n");
		result.append("category:").append(obj.getString("category")).append("\r\n");
		return result.toString();
	}

	public static String alter(String id, String name, String date,String picturePath,String category) throws Exception {
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("personName", name);
		map.put("date", date);
		map.put("picturePath",picturePath);
		map.put("category",category);
		// 定义请求将会发送到addKind.jsp页面
		String url = HttpUtil.BASE_URL + "UpdateActivity.jsp";
		// 发送请求
		return HttpUtil.postRequest(url, map);
	}

	public static String list() throws Exception {
		// 发送请求
		String url = HttpUtil.BASE_URL + "ListActivity.jsp";
		String body = HttpUtil.getRequest(url);
		StringBuffer result= new StringBuffer();
		JSONArray array = new JSONArray(body);
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			result.append("id:").append(obj.getInt("id")).append("\t");
			result.append("personName:").append(obj.getString("personName")).append("\t");
			result.append("date:").append(obj.getString("date")).append("\r\n");
			result.append("category:").append(obj.getString("category")).append("\r\n");
		}
		return result.toString();
	}
	public static String viewByCategory(String category) throws Exception {
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("category", category);
		String url = HttpUtil.BASE_URL + "ViewActivityByCategory.jsp";
		// 发送请求
		StringBuffer result= new StringBuffer();
		String body=HttpUtil.postRequest(url, map);
		JSONArray array = new JSONArray(body);
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			result.append("id:").append(obj.getInt("id")).append("\t");
			result.append("personName:").append(obj.getString("personName")).append("\t");
			result.append("date:").append(obj.getString("date")).append("\r\n");
			result.append("category:").append(obj.getString("category")).append("\r\n");
			if(!obj.getString("picturePath").equals(""))
			    pathList.add(obj.getString("picturePath"));
		}
		return result.toString();
	}
}