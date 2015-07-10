package http;

import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.Resume;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import drawerlistitem.PindaoListItem;
import drawerlistitem.PinglunItem;

public abstract class Manage {
    private Huodong huodong;
	/** Called when the activity is first created. */
	public static ArrayList<String> pathList=new ArrayList();
    //发布活动
    public static String add(String title, String fenlei,String picturePath,String neirong,String ifNeedTupian,String renshu,String endTime,String userId,String authorName,String longitude,String latitude,String locate) throws Exception {
        // 使用Map封装请求参数
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", title);
        map.put("fenlei", fenlei);
        map.put("picturePath",picturePath);
        map.put("neirong",neirong);
        map.put("ifNeedTupian",ifNeedTupian);
        map.put("renshu",renshu);
        map.put("endtime",endTime);
        map.put("authorID",userId);
        map.put("authorName",authorName);
        map.put("longitude",longitude);
        map.put("latitude",latitude);
        map.put("locate",locate);

        // 定义请求将会发送到addKind.jsp页面
        String url = com.example.topnewgrid.util.HttpUtil.BASE_URL + "AddActivity.jsp";
        // 发送请求
        String result=com.example.topnewgrid.util.HttpUtil.postRequest(url, map);
        System.out.println(result);
        return result;
    }
    //创建简历
    public static String AddResume(String name,String sex,String age,String locate,String contactInformation,String education,
                                   String workExperience,String merit,String skill,String honour,String evaluation,String userId,String action) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name",name);
        map.put("sex",sex);
        map.put("age", age);
        map.put("locate",locate);
        map.put("contactInformation",contactInformation);
        map.put("education",education);
        map.put("workExperience",workExperience);
        map.put("merit",merit);
        map.put("skill",skill);
        map.put("honour",honour);
        map.put("evaluation",evaluation);
        map.put("userId",userId);
        map.put("action",action);

        String url = HttpUtil.BASE_URL + "SaveResume.jsp";

