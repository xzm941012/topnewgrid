package demo;

import java.io.Serializable;

public class MyLocation implements Serializable{
	 // private static final long serialVersionUID = -7060210544600464481L; 
	public Point point;//坐标，(latLng.longitude,latLng.latitude)
	public String Poi;//在地理信息系统中,一个POI可以是一栋房子、一个商铺、一个邮筒、一个公交站等
	public String Adress;//地址
	public String Poi_Adress;//Poi+Adress
	//BDLocation BdLocation;	//保存了首次定位时的地理信息
}

