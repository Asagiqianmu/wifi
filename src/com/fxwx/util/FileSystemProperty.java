package com.fxwx.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 文件系统读取和保存
 * @author dengfei E-mail:dengfei200857@163.com
 * @time 2018年1月15日 下午10:00:37
 */
public class FileSystemProperty {
	private static final Log log = Logs.getLog(FileSystemProperty.class);

	// 传参获取值
	public static String propertyName(String name) {
		ResourceBundle rb = ResourceBundle.getBundle("Appconfig", Locale.getDefault());
		return rb.getString(name);
	}

	public static void updateUrl(String value) {
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String filename = "Appconfig.properties";
		Properties pro = new Properties();
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(path + filename));
			pro.load(in);
			FileOutputStream file = new FileOutputStream(path + filename);
			// 重新写入配置文件
			pro.put("url", "https://" + value);
			pro.store(file, "系统配置修改"); // 这句话表示重新写入配置文件
			file.close();
			in.close();
		} catch (IOException e) {
			log.error("修改域名配置文件失败" + e);
		}

	}

	private static PropertiesConfiguration propConfig;

	/**
	 * 自动保存
	 */
	private static boolean autoSave = true;

	private FileSystemProperty() {
	}

	// public static Config getInstance(String propertiesFile) {
	//
	// }
	static {
		// 执行初始化
		init("Appconfig.properties");
	}

	/**
	 * 初始化
	 *
	 * @param propertiesFile
	 * @see
	 */
	private static void init(String propertiesFile) {
		try {
			propConfig = new PropertiesConfiguration(propertiesFile);
			// 自动重新加载
			propConfig.setReloadingStrategy(new FileChangedReloadingStrategy());
			// 自动保存
			propConfig.setAutoSave(autoSave);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据Key获得对应的value
	 *
	 * @param key
	 * @return
	 * @see
	 */
	public static Object getValue(String key) {
		return propConfig.getProperty(key);
	}

	/**
	 * 设置属性
	 *
	 * @param key
	 * @param value
	 * @see
	 */
	public static void setProperty(String key, String value) {
		log.error(propConfig);
		propConfig.setProperty(key, value);
	}

	public static void main(String[] args) throws IOException {
		// SetSystemProperty.updateUrl("portal.kdfwifi.com");
		System.out.println(FileSystemProperty.propertyName("ios_download_url"));

	}
}