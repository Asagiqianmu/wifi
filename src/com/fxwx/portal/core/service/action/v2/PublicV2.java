/*     */ package com.fxwx.portal.core.service.action.v2;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import org.apache.commons.lang.math.NumberUtils;
/*     */ import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;
import com.fxwx.portal.core.service.utils.Authenticator;
import com.fxwx.portal.core.service.utils.PortalUtil;
/*     */ 
/*     */ public class PublicV2
/*     */ {
/*  17 */   private static Config config = Config.getInstance();
/*  18 */   private static Logger log = Logger.getLogger(PublicV2.class);
/*     */ 
/*     */   public static boolean choose(int type, byte[] Req_Quit, int timeout_Sec, String Bas_IP, int bas_PORT, String sharedSecret)
/*     */   {
/*  22 */     if (type == 0) {
/*  23 */       return offline(Req_Quit, timeout_Sec, Bas_IP, bas_PORT, 
/*  24 */         sharedSecret);
/*     */     }
/*  26 */     return timeoutAffirm(Req_Quit, Bas_IP, bas_PORT, 
/*  27 */       sharedSecret);
/*     */   }
/*     */ 
/*     */   public static boolean offline(byte[] Req_Quit, int timeout_Sec, String Bas_IP, int bas_PORT, String sharedSecret)
/*     */   {
/*  40 */     DatagramSocket dataSocket = null;
/*  41 */     byte[] ACK_Data = new byte[100];
/*  42 */     byte[] BBuff = new byte[16];
/*  43 */     Req_Quit[15] = 1;
/*  44 */     for (int i = 0; i < 16; i++) {
/*  45 */       BBuff[i] = Req_Quit[i];
/*     */     }
/*     */ 
/*  48 */     byte[] Attrs = new byte[6];
/*  49 */     byte[] BasIP = new byte[4];
/*  50 */     String[] basips = Bas_IP.split("[.]");
/*     */ 
/*  52 */     for (int i = 0; i < 4; i++) {
/*  53 */       int m = NumberUtils.toInt(basips[i]);
/*  54 */       byte b = (byte)m;
/*  55 */       BasIP[i] = b;
/*     */     }
/*  57 */     Attrs[0] = 10;
/*  58 */     Attrs[1] = 6;
/*  59 */     Attrs[2] = BasIP[0];
/*  60 */     Attrs[3] = BasIP[1];
/*  61 */     Attrs[4] = BasIP[2];
/*  62 */     Attrs[5] = BasIP[3];
/*     */ 
/*  64 */     byte[] Authen = Authenticator.MK_Authen(BBuff, Attrs, sharedSecret);
/*     */ 
/*  66 */     for (int i = 0; i < 16; i++) {
/*  67 */       Req_Quit[(16 + i)] = Authen[i];
/*     */     }
/*     */ 
/*  70 */     byte[] Req_Quit_F = new byte[Req_Quit.length + 6];
/*  71 */     for (int i = 0; i < Req_Quit.length; i++) {
/*  72 */       Req_Quit_F[i] = Req_Quit[i];
/*     */     }
/*  74 */     for (int i = 0; i < 6; i++) {
/*  75 */       Req_Quit_F[(32 + i)] = Attrs[i];
/*     */     }
/*     */     try
/*     */     {
/*  83 */       dataSocket = new DatagramSocket();
/*  84 */       DatagramPacket requestPacket = new DatagramPacket(Req_Quit_F, 38, 
/*  85 */         InetAddress.getByName(Bas_IP), bas_PORT);
/*  86 */       dataSocket.send(requestPacket);
/*  87 */       DatagramPacket receivePacket = new DatagramPacket(ACK_Data, 100);
/*  88 */       dataSocket.setSoTimeout(timeout_Sec * 1000);
/*  89 */       dataSocket.receive(receivePacket);
/*  90 */       ACK_Data = new byte[receivePacket.getLength()];
/*  91 */       for (int i = 0; i < ACK_Data.length; i++) {
/*  92 */         ACK_Data[i] = receivePacket.getData()[i];
/*     */       }
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 100 */         log.info("发送下线请求无响应!!!");
/* 103 */       return false;
/*     */     } finally {
/* 105 */       dataSocket.close();
/*     */     }
/* 107 */     if ((ACK_Data[14] & 0xFF) == 1) {
/* 109 */         log.info("发送下线请求被拒绝!!!");
/* 112 */       return false;
/* 113 */     }if ((ACK_Data[14] & 0xFF) == 2) {
/* 115 */         log.info("发送下线请求出现错误!!!");
/* 118 */       return false;
/*     */     }
/* 121 */       log.info("请求下线成功！！");
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean timeoutAffirm(byte[] Req_Quit, String Bas_IP, int bas_PORT, String sharedSecret)
/*     */   {
/* 136 */     DatagramSocket dataSocket = null;
/* 137 */     byte[] BBuff = new byte[16];
/* 138 */     Req_Quit[14] = 1;
/* 139 */     Req_Quit[15] = 1;
/* 140 */     for (int i = 0; i < 16; i++) {
/* 141 */       BBuff[i] = Req_Quit[i];
/*     */     }
/*     */ 
/* 144 */     byte[] Attrs = new byte[6];
/* 145 */     byte[] BasIP = new byte[4];
/* 146 */     String[] basips = Bas_IP.split("[.]");
/*     */ 
/* 148 */     for (int i = 0; i < 4; i++) {
/* 149 */       int m = NumberUtils.toInt(basips[i]);
/* 150 */       byte b = (byte)m;
/* 151 */       BasIP[i] = b;
/*     */     }
/* 153 */     Attrs[0] = 10;
/* 154 */     Attrs[1] = 6;
/* 155 */     Attrs[2] = BasIP[0];
/* 156 */     Attrs[3] = BasIP[1];
/* 157 */     Attrs[4] = BasIP[2];
/* 158 */     Attrs[5] = BasIP[3];
/*     */ 
/* 160 */     byte[] Authen = Authenticator.MK_Authen(BBuff, Attrs, 
/* 161 */       sharedSecret);
/* 162 */     for (int i = 0; i < 16; i++) {
/* 163 */       Req_Quit[(16 + i)] = Authen[i];
/*     */     }
/*     */ 
/* 166 */     byte[] Req_Quit_F = new byte[Req_Quit.length + 6];
/* 167 */     for (int i = 0; i < Req_Quit.length; i++) {
/* 168 */       Req_Quit_F[i] = Req_Quit[i];
/*     */     }
/* 170 */     for (int i = 0; i < 6; i++) {
/* 171 */       Req_Quit_F[(32 + i)] = Attrs[i];
/*     */     }
/*     */     try
/*     */     {
/* 175 */       dataSocket = new DatagramSocket();
/* 176 */       DatagramPacket requestPacket = new DatagramPacket(Req_Quit_F, 38, 
/* 177 */         InetAddress.getByName(Bas_IP), bas_PORT);
/* 178 */       dataSocket.send(requestPacket);
/* 183 */       return true;
/*     */     } catch (IOException e) {
/* 186 */         log.info("请求超时回复报文发生未知错误!");
/* 189 */       return false;
/*     */     } finally {
/* 191 */       dataSocket.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           F:\ghos\OpenPortalServer V3.3.5.6 Stable 2016-1-16\OpenPortalServer\webapps\ROOT\WEB-INF\classes\
 * Qualified Name:     com.leeson.portal.core.service.action.v2.PublicV2
 * JD-Core Version:    0.6.2
 */