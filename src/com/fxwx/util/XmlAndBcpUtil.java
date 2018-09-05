package com.fxwx.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlAndBcpUtil {
	public static final String xmlFileName = "145-440300-1499308220-00011-WA_AUTH_FJ_0001-0.xml";

	// 数据采集的系统类型，，固定填写为145
	public static final int System_Type = 145;
	// 数据产生源标识
	public static final String Data_Source_Identifier = "440300";
	// 数据传输目的标识
	public static final String Data_Transmission_Identifier = "440300";
	// 组织机构代码
	public static final String Organization_Code = "91110228076584524H";
	// 序列号
	public static int Serial_Number1 = 0;
	// 序列号
	public static int Serial_Number2 = 0;
	// 数据集代码
	public static final String Data_List_Code = "WA_AUTH_FJ_0001";
	// 结构化/非结构化标识
	public static final int Structuring = 0;

	// 认证类型
	public static final String Auth_Type = "1020004";

	// 文件目录
	public static final String path = "/home/rz_log/";
//	public static final String path = "C:\\Users\\Administrator\\Desktop\\rz_log\\";
	// 文件目录
	public static final String zip_path = "/home/rz_log_zip/";
//	public static final String zip_path = "C:\\Users\\Administrator\\Desktop\\rz_log_zip\\";

	// 组装XML/BCP文件名
	public synchronized static String getDatafileName() {
		if (Serial_Number1 >= 99999) {
			Serial_Number1 = 0;
		}
		Serial_Number1++;
		String str = "" + Serial_Number1;
		int a = 5 - str.length();
		switch (a) {
		case 1:
			str = "0" + Serial_Number1;
			break;
		case 2:
			str = "00" + Serial_Number1;
			break;
		case 3:
			str = "000" + Serial_Number1;
			break;
		case 4:
			str = "0000" + Serial_Number1;
			break;
		}
		long time = System.currentTimeMillis() / 1000;
		str = System_Type + "-" + Data_Source_Identifier + "-" + time + "-" + str + "-" + Data_List_Code + "-" + Structuring;
		return str;
	}

	// 组装ZIP文件名
	public synchronized static String getZIPfileName() {
		if (Serial_Number2 >= 99999) {
			Serial_Number2 = 0;
		}
		Serial_Number2++;
		String str = "" + Serial_Number2;
		int a = 5 - str.length();
		switch (a) {
		case 1:
			str = "0" + Serial_Number2;
			break;
		case 2:
			str = "00" + Serial_Number2;
			break;
		case 3:
			str = "000" + Serial_Number2;
			break;
		case 4:
			str = "0000" + Serial_Number2;
			break;
		}
		long time = System.currentTimeMillis() / 1000;
		str = System_Type + "-" + Organization_Code + "-" + Data_Source_Identifier + "-" + Data_Transmission_Identifier + "-" + time + "-" + str;
		return str;
	}

	public synchronized static Document ReadXML(String bcpFileName, int Num) {
		Document document = null;
		try {

			// 1。获取DOM 解析器的工厂实例。
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// 2。获得具体的DOM解析器。
			DocumentBuilder builder = factory.newDocumentBuilder();

			// 3。获取文件
			document = builder.parse(new File(XmlAndBcpUtil.class.getResource("/").getPath() + "com/nutz/util/" + xmlFileName));

			// 4。获取根元素
			Element root = document.getDocumentElement();
			// System.out.println("root=" + root.getTagName());
			// 5。获取根节点[有多个节点]
			NodeList list = root.getElementsByTagName("ITEM");
			// System.out.println("ITEM=" + root.getAttribute("key"));
			for (int i = 0; i < list.getLength(); i++) {
				// Node lan = list.item(i);
				// System.out.println("id="+lan.getNodeType());
				Element item = (Element) list.item(i);
				// System.out.println("key=" + item.getAttribute("key"));
				// 数据采集地
				if (item.getAttribute("key").equals("F010008")) {
					item.setAttribute("val", "440300");
				}
				// BCP文件名
				if (item.getAttribute("key").equals("H010020")) {
					item.setAttribute("val", bcpFileName + ".bcp");
				}
				// 记录数
				if (item.getAttribute("key").equals("I010034")) {
					item.setAttribute("val", Num + "");
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return document;
	}

	/**
	 * Document 保存为 XML 文件
	 * 
	 * @param doc
	 *            Document对象
	 * @param path
	 *            文件路径
	 */
	public synchronized static boolean doc2XML(Document doc, String path) {
		boolean flag = false;
		try {
			Source source = new DOMSource(doc);
			Result result = new StreamResult(new File(path));
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 写BCP文件 按字符缓冲写入 BufferedWriter and BufferedOutputStream
	 * 
	 * @param str
	 *            写入字符串
	 * @param filepath
	 *            文件路径
	 */
	public synchronized static boolean writeBCP(String str, String filepath) {
		boolean flag = false;
		File file = new File(filepath);
		BufferedOutputStream bos = null;
		OutputStreamWriter writer = null;
		BufferedWriter bw = null;
		try {
			OutputStream os = new FileOutputStream(file);
			bos = new BufferedOutputStream(os);
			writer = new OutputStreamWriter(bos);
			bw = new BufferedWriter(writer);
			bw.write(str);
			bw.flush();
			flag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 构建XML文件
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月24日 上午12:39:58
	 * @param path
	 * @param Auth_Type
	 * @param Auth_Account
	 * @param userMac
	 * @param apMac
	 */
	public synchronized static boolean buildXML(int Num,String fileName) {
		judeDirExists(new File(path));
		Document doc = ReadXML(fileName, Num);
		String xmlpath = path + fileName + ".xml";
		return doc2XML(doc, xmlpath);
	}
	
	/**
	 * 构建BCP文件
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月25日 上午10:42:37
	 * @param fileName
	 * @param Auth_Account
	 * @param userMac
	 * @param apMac
	 * @return
	 */
	public synchronized static boolean buildBCP(String fileName, String bcpStr) {
		judeDirExists(new File(path));
		String bcppath = path + fileName + ".bcp";
		return writeBCP(bcpStr, bcppath);
	}

	/**
	 * 压缩文件成zip文件
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月23日 下午11:58:39
	 * @param srcPath
	 * @param paths
	 */
	public synchronized static boolean writeZipFile(String srcPath, String[] paths) {
		boolean flag = false;
		ZipOutputStream zos = null;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		try {
			File zipFile = new File(srcPath);
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(new BufferedOutputStream(fos));
			byte[] bufs = new byte[1024 * 10];
			for (String str : paths) {
				File file = new File(str);
				String filename = str.substring(path.length());
				System.out.println("---newpath-------"+zip_path);
				System.out.println("---filename-------"+filename);
				if(Copy(file, zip_path+filename)){
					// 创建ZIP实体，并添加进压缩包
					ZipEntry zipEntry = new ZipEntry(filename);
					zos.putNextEntry(zipEntry);
					// 读取待压缩的文件并写进压缩包里
					FileInputStream fis = new FileInputStream(zip_path+filename);
					bis = new BufferedInputStream(fis, 1024 * 10);
					int read = 0;
					while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
						zos.write(bufs, 0, read);
					}
				}
			}
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (null != bis)
					bis.close();
				if (null != zos)
					zos.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return flag;
	}

	public static boolean Copy(File oldfile, String newPath) {
		boolean flag = false;
		try {
			int bytesum = 0;
			int byteread = 0;
			// File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldfile);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
//					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 判断文件夹是否存在
	 * 
	 * @author dengfei E-mail:dengfei200857@163.com
	 * @time 2017年10月24日 上午9:00:33
	 * @param file
	 */
	public synchronized static boolean judeDirExists(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				System.out.println("dir exists");
			} else {
				System.out.println("the same name file exists, can not create dir");
			}
		} else {
			System.out.println("dir not exists, create it ...");
			file.mkdir();
		}
		return true;
	}

	public static void main(String[] args) {
//		int Num = 1;
//		String Auth_Account = "15972935811";
//		String userMac = "20:5d:47:a2:0a:37";
//		String apMac = "74:ac:5f:b5:cc:2c";
//		String fileName = getDatafileName();
//
//		String bcpStr = Auth_Type + "\t" + Auth_Account + "\t" + userMac + "\t" + apMac + "\n";
//		judeDirExists(new File(path));
//		Document doc = ReadXML(fileName, Num);
//		doc2XML(doc, path + fileName + ".xml");
//		writeBCP(bcpStr, path + fileName + ".bcp");
//		String[] paths = new String[2];
//		paths[0] = path + fileName + ".xml";
//		paths[1] = path + fileName + ".bcp";
////		String[] paths = new String[]{"C:\\Users\\Administrator\\Desktop\\rz_log\\145-440300-1508820778-00001-WA_AUTH_FJ_0001-0.bcp",
////				"C:\\Users\\Administrator\\Desktop\\rz_log\\145-440300-1508820778-00001-WA_AUTH_FJ_0001-0.xml",
////				"C:\\Users\\Administrator\\Desktop\\rz_log\\145-440300-1508829785-00001-WA_AUTH_FJ_0001-0.bcp",
////				"C:\\Users\\Administrator\\Desktop\\rz_log\\145-440300-1508829785-00001-WA_AUTH_FJ_0001-0.xml"};
//		if(XmlAndBcpUtil.judeDirExists(new File(XmlAndBcpUtil.zip_path))){
//			fileName = XmlAndBcpUtil.getZIPfileName();
//			String zip_srcPath = XmlAndBcpUtil.zip_path+fileName+".zip";
//			writeZipFile(zip_srcPath, paths);
//		}
	}
}
