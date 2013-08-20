package com.tool;

/**
 * 
 * @description 网络连接URL
 * @version 1.0
 * @author Mrluo
 * @date 2013-7-9 下午2:09:42
 */
public class CommonURL {
	// 查询 0 - 12 条 qq139811679.vicp.cc/ZufuService/getService?
	public static final String MALL_TJ_URL = "http://zufu.mrluo.net/json.php?type=page&offone=0&offtwo=12";
	// 查询所有数据链接
	public static final String MALL_SJ_URL = "http://zufu.mrluo.net/json.php?type=all";
	// 根据分页查询数据
	public static final String MALL_PAGE_URL = "http://zufu.mrluo.net/json.php?type=page";
	// 添加祝福信息
	public static final String MALL_ADD_URL = "http://zufu.mrluo.net/json.php?type=add";
	// 分类查询
	public static final String MALL_SELECT_URL = "http://zufu.mrluo.net/json.php?type=select&cat=";
	// 音乐地址
	public final static String SHENGRI = "http://mrluo.net/mp3/shengri.mp3";
	public final static String YOUQIN = "http://mrluo.net/mp3/pengyou.mp3";
	public final static String BIAOBAI = "http://mrluo.net/mp3/aiqing.mp3";
	public final static String QIANYI = "http://mrluo.net/mp3/duibuqi.mp3";
	// 获取版本信息xml
	public static String WebXml = "http://mrluo.net/xml/versiom.xml";
	// 查询URL
	public static final String MALL_Search_URL = "http://zufu.mrluo.net/json.php?type=search";
}
