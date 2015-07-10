package com.example.topnewgrid.choosephotos.choosephotos;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContent
{
	private static Map<String, Object> map = new HashMap<String, Object>();
	
	public static void add(String key,Object value)
	{
		map.put(key, value);
	}
	
	public static Map<String, Object> getMap()
	{
		return map;
	}
	
	public static Object get(String key)
	{
		return map.get(key);
	}
	
	public static void remove(String key)
	{
		map.remove(key);
	}
}