        return HttpUtil.postRequest(url, map);
    }
    //收藏活动
    public static String CollectionActivity(String id,String activityId) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("activityId",activityId);
        String url = HttpUtil.BASE_URL + "CollectionActivity.jsp";
        return HttpUtil.postRequest(url, map);
    }
    //修改简历
    public static String AlterResume(String name,String sex,String age,String locate,String contactInformation,String education,
                                     String workExperience,String merit,String skill,String honour,String evaluation,String userId,String id) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name",name);
        map.put("sex",sex);
        map.put("age", age);
        map.put("locate",locate);
        map.put("contactInformation",contactInformation);
        map.put("education",education);
        map.put("workExperience",workExperience);
        map.put("merit",merit);
        map.put("skill",skill);
        map.put("honour",honour);
        map.put("evaluation",evaluation);
        map.put("userId",userId);
        map.put("id",id);
        String url = HttpUtil.BASE_URL + "AlterResume.jsp";
        return HttpUtil.postRequest(url, map);
    }
    //查看附近的人
    public static  List<OtherUser> NearByPerson(String id,String pageNum) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        List<OtherUser>friendList=new ArrayList<OtherUser>();
        map.put("id",id);
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "NearByPerson.jsp";
        String body = HttpUtil.postRequest(url, map);
        StringBuffer result = new StringBuffer();
        if(body==null)
            return null;
        else if("查看附近的人需先定位".equals(body))
            return null;
        else if ("f".equals(body))
            return null;
        else{
            Gson gson = new Gson();
            friendList=gson.fromJson(body, new TypeToken<List<OtherUser>>(){}.getType());
        }
        return friendList;
    }
    //上传定位坐标
    public static String GPSUser(String id,String longitude,String latitude,String locate) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("longitude",longitude);
        map.put("latitude",latitude);
        map.put("locate",locate);
        String url = HttpUtil.BASE_URL + "GPSUser.jsp";
        return HttpUtil.postRequest(url, map);
    }
    //查找活动
    public static List<Huodong> ResearchActivity(String name,String pageNum) throws Exception {
        List<Huodong> huodongList = new ArrayList<Huodong>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("pageNum", pageNum);
        String url = HttpUtil.BASE_URL + "ResearchActivity.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        StringBuffer result = new StringBuffer();
        if ("无相关活动".equals(body))
            result.append("无相关活动");
        else if ("f".equals(body))
            result.append("超出页数");
        else {
            JSONArray array = new JSONArray(body);
            for (int i = 0; i < array.length(); i++) {
                Huodong huodong1 = new Huodong();
                JSONObject obj = array.getJSONObject(i);
                huodong1.setHuodongId(obj.getInt("id"));
                huodong1.setSex(obj.getString("conditions"));
                huodong1.setAuthorId(obj.getInt("authorID")+"");
                huodong1.setTitle(obj.getString("title"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setFengmianUrl(obj.getString("picturePath"));
                huodong1.setPinglunNum(obj.getInt("commentAmount"));
                huodong1.setCollected(obj.getInt("collectionAmount"));
                huodong1.setAuthorName(obj.getString("authorName"));
                huodong1.setChuangjianTime(obj.getString("publicTime"));
                huodong1.setFlag(obj.getString("flag"));
                huodong1.setJoinMun(obj.getInt("enrollAmount"));
                huodong1.setLocate(obj.getString("locate"));
                huodong1.setAddress(obj.getString("address"));
                huodong1.setRecompense(obj.getString("recompense"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setStyle(obj.getString("style"));
                if(!obj.getString("picturePath").equals("")){
                    huodong1.setFengmianUrl(obj.getString("picturePath"));
                }

                huodongList.add(huodong1);
            }

        }
        return huodongList;
    }
    //查找频道
    public static List<PindaoListItem> ResearhChannel(String tag,String pageNum) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tag",tag);
        map.put("pageNum", pageNum);
        String url = HttpUtil.BASE_URL + "ResearhChannel.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        StringBuffer result = new StringBuffer();
        List<PindaoListItem>pindaoListItemList=new ArrayList<PindaoListItem>();
        if("无相关频道".equals(body))
            return null;
        else if ("f".equals(body))
            return null;
        else{
            JSONArray array = new JSONArray(body);
            for (int i = 0; i < array.length(); i++) {
                PindaoListItem pindaoListItem=new PindaoListItem();
                JSONObject obj = array.getJSONObject(i);
                pindaoListItem.setPindaoName(obj.getString("tag"));
                pindaoListItem.setPindaoJianjie(obj.getString("introduction"));
                pindaoListItem.setPindaoNum(obj.getString("members").split(";").length+"");
                pindaoListItemList.add(pindaoListItem);

            }
        }
        return pindaoListItemList;
    }
    public static List<Huodong> RecommendActivity(String userId,String locate,String style,String longitude,String latitude,String num) throws Exception {
        List<Huodong> huodongList=new ArrayList<Huodong>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId",userId);//推荐活动或需求的目标用户id
        map.put("locate",locate);//用户所处地区，默认推荐的活动或需求同城
        map.put("style", style);//style=”活动“，推荐活动 style=”需求“，推荐需求 style=”“，推荐全部
        map.put("longitude",longitude);//经度，为”“这不把附近这一因素考虑进去
        map.put("latitude", latitude);//纬度，为”“这不把附近这一因素考虑进去
        //map.put("num",num);//需要推荐的活动或需求个数，为“”则默认为1
        String url = HttpUtil.BASE_URL + "RecommendActivity.jsp";
        String body = HttpUtil.postRequest(url, map);
        StringBuffer result = new StringBuffer();
        if(body.equals("该用户的使用痕迹少，暂无推荐"))
            return null;
        else if (body.equals("f"))
            return null;
        else{
            JSONArray array = new JSONArray(body);
            for (int i = 0; i < array.length(); i++) {
                Huodong huodong1=new Huodong();
                JSONObject obj = array.getJSONObject(i);
                huodong1.setHuodongId(obj.getInt("id"));
                huodong1.setSex(obj.getString("conditions"));
                huodong1.setAuthorId(obj.getInt("authorID")+"");
                huodong1.setTitle(obj.getString("title"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setFengmianUrl(obj.getString("picturePath"));
                huodong1.setPinglunNum(obj.getInt("commentAmount"));
                huodong1.setCollected(obj.getInt("collectionAmount"));
                huodong1.setAuthorName(obj.getString("authorName"));
                huodong1.setChuangjianTime(obj.getString("publicTime"));
                huodong1.setFlag(obj.getString("flag"));
                huodong1.setJoinMun(obj.getInt("enrollAmount"));
                huodong1.setLocate(obj.getString("locate"));
                huodong1.setAddress(obj.getString("address"));
                huodong1.setRecompense(obj.getString("recompense"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setStyle(obj.getString("style"));
                if(!obj.getString("picturePath").equals("")){
                    huodong1.setFengmianUrl(obj.getString("picturePath"));
                }
                huodong1.setDistance(obj.getString("distance").trim());
                huodongList.add(huodong1);
            }
            return huodongList;
        }
    }
    public static List<Huodong> RecommendActivity2(String userId,String locate,String style,String longitude,String latitude,String num) throws Exception {
        List<Huodong> huodongList=new ArrayList<Huodong>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId",userId);//推荐活动或需求的目标用户id
        map.put("locate",locate);//用户所处地区，默认推荐的活动或需求同城
        map.put("style", style);//style=”活动“，推荐活动 style=”需求“，推荐需求 style=”“，推荐全部
        map.put("longitude",longitude);//经度，为”“这不把附近这一因素考虑进去
        map.put("latitude", latitude);//纬度，为”“这不把附近这一因素考虑进去
        //map.put("num",num);//需要推荐的活动或需求个数，为“”则默认为1
        String url = HttpUtil.BASE_URL + "RecommendActivity.jsp";
        String body = HttpUtil.postRequest(url, map);
        StringBuffer result = new StringBuffer();
        if(body.equals("该用户的使用痕迹少，暂无推荐"))
            return null;
        else if (body.equals("f"))
            return null;
        else{
                JSONObject obj = new JSONObject(body);
                Huodong huodong1=new Huodong();
                huodong1.setHuodongId(obj.getInt("id"));
                huodong1.setSex(obj.getString("conditions"));
                huodong1.setAuthorId(obj.getInt("authorID")+"");
                huodong1.setTitle(obj.getString("title"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setFengmianUrl(obj.getString("picturePath"));
                huodong1.setPinglunNum(obj.getInt("commentAmount"));
                huodong1.setCollected(obj.getInt("collectionAmount"));
                huodong1.setAuthorName(obj.getString("authorName"));
                huodong1.setChuangjianTime(obj.getString("publicTime"));
                huodong1.setFlag(obj.getString("flag"));
                huodong1.setJoinMun(obj.getInt("enrollAmount"));
                huodong1.setLocate(obj.getString("locate"));
                huodong1.setAddress(obj.getString("address"));
                huodong1.setRecompense(obj.getString("recompense"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setStyle(obj.getString("style"));
                if(!obj.getString("picturePath").equals("")){
                    huodong1.setFengmianUrl(obj.getString("picturePath"));
                }
                huodong1.setDistance(obj.getString("distance").trim());
                huodongList.add(huodong1);

            return huodongList;
        }
    }
    //查看标签活动 listStyle="0"按最新排序，style=""，默认全显示 style="活动"，显示活动 style="需求"，显示需求
    public static List<Huodong>  findActivityByTag(String style,String tag, String listStyle, String pageNum, String longitude, String latitude, String locate) throws Exception {
// 发送请求
        List<Huodong> huodongList=new ArrayList<Huodong>();
        Map<String, String> map = new HashMap<String, String>();

        map.put("style", style);
        map.put("tag", tag);
        map.put("listStyle", listStyle);
        map.put("pageNum", pageNum);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("locate", locate);
        map.put("userId","");

        String url = HttpUtil.BASE_URL + "ViewActivityByCategory.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        if ((body.trim().equals("f"))){
            return  null;
        }
        StringBuffer result = new StringBuffer();
        JSONArray array = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            Huodong huodong1=new Huodong();
            JSONObject obj = array.getJSONObject(i);
            huodong1.setHuodongId(obj.getInt("id"));
            huodong1.setSex(obj.getString("conditions"));
            huodong1.setAuthorId(obj.getInt("authorID")+"");
            huodong1.setTitle(obj.getString("title"));
            huodong1.setFenlei(obj.getString("fenlei"));
            huodong1.setContent(obj.getString("neirong"));
            huodong1.setFengmianUrl(obj.getString("picturePath"));
            huodong1.setPinglunNum(obj.getInt("commentAmount"));
            huodong1.setCollected(obj.getInt("collectionAmount"));
            huodong1.setAuthorName(obj.getString("authorName"));
            huodong1.setChuangjianTime(obj.getString("publicTime"));
            huodong1.setFlag(obj.getString("flag"));
            huodong1.setJoinMun(obj.getInt("enrollAmount"));
            huodong1.setLocate(obj.getString("locate"));
            huodong1.setAddress(obj.getString("address"));
            huodong1.setRecompense(obj.getString("recompense"));
            huodong1.setContent(obj.getString("neirong"));
            huodong1.setStyle(obj.getString("style"));
            if(!obj.getString("picturePath").equals("")){
                huodong1.setFengmianUrl(obj.getString("picturePath"));
            }
            if (listStyle.equals("1")){
                huodong1.setDistance(obj.getString("distance").trim());
            }
            huodongList.add(huodong1);
        }
        return huodongList;
    }
    public static List<Huodong>  findActivityByTag(String style,String tag, String listStyle, String pageNum, String longitude, String latitude, String locate,String userid) throws Exception {
// 发送请求
        List<Huodong> huodongList=new ArrayList<Huodong>();
        Map<String, String> map = new HashMap<String, String>();

        map.put("style", style);
        map.put("tag", tag);
        map.put("listStyle", listStyle);
        map.put("pageNum", pageNum);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("locate", locate);
        map.put("userId",userid);

        String url = HttpUtil.BASE_URL + "ViewActivityByCategory.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        if ((body.trim().equals("f"))){
            return  null;
        }
        StringBuffer result = new StringBuffer();
        JSONArray array = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            Huodong huodong1=new Huodong();
            JSONObject obj = array.getJSONObject(i);
            huodong1.setHuodongId(obj.getInt("id"));
            huodong1.setSex(obj.getString("conditions"));
            huodong1.setAuthorId(obj.getInt("authorID")+"");
            huodong1.setTitle(obj.getString("title"));
            huodong1.setFenlei(obj.getString("fenlei"));
            huodong1.setContent(obj.getString("neirong"));
            huodong1.setFengmianUrl(obj.getString("picturePath"));
            huodong1.setPinglunNum(obj.getInt("commentAmount"));
            huodong1.setCollected(obj.getInt("collectionAmount"));
            huodong1.setAuthorName(obj.getString("authorName"));
            huodong1.setChuangjianTime(obj.getString("publicTime"));
            huodong1.setFlag(obj.getString("flag"));
            huodong1.setJoinMun(obj.getInt("enrollAmount"));
            huodong1.setLocate(obj.getString("locate"));
            huodong1.setAddress(obj.getString("address"));
            huodong1.setRecompense(obj.getString("recompense"));
            huodong1.setContent(obj.getString("neirong"));
            huodong1.setStyle(obj.getString("style"));
            if(!obj.getString("picturePath").equals("")){
                huodong1.setFengmianUrl(obj.getString("picturePath"));
            }
            if (listStyle.equals("1")){
                huodong1.setDistance(obj.getString("distance").trim());
            }
            huodongList.add(huodong1);
        }
        return huodongList;
    }
    //添加活动信息，最新
    public static String addActivity(String title, String fenlei, String picturePath, String neirong, String ifNeedTupian, String renshu, String executeTime, String authorName,String authorID,
                                     String longitude, String latitude, String locate,String style,String recompense,String contactInformation,String address,String needResume,String sex,String locationx,String locationy,String locationAdress,String locationPoi,String locationPoi_Adress,String conditions) throws Exception {
// 使用Map封装请求参数
        Map<String, String> map = new HashMap<String, String>();
        if(locationx==null){
            locationx="";
        }
        if(locationy==null){
            locationy="";
        }
        if(locationAdress==null){
            locationAdress="";
        }
        if(locationPoi==null){
            locationPoi="";
        }
        if(locationPoi_Adress==null){
            locationPoi_Adress="";
        }
        if(address==null){
            address="";
        }
        if(address==null){
            address="";
        }
        map.put("locationx",locationx);
        map.put("locationy",locationy);
        map.put("locationAdress",locationAdress);
        map.put("locationPoi",locationPoi);
        map.put("locationPoi_Adress",locationPoi_Adress);
        map.put("title", title);
        map.put("conditions","");
        map.put("sex",sex);
        map.put("fenlei", fenlei);
        map.put("picturePath", picturePath);
        map.put("neirong", neirong);
        map.put("ifNeedTupian", ifNeedTupian);
        map.put("renshu", renshu);
        map.put("executeTime",executeTime);
        map.put("authorID", authorID);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("locate", locate);
        map.put("authorName",authorName);
        map.put("style",style);
        map.put("recompense",recompense);
        map.put("contactInformation",contactInformation);
        map.put("address",address);
        map.put("needResume",needResume);
// 定义请求将会发送到addKind.jsp页面
        String url = HttpUtil.BASE_URL + "AddActivity.jsp";
// 发送请求
        return HttpUtil.postRequest(url, map);
    }
    public static String addComment(String information,String activityID,String authorID,String style) throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=df.format(new Date());
        Map<String, String> map = new HashMap<String, String>();
        map.put("information",information);
        map.put("date",date);
        map.put("activityID",activityID);
        map.put("authorID",authorID);
        map.put("style",style);

        String url = HttpUtil.BASE_URL + "AddComment.jsp";
        return HttpUtil.postRequest(url, map);
    }
    //查看活动评论
    public static List<PinglunItem> ViewCommentByActivity(String activityID,String pageNum,String style) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("activityID",activityID);
        map.put("pageNum",pageNum);
        map.put("style", style);
        String url = HttpUtil.BASE_URL + "ViewCommentByActivity.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println("body:"+body);
        if(body==null){
            return null;
        }else if(body.trim().equals("该活动没有评论")||body.trim().equals("f")){
            return null;
        }
        StringBuffer result= new StringBuffer();
        JSONArray array = new JSONArray(body);
        List<PinglunItem>pinglunItemList=new ArrayList<PinglunItem>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            System.out.println(obj);
            PinglunItem pinglunItem=new PinglunItem();
            pinglunItem.setPinglunId(obj.getInt("id"));
            pinglunItem.setContent(obj.getString("information"));
            pinglunItem.setTime(obj.getString("date"));
            pinglunItem.setUserId(obj.getInt("authorID"));
            pinglunItem.setHuodongId(obj.getInt("activityID"));
            pinglunItem.setNum(obj.getString("nums"));
            pinglunItem.setName(obj.getString("authorName"));
            pinglunItemList.add(pinglunItem);
            result.append(obj.toString()).append("\r\n");
        }
        return pinglunItemList;
    }
    //报名
    public static String enrollByActivityId(String flag,String userId,String activityId,String picturePath) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("flag",flag);
        map.put("userId",userId);
        map.put("activityId",activityId);
        map.put("picturePath",picturePath);
        String url = HttpUtil.BASE_URL + "enrollByActivityId.jsp";
        return HttpUtil.postRequest(url, map);
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
    //关注好友
    public static String FocusingFriend(String id,String friendID) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("friendID",friendID);
        String url = HttpUtil.BASE_URL + "FocusingFriend.jsp";
        return HttpUtil.postRequest(url, map);
    }
    //搜索用户
    public static List<OtherUser> ResearchUserByName(String name,String pageNum) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        List<OtherUser>friendList=new ArrayList<OtherUser>();
        map.put("name",name);
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "ResearchUserByName.jsp";
        String body = HttpUtil.postRequest(url, map);
        StringBuffer result = new StringBuffer();
        if("无类似用户".equals(body))
            return null;
        else if ("f".equals(body))
            return null;
        else{
            JSONArray array=new JSONArray(body);
            for(int i=0;i<array.length();i++){
                JSONObject obj=array.getJSONObject(i);
                OtherUser otherUser=new OtherUser();
                otherUser.setId(obj.getInt("id"));
                otherUser.setInformation(obj.getString("information"));
                otherUser.setSex(obj.getString("sex"));
                otherUser.setDistance(obj.getInt("distance"));
                otherUser.setHeadSculpture(obj.getString("headSculpture"));
                otherUser.setJoinNum(obj.getInt("joinNum"));
                otherUser.setProfession("profession");
                otherUser.setAlbum(obj.getString("album"));
                otherUser.setUserName(obj.getString("userName"));
                otherUser.setPublicNum(obj.getInt("publicNum"));
                otherUser.setLocate(obj.getString("locate"));
                otherUser.setRegisterTime(obj.getString("registerTime"));
                friendList.add(otherUser);
            }
        }
        return friendList;
    }
    //qq登陆用
    public static User QQLogin(String username,String email,String headSculpture
            ,String sex,String locate,String imageurl) throws Exception {
// 使用Map封装请求参数
        Map<String, String> map = new HashMap<String, String>();
        map.put("username",username);//昵称
        map.put("email",email);//唯一序列表
        map.put("headSculpture",headSculpture);//头像地址
        map.put("sex",sex);//性别
        map.put("locate",locate);//地址
        map.put("imgUrl",imageurl);

        String url = HttpUtil.BASE_URL + "QQLogin.jsp";
// 发送请求
        String body = HttpUtil.postRequest(url, map);
        if ("false".equals(body)) {
            //用户已经被注册
            System.out.println("登录失败");
            return null;
        } else {
            User user=new User();
            //表示用户注册成功
            JSONObject obj;
            try {
                System.out.println(body);
                obj = new JSONObject(body);
                StringBuffer result = new StringBuffer();
                user.setUserId(obj.getString("id"));
                user.setInformation(obj.getString("information"));
                user.setSex(obj.getString("sex"));
                user.setJoinNum(obj.getInt("joinNum"));
                user.setHometown(obj.getString("hometown"));

                user.setPublicNum(obj.getInt("publicNum"));
                user.setProfession(obj.getString("profession"));
                user.setName(obj.getString("username"));
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

                if(!obj.getString("friendID").equals("")){
                    String[] a=obj.getString("friendID").split(";");
                    List<String>a1=new ArrayList<>();
                    for(String a2:a){
                        a1.add(a2);
                    }
                    user.setFriendList(a1);
                }else{
                    List<String>a1=new ArrayList<>();
                }
                */
                if(!obj.getString("album").equals("")){
                    List<String>album=new ArrayList<>();
                    String []albums=obj.getString("album").split(";");
                    for(String a:albums){
                        album.add(a);
                    }
                    user.setAlbum(album);
                }else{
                    user.setAlbum(new ArrayList<String>());
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
                    huodongList.add("旅游");
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
    }
    //智能推荐的人
    public static List<OtherUser> RecommendUser(String id,String pageNum) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        Gson gson = new Gson();
        List<OtherUser>friendList=new ArrayList<OtherUser>();
        map.put("id",id);//需求id
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "RecommendUser.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        StringBuffer result = new StringBuffer();
        if(body.equals("该用户需先添加频道或参与活动才能获得智能推荐"))
            return null;
        else if (body.equals("f"))
            return null;
        else{
            friendList=gson.fromJson(body, new TypeToken<List<OtherUser>>(){}.getType());
        }
        return friendList;
    }
    //查看好友列表 flag=0 我关注的人 flag=1 关注我的人
    public static List<OtherUser> ViewUserByFriend(String id,String pageNum,String flag) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        List<OtherUser>friendList=new ArrayList<OtherUser>();
        Gson gson = new Gson();
        map.put("id",id);
        map.put("pageNum",pageNum);
        map.put("flag",flag);
        String url = HttpUtil.BASE_URL + "ViewUserByFriend.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        StringBuffer result = new StringBuffer();
        if (!"尚未关注好友".equals(body)&&!"f".equals(body)) {
            friendList=gson.fromJson(body, new TypeToken<List<OtherUser>>(){}.getType());
        } else
            return null;
        return friendList;
    }
    //查找简历
    public static Resume FindResumeByUserId(String id) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        String url = HttpUtil.BASE_URL + "FindResumeByUserId.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        StringBuffer result = new StringBuffer();
        Resume resume;
        if(body.equals("该用户没建立简历"))
            return null;
        else{
            Gson gson=new Gson();
            resume=gson.fromJson(body,Resume.class);

        }
        return resume;
    }
    //上传简历
    public static String SaveResume(String name,String sex,String age,String locate,String contactInformation,String education,
                                    String workExperience,String merit,String skill,String honour,String evaluation,String userId,String action) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name",name);
        map.put("sex",sex);
        map.put("age", age);
        map.put("locate",locate);
        map.put("contactInformation",contactInformation);
        map.put("education",education);
        map.put("workExperience",workExperience);
        map.put("merit",merit);
        map.put("skill",skill);
        map.put("honour",honour);
        map.put("evaluation",evaluation);
        map.put("userId",userId);
        map.put("action",action);
        String url = HttpUtil.BASE_URL + "SaveResume.jsp";
        return HttpUtil.postRequest(url, map);
    }
    public static Huodong viewActivityByID(String id) throws Exception {
        // 使用Map封装请求参数
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        String url = HttpUtil.BASE_URL + "ViewActivityById.jsp";
        // 发送请求
        String body=HttpUtil.postRequest(url, map);
        System.out.println("这个活动的id是："+id);
        System.out.println("这个活动的详情是："+body);
        JSONObject obj=new JSONObject(body);
        Huodong huodong=new Huodong();
        huodong.setAuthorInformation(obj.getString("authorInformation"));
        huodong.setFlag(obj.getString("flag"));
        huodong.setExecuteTime(obj.getString("executeTime"));
        huodong.setHuodongId(obj.getInt("id"));
        huodong.setPinglunNum(obj.getInt("commentAmount"));
        huodong.setTitle(obj.getString("title"));
        huodong.setContent(obj.getString("neirong"));
        huodong.setIfneedtupian(obj.getInt("ifneedtupian"));
        huodong.setLevel(obj.getInt("level"));

        huodong.setAuthorId(obj.getInt("authorID")+"");
        huodong.setLocate(obj.getString("locate"));
        huodong.setFengmianUrl(obj.getString("picturePath"));
        huodong.setFenlei(obj.getString("fenlei"));
        huodong.setChuangjianTime(obj.getString("publicTime"));
        huodong.setCollected(obj.getInt("collectionAmount"));
        huodong.setIfneedjianli(obj.getInt("needResume"));
        huodong.setStyle(obj.getString("style"));
        huodong.setFenlei(obj.getString("fenlei"));
        if(!obj.getString("enrollUser").equals("")){
            String[]a=obj.getString("enrollUser").split(";");
            List<String>a1=new ArrayList<>();
            for(String a2:a){
                if(!a2.equals("")){
                    a1.add(a2);
                }
            }
            huodong.setEnrollUser(a1);
        }else{
            huodong.setEnrollUser(new ArrayList<String>());
        }
        if(!obj.getString("renshu").equals("")) {
            huodong.setPeopleNum(Integer.parseInt(obj.getString("renshu")));
        }
        huodong.setJoinMun(obj.getInt("enrollAmount"));
        huodong.setAuthorName(obj.getString("authorName"));
        huodong.setLocationPoi((obj.getString("locationPoi")));
        huodong.setLocationAdress(obj.getString("locationAdress"));
        huodong.setLocationPoi_Adress("locationPoi_Adress");
        huodong.setRecompense(obj.getString("recompense"));
        huodong.setLocationx(obj.getString("locationx"));
        huodong.setLocationy(obj.getString("locationy"));
        huodong.setAddress(obj.getString("address"));
        huodong.setContactInformation(obj.getString("contactInformation"));
        StringBuffer result= new StringBuffer();
        result.append(obj.toString()).append("\r\n");
        System.out.println("这个活动的详情是："+result.toString());
        return huodong;
    }
    //修改用户信息，如果原来信息无需修改，则为空
    //DialogUtil.showDialog(MainActivity.this,Manage.AlterUser("1","男","医生","羽毛球")
    //DialogUtil.showDialog(MainActivity.this,Manage.AlterUser("2","女","",""),true)
    public static String AlterUser(String id,String sex,String profession,String activityTag) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("sex", sex);
        map.put("profession",profession);
        map.put("activityTag",activityTag);
        String url = HttpUtil.BASE_URL + "AlterUser.jsp";
        return HttpUtil.postRequest(url, map);
    }
    public static String AlterUser(String id,String sex,String profession,String activityTag,String information) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("sex", sex);
        map.put("profession",profession);
        map.put("activityTag",activityTag);
        map.put("information",information);
        String url = HttpUtil.BASE_URL + "AlterUser.jsp";
        return HttpUtil.postRequest(url, map);
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
    //创建频道
    public static String AddChannel(String tag,String assortment, String introduction,String userID) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tag", tag);  //  小频道
        map.put("assortment", assortment);  //大频道
        map.put("introduction", introduction);//  频道介绍
        map.put("userID",userID);
        String url = HttpUtil.BASE_URL + "AddChannel.jsp";
        return HttpUtil.postRequest(url, map);
    }

    //覆盖用户的所有标签
    public static String AlterChannel(String tag, String userID) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        System.out.println("asd");

        map.put("tag", tag);
        map.put("userID", userID);


        String url = HttpUtil.BASE_URL + "AlterChannel.jsp";
       String s=HttpUtil.postRequest(url, map);
        System.out.println("asd2"+s);
       return s;
    }
    //查看频道的所有用户
    public static String ViewUserByChannel(String tag,String pageNum) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tag", tag);
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "ViewUserByChannel.jsp";
        String body = HttpUtil.postRequest(url, map);
        StringBuffer result = new StringBuffer();
        JSONArray array = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            result.append(obj.toString()).append("\r\n");
        }
        return result.toString();
    }

    //创建频道
    public static String ManageChannel(String action,String tag,String userID,String assortment,String introduction) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("action",action);
        map.put("tag",tag);
        map.put("userID",userID);
        map.put("assortment",assortment);
        map.put("introduction",introduction);
        String url = HttpUtil.BASE_URL + "ManageChannel.jsp";
        return HttpUtil.postRequest(url, map);
    }
    //加入频道
    public static String JoinChannel(String tag,String userID) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("tag",tag);
        map.put("userID",userID);
        String url = HttpUtil.BASE_URL + "JoinChannel.jsp";
        String body = HttpUtil.postRequest(url, map);

        return body;
    }
    //查看频道下面所有人
    public static String ViewUserByChannel(String tag) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("tag",tag);
        String url = HttpUtil.BASE_URL + "ViewUserByChannel.jsp";
        String body = HttpUtil.postRequest(url, map);
        StringBuffer result= new StringBuffer();
        JSONArray array = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            result.append(obj.toString()).append("\r\n");
        }
        return result.toString();
    }
    //查看大频道下面的所有小频道
    public static List<PindaoListItem> ViewChannelByAssortment(String assortment) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("assortment",assortment);
        map.put("pageNum","");
        String url = HttpUtil.BASE_URL +"ViewChannelByAssortment.jsp";
        String body = HttpUtil.postRequest(url, map);
        if(body.contains("false")){
            return null;
        }
        List<PindaoListItem>pindaoListItemList=new ArrayList<PindaoListItem>();
        StringBuffer result= new StringBuffer();
        JSONArray array = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            PindaoListItem pindaoListItem=new PindaoListItem();
            JSONObject obj = array.getJSONObject(i);
            pindaoListItem.setPindaoName(obj.getString("tag"));
            pindaoListItem.setPindaoJianjie(obj.getString("introduction"));
            pindaoListItem.setPindaoNum(obj.getString("members").split(";").length+"");
            pindaoListItemList.add(pindaoListItem);
        }
        return pindaoListItemList;
    }
    public static PinglunItem FindCommentByLike(String id,String style) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);//活动或需求id
        map.put("style",style);//指该评论的类型，比如活动还是需求
        String url = HttpUtil.BASE_URL + "FindCommentByLike.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        StringBuffer result = new StringBuffer();
        if(body.equals("暂无评论"))
            return null;
        else{
            JSONObject obj = new JSONObject(body);
            PinglunItem pinglunItem=new PinglunItem();
            pinglunItem.setPinglunId(obj.getInt("id"));
            pinglunItem.setContent(obj.getString("information"));
            pinglunItem.setTime(obj.getString("date"));
            pinglunItem.setUserId(obj.getInt("authorID"));
            pinglunItem.setHuodongId(obj.getInt("activityID"));
            pinglunItem.setNum(obj.getString("nums"));
            pinglunItem.setName(obj.getString("authorName"));
            //pinglunItem.setName("无名");
            return pinglunItem;
        }
    }

    //查找活动 0热门1附近
    public static List<Huodong> findbyTag(String tag,String listStyle,String pageNum,String longitude,String latitude,String locate) throws Exception {
        // 发送请求
        List<Huodong>huodongList=new ArrayList<Huodong>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("tag",tag);
        map.put("listStyle",listStyle);
        map.put("pageNum", pageNum);
        map.put("longitude",longitude);
        map.put("latitude", latitude);
        map.put("locate",locate);
        String url = HttpUtil.BASE_URL + "ViewActivityByCategory.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        if(body.trim().equals("f")){
            return null;
        }
        StringBuffer result= new StringBuffer();
        JSONArray array = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            Huodong huodong1=new Huodong();
            JSONObject obj = array.getJSONObject(i);
            huodong1.setHuodongId(obj.getInt("id"));
            huodong1.setAuthorId(obj.getInt("authorID")+"");
            huodong1.setTitle(obj.getString("title"));
            huodong1.setContent(obj.getString("neirong"));
            huodong1.setFengmianUrl(obj.getString("picturePath"));
            huodong1.setPinglunNum(obj.getInt("commentAmount"));
            huodong1.setCollected(obj.getInt("collectionAmount"));
            huodong1.setAuthorName(obj.getString("authorName"));
            huodong1.setChuangjianTime(obj.getString("executeTime"));
            if(!obj.getString("picturePath").equals("")){
                huodong1.setFengmianUrl(obj.getString("picturePath"));
            }
            if (listStyle.equals("1")){
                huodong1.setDistance(obj.getString("distance").trim());
            }
            huodongList.add(huodong1);
        }
      return huodongList;
    }
    //发送邀请广播
    public static void sendHuodongYaoqing(String userid,String username,String type,String huodongId,String huodongName,String id){
        String url = HttpUtil.BASE_URL + "SendYaoqing.jsp";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid",userid);
        map.put("username",username);
        map.put("type","3");
        map.put("huodongId",huodongId);
        map.put("huodongName",huodongName);
        map.put("id",id);
        try {
            HttpUtil.postRequest(url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //结束活动
    public static String FinishActivity(String id) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        String url = HttpUtil.BASE_URL + "FinishActivity.jsp";
        return HttpUtil.postRequest(url, map);
    }
    //查看用户参加的所有活动
    public static List<Huodong> ViewActivityByJoin(String style,String id,String pageNum) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        List<Huodong> huodongList=new ArrayList<Huodong>();
        map.put("id", id);
        map.put("style", style);
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "ViewActivityByJoin.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        StringBuffer result = new StringBuffer();
        if("该用户未参加任何活动".equals(body))
            return null;
        else if ("f".equals(body))
            return null;
        else{
            JSONArray array = new JSONArray(body);
            for (int i = 0; i < array.length(); i++) {
                Huodong huodong1=new Huodong();
                JSONObject obj = array.getJSONObject(i);
                huodong1.setHuodongId(obj.getInt("id"));
                huodong1.setAuthorId(obj.getInt("authorID")+"");
                huodong1.setTitle(obj.getString("title"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setFengmianUrl(obj.getString("picturePath"));
                huodong1.setPinglunNum(obj.getInt("commentAmount"));
                huodong1.setCollected(obj.getInt("collectionAmount"));
                huodong1.setAuthorName(obj.getString("authorName"));
                huodong1.setChuangjianTime(obj.getString("publicTime"));
                huodong1.setFlag(obj.getString("flag"));
                huodong1.setJoinMun(obj.getInt("enrollAmount"));
                huodong1.setLocate(obj.getString("locate"));
                huodong1.setAddress(obj.getString("address"));
                huodong1.setRecompense(obj.getString("recompense"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setStyle(obj.getString("style"));
                if(!obj.getString("picturePath").equals("")){
                    huodong1.setFengmianUrl(obj.getString("picturePath"));
                }
                huodongList.add(huodong1);
            }
            return huodongList;
        }
    }
    //查看用户发布的所有活动
    public static List<Huodong> ViewActivityByUser(String style,String id,String pageNum) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        List<Huodong> huodongList=new ArrayList<Huodong>();
        map.put("id", id);
        map.put("style", style);
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "ViewActivityByUser.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        StringBuffer result = new StringBuffer();
        if("该用户无任何发布".equals(body))
            return null;
        else if ("f".equals(body))
            return null;
        else{
            JSONArray array = new JSONArray(body);
            for (int i = 0; i < array.length(); i++) {
                Huodong huodong1=new Huodong();
                JSONObject obj = array.getJSONObject(i);
                huodong1.setHuodongId(obj.getInt("id"));
                huodong1.setSex(obj.getString("conditions"));
                huodong1.setAuthorId(obj.getInt("authorID")+"");
                huodong1.setTitle(obj.getString("title"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setFengmianUrl(obj.getString("picturePath"));
                huodong1.setPinglunNum(obj.getInt("commentAmount"));
                huodong1.setCollected(obj.getInt("collectionAmount"));
                huodong1.setAuthorName(obj.getString("authorName"));
                huodong1.setChuangjianTime(obj.getString("publicTime"));
                huodong1.setFlag(obj.getString("flag"));
                huodong1.setJoinMun(obj.getInt("enrollAmount"));
                huodong1.setLocate(obj.getString("locate"));
                huodong1.setAddress(obj.getString("address"));
                huodong1.setRecompense(obj.getString("recompense"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setStyle(obj.getString("style"));
                if(!obj.getString("picturePath").equals("")){
                    huodong1.setFengmianUrl(obj.getString("picturePath"));
                }
                huodongList.add(huodong1);
            }
            return huodongList;
        }
    }
    //查看用户收藏的所有活动
    public static List<Huodong> ViewActivityByCollection(String style,String id,String pageNum) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        List<Huodong> huodongList=new ArrayList<Huodong>();
        map.put("id", id);
        map.put("style", style);
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "ViewActivityByCollection.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        StringBuffer result = new StringBuffer();
        if("该用户未收藏任何活动".equals(body))
            return null;
        else if ("f".equals(body))
            return null;
        else{
            JSONArray array = new JSONArray(body);
            for (int i = 0; i < array.length(); i++) {
                Huodong huodong1=new Huodong();
                JSONObject obj = array.getJSONObject(i);
                huodong1.setHuodongId(obj.getInt("id"));
                huodong1.setAuthorId(obj.getInt("authorID")+"");
                huodong1.setTitle(obj.getString("title"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setFengmianUrl(obj.getString("picturePath"));
                huodong1.setPinglunNum(obj.getInt("commentAmount"));
                huodong1.setCollected(obj.getInt("collectionAmount"));
                huodong1.setAuthorName(obj.getString("authorName"));
                huodong1.setChuangjianTime(obj.getString("publicTime"));
                huodong1.setFlag(obj.getString("flag"));
                huodong1.setJoinMun(obj.getInt("enrollAmount"));
                huodong1.setLocate(obj.getString("locate"));
                huodong1.setAddress(obj.getString("address"));
                huodong1.setRecompense(obj.getString("recompense"));
                huodong1.setContent(obj.getString("neirong"));
                huodong1.setStyle(obj.getString("style"));
                if(!obj.getString("picturePath").equals("")){
                    huodong1.setFengmianUrl(obj.getString("picturePath"));
                }
                huodongList.add(huodong1);
            }
            return huodongList;
        }
    }
    //发送评论广播
    public static void sendPinglunReply(String userid,String huodongId,String content,String name,String id){
        String url = HttpUtil.BASE_URL + "SendHuifu.jsp";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid",userid);
        map.put("huodongId",huodongId);
        map.put("pinglunContant",content);
        map.put("username",name);
        map.put("id",id);

        try {
            HttpUtil.postRequest(url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //查看用户信息
    public static OtherUser FindUserById(String id) throws Exception {
        Gson gson=new Gson();
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        String url = HttpUtil.BASE_URL + "findUserById.jsp";
        System.out.println(url);
        System.out.println(id);
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        OtherUser otherUser=gson.fromJson(body,OtherUser.class);
        JSONObject obj = new JSONObject(body);
        StringBuffer result = new StringBuffer();
        result.append(obj.toString()).append("\r\n");
        return otherUser;
    }
    //0点赞 1取消赞
    public static String LikesComment(String id, String authorId, String flag) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("authorId", authorId);
        map.put("flag", flag);
        String url = HttpUtil.BASE_URL + "LikesComment.jsp";
        return HttpUtil.postRequest(url, map);
    }

    //发送活动广播
    public static void sendHuodongReceiver(String huodongId,String content,String title,String userId,String userName){
        String url = HttpUtil.BASE_URL + "PushMessage.jsp";
        Map<String, String> map = new HashMap<String, String>();
        map.put("huodongId",huodongId);
        map.put("type","1");
        map.put("userId",userId);
        map.put("userName",userName);
        map.put("content",content);
        map.put("title",title);
        try {
            HttpUtil.postRequest(url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //查看活动所有报名的人
    public static List<OtherUser> ViewEnrolluserByActivityID(String id,String pageNum) throws Exception{
        List<OtherUser>friendList=new ArrayList<OtherUser>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "ViewEnrolluserByActivityID.jsp";
        String body = HttpUtil.postRequest(url, map);
        if(body.contains("无用户报名")){
            return null;
        }
        System.out.println(body);
        Gson gson = new Gson();
        friendList=gson.fromJson(body, new TypeToken<List<OtherUser>>(){}.getType());
        return friendList;
    }

    public static List<Huodong> viewByCategory(String category) throws Exception {
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("category", category);
		String url = HttpUtil.BASE_URL + "ViewActivityByCategory.jsp";
		// 发送请求
		StringBuffer result= new StringBuffer();
        List<Huodong>huodongList=new ArrayList<Huodong>();
		String body=HttpUtil.postRequest(url, map);
		JSONArray array = new JSONArray(body);
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			result.append("id:").append(obj.getInt("id")).append("\t");
            Huodong huodong1=new Huodong();
            huodong1.setHuodongId(obj.getInt("id"));
			result.append("personName:").append(obj.getString("personName")).append("\t");
            huodong1.setAuthorName(obj.getString("personName"));
			result.append("date:").append(obj.getString("date")).append("\r\n");
            huodong1.setTime(obj.getString("date"));
			result.append("category:").append(obj.getString("category")).append("\r\n");
            huodong1.setFenlei(obj.getString("category"));
			if(!obj.getString("picturePath").equals("")){
                huodong1.setFengmianUrl(obj.getString("picturePath"));
            }
			huodongList.add(huodong1);
		}
		return huodongList;
	}
    //获取token
    public static String getToken(String id,String name) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("name",name);

        String url = HttpUtil.BASE_URL + "gettoken.jsp";
        //String url ="http://59.52.131.248:8080/Server/android/"+"gettoken.jsp";
        // 发送请求

        String result= null;
        try {
            result = HttpUtil.postRequest(url, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }
    //创建群组
    public static String createGroup(String userId,String groupId,String groupName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("groupName",groupName);
        map.put("groupId",groupId);

        String url = HttpUtil.BASE_URL + "CreateGroup.jsp";
        // 发送请求

        String result= null;
        try {
            result = HttpUtil.postRequest(url, map);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }
    //创建群组
    public static String joinGroup(String userId,String groupId,String groupName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("groupName",groupName);
        map.put("groupId",groupId);

        String url = HttpUtil.BASE_URL + "JoinGroup.jsp";
        // 发送请求

        String result= null;
        try {
            result = HttpUtil.postRequest(url, map);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }
    //添加广播
    public static String addBroadcast(String information,String activityID,String image,String video,String city,String latitude,String longitude) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("information",information);
        map.put("activityID",activityID);
        map.put("picturePath",image);
        map.put("mediaPath",video);
        map.put("GPS",city+";"+latitude+";"+longitude+";");
        String url = HttpUtil.BASE_URL + "AddBroadcast.jsp";
        return HttpUtil.postRequest(url, map);
    }
    //通过活动id查看所有广播
    public static List<PinglunItem> ViewBroadcastByActivity(String activityID,String pageNum) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("activityID",activityID);
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "ViewBroadcastByActivity.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        if(body==null||body.trim().equals("f")||body.trim().equals("该活动没有广播")){
            return null;
        }
        StringBuffer result= new StringBuffer();
        JSONArray array = new JSONArray(body);
        List<PinglunItem>pinglunItemList=new ArrayList<PinglunItem>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            System.out.println(obj);
            PinglunItem pinglunItem=new PinglunItem();
            pinglunItem.setPinglunId(obj.getInt("id"));
            pinglunItem.setContent(obj.getString("information"));
            pinglunItem.setTime(obj.getString("date"));
            pinglunItem.setUserId(obj.getInt("authorID"));
            pinglunItem.setHuodongId(obj.getInt("activityID"));
            pinglunItem.setGps(obj.getString("GPS"));
            pinglunItem.setCommentNum(obj.getString("commentNum"));

            if(obj.getString("mediaPath")!=null){
                if(!obj.getString("mediaPath").equals("")){
                    String[] media=(obj.getString("mediaPath")).split(";");
                    List<String>mediaList=new ArrayList<>();
                    for(String a:media){
                        mediaList.add(a);
                    }
                    pinglunItem.setMediaPath(mediaList);
                }
            }
            if(obj.getString("picturePath")!=null){
                if(!obj.getString("picturePath").equals("")){
                    String[] media=(obj.getString("picturePath")).split(";");
                    List<String>mediaList=new ArrayList<>();
                    for(String a:media){
                        mediaList.add(a);
                    }
                    pinglunItem.setPicturePath(mediaList);
                }
            }
            pinglunItemList.add(pinglunItem);
        }
        System.out.println(pinglunItemList.size());
        return pinglunItemList;
    }
    //通过用户id查看所有广播
    public static List<PinglunItem> ViewBroadcastByUser(String userID,String pageNum) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("userID",userID);
        map.put("pageNum",pageNum);
        String url = HttpUtil.BASE_URL + "ViewBroadcastByUser.jsp";
        String body = HttpUtil.postRequest(url, map);
        System.out.println(body);
        if(body==null||body.trim().equals("f")||body.trim().equals("该活动没有广播")){
            return null;
        }
        StringBuffer result= new StringBuffer();
        JSONArray array = new JSONArray(body);
        List<PinglunItem>pinglunItemList=new ArrayList<PinglunItem>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            System.out.println(obj);
            PinglunItem pinglunItem=new PinglunItem();
            pinglunItem.setPinglunId(obj.getInt("id"));
            pinglunItem.setContent(obj.getString("information"));
            pinglunItem.setTime(obj.getString("date"));
            pinglunItem.setUserId(obj.getInt("authorID"));
            pinglunItem.setHuodongId(obj.getInt("activityID"));
            pinglunItemList.add(pinglunItem);
            if(obj.getString("mediaPath")!=null){
                if(!obj.getString("mediaPath").equals("")){
                    String[] media=(obj.getString("mediaPath")).split(";");
                    List<String>mediaList=new ArrayList<>();
                    for(String a:media){
                        mediaList.add(a);
                    }
                    pinglunItem.setMediaPath(mediaList);
                }
            }
            if(obj.getString("picturePath")!=null){
                if(!obj.getString("picturePath").equals("")){
                    String[] media=(obj.getString("picturePath")).split(";");
                    List<String>mediaList=new ArrayList<>();
                    for(String a:media){
                        mediaList.add(a);
                    }
                    pinglunItem.setPicturePath(mediaList);
                }
            }
        }
        System.out.println(pinglunItemList.size());
        return pinglunItemList;
    }
}