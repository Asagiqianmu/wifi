/*     */ package com.fxwx.portal.core.service.action.v2.chap;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import org.apache.commons.lang.math.NumberUtils;
/*     */ import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;
import com.fxwx.portal.core.service.utils.Authenticator;
import com.fxwx.portal.core.service.utils.ChapPassword;
import com.fxwx.portal.core.service.utils.PortalUtil;
/*     */ 
/*     */ public class Chap_Auth_V2
/*     */ {
/*  26 */   private static Config config = Config.getInstance();
/*  27 */   private static Logger log = Logger.getLogger(Chap_Auth_V2.class);
/*     */ 
/*     */   public static byte[] auth(String Bas_IP, int bas_PORT, int timeout_Sec, String in_username, String in_password, byte[] SerialNo, byte[] UserIP, byte[] ReqID, byte[] Challenge, String sharedSecret)
/*     */   {
/*  33 */     byte[] ChapPass = new byte[16];
/*  34 */     byte[] Username = in_username.getBytes();
/*  35 */     byte[] password = in_password.getBytes();
/*     */     try {
/*  37 */       ChapPass = ChapPassword.MK_ChapPwd(ReqID, Challenge, password);
/*     */     } catch (UnsupportedEncodingException e) {
/*  39 */       e.printStackTrace();
/*     */     }
/*  41 */     return Req_Auth(Username, ChapPass, SerialNo, UserIP, 
/*  42 */       ReqID, timeout_Sec, Bas_IP, bas_PORT, sharedSecret);
/*     */   }
/*     */ 
/*     */   public static byte[] Req_Auth(byte[] Username, byte[] ChapPass, byte[] SerialNo, byte[] UserIP, byte[] ReqID, int timeout_Sec, String Bas_IP, int bas_PORT, String sharedSecret)
/*     */   {
/*  50 */     DatagramSocket dataSocket = null;
/*     */ 
/*  52 */     byte[] ErrorInfo = new byte[1];
/*     */ 
/*  54 */     byte[] authbuff = new byte[4 + Username.length + ChapPass.length + 6];
/*  55 */     authbuff[0] = 1;
/*  56 */     authbuff[1] = ((byte)(Username.length + 2));
/*  57 */     for (int i = 0; i < Username.length; i++) {
/*  58 */       authbuff[(2 + i)] = Username[i];
/*     */     }
/*  60 */     authbuff[(2 + Username.length)] = 4;
/*  61 */     authbuff[(3 + Username.length)] = ((byte)(ChapPass.length + 2));
/*  62 */     for (int i = 0; i < ChapPass.length; i++) {
/*  63 */       authbuff[(4 + Username.length + i)] = ChapPass[i];
/*     */     }
/*     */ 
/*  66 */     byte[] BasIP = new byte[4];
/*  67 */     String[] basips = Bas_IP.split("[.]");
/*     */ 
/*  69 */     for (int i = 0; i < 4; i++) {
/*  70 */       int m = NumberUtils.toInt(basips[i]);
/*  71 */       byte b = (byte)m;
/*  72 */       BasIP[i] = b;
/*     */     }
/*  74 */     authbuff[(4 + Username.length + ChapPass.length)] = 10;
/*  75 */     authbuff[(4 + Username.length + ChapPass.length + 1)] = 6;
/*  76 */     authbuff[(4 + Username.length + ChapPass.length + 2)] = BasIP[0];
/*  77 */     authbuff[(4 + Username.length + ChapPass.length + 3)] = BasIP[1];
/*  78 */     authbuff[(4 + Username.length + ChapPass.length + 4)] = BasIP[2];
/*  79 */     authbuff[(4 + Username.length + ChapPass.length + 5)] = BasIP[3];
/*     */ 
/*  82 */     byte[] Req_Auth = new byte[36 + Username.length + ChapPass.length + 6];
/*  83 */     Req_Auth[0] = 2;
/*  84 */     Req_Auth[1] = 3;
/*  85 */     Req_Auth[2] = 0;
/*  86 */     Req_Auth[3] = 0;
/*  87 */     Req_Auth[4] = SerialNo[0];
/*  88 */     Req_Auth[5] = SerialNo[1];
/*  89 */     Req_Auth[6] = ReqID[0];
/*  90 */     Req_Auth[7] = ReqID[1];
/*  91 */     Req_Auth[8] = UserIP[0];
/*  92 */     Req_Auth[9] = UserIP[1];
/*  93 */     Req_Auth[10] = UserIP[2];
/*  94 */     Req_Auth[11] = UserIP[3];
/*  95 */     Req_Auth[12] = 0;
/*  96 */     Req_Auth[13] = 0;
/*  97 */     Req_Auth[14] = 0;
/*  98 */     Req_Auth[15] = 3;
/*  99 */     byte[] BBuff = new byte[16];
/* 100 */     for (int i = 0; i < 16; i++) {
/* 101 */       BBuff[i] = Req_Auth[i];
/*     */     }
/* 103 */     byte[] Authen = Authenticator.MK_Authen(BBuff, authbuff, sharedSecret);
/* 104 */     for (int i = 0; i < 16; i++) {
/* 105 */       Req_Auth[(16 + i)] = Authen[i];
/*     */     }
/* 107 */     for (int i = 0; i < authbuff.length; i++) {
/* 108 */       Req_Auth[(32 + i)] = authbuff[i];
/*     */     }
/*     */     try
/*     */     {
/* 115 */       dataSocket = new DatagramSocket();
/* 116 */       DatagramPacket requestPacket = new DatagramPacket(Req_Auth, 
/* 117 */         Req_Auth.length, InetAddress.getByName(Bas_IP), bas_PORT);
/* 118 */       dataSocket.send(requestPacket);
/* 119 */       byte[] ACK_Auth_Data = new byte[100];
/* 120 */       DatagramPacket receivePacket = new DatagramPacket(ACK_Auth_Data, ACK_Auth_Data.length);
/* 121 */       dataSocket.setSoTimeout(timeout_Sec * 1000);
/* 122 */       dataSocket.receive(receivePacket);
/* 123 */       ACK_Auth_Data = new byte[receivePacket.getLength()];
/* 124 */       for (int i = 0; i < ACK_Auth_Data.length; i++) {
/* 125 */         ACK_Auth_Data[i] = receivePacket.getData()[i];
/*     */       }
/*     */ 
/* 131 */       if (((ACK_Auth_Data[14] & 0xFF) == 0) || ((ACK_Auth_Data[14] & 0xFF) == 2))
/*     */       {
/*     */ 
/* 139 */           log.info("认证成功！！准备发送AFF_ACK_AUTH!!!");
/* 141 */         return AFF_Ack_Auth(SerialNo, UserIP, ReqID, 
/* 142 */           Bas_IP, bas_PORT, sharedSecret, BBuff);
/*     */       }
/* 144 */       if ((ACK_Auth_Data[14] & 0xFF) == 1) {
/* 146 */           log.info("发送认证请求被拒绝!!!");
/* 149 */         ErrorInfo[0] = 21;
/* 150 */         return ErrorInfo;
/* 151 */       }if ((ACK_Auth_Data[14] & 0xFF) == 2) {
/* 153 */           log.info("发送用户认证请求连接已建立!!!");
/* 156 */         ErrorInfo[0] = 22;
/* 157 */         return ErrorInfo;
/* 158 */       }if ((ACK_Auth_Data[14] & 0xFF) == 3) {
/* 160 */           log.info("系统繁忙，请稍后再试!!!");
/* 163 */         ErrorInfo[0] = 23;
/* 164 */         return ErrorInfo;
/* 165 */       }if ((ACK_Auth_Data[14] & 0xFF) == 4) {
/* 167 */           log.info("发送认证请求失败!!!");
/* 170 */         ErrorInfo[0] = 24;
/* 171 */         return ErrorInfo;
/*     */       }
/* 177 */       ErrorInfo[0] = 2;
/* 178 */       return ErrorInfo;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */       byte[] arrayOfByte1;
/* 182 */         log.info("发送认证请求无响应!!!");
/* 185 */       ErrorInfo[0] = 2;
/* 186 */       return ErrorInfo;
/*     */     } finally {
/* 188 */       dataSocket.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] AFF_Ack_Auth(byte[] SerialNo, byte[] UserIP, byte[] ReqID, String Bas_IP, int bas_PORT, String sharedSecret, byte[] BBuff)
/*     */   {
/* 197 */     DatagramSocket dataSocket = null;
/*     */ 
/* 199 */     byte[] ErrorInfo = new byte[1];
/*     */ 
/* 201 */     byte[] AFF_Ack_Auth_Data = new byte[38];
/*     */ 
/* 203 */     AFF_Ack_Auth_Data[0] = 2;
/* 204 */     AFF_Ack_Auth_Data[1] = 7;
/* 205 */     AFF_Ack_Auth_Data[2] = 0;
/* 206 */     AFF_Ack_Auth_Data[3] = 0;
/* 207 */     AFF_Ack_Auth_Data[4] = SerialNo[0];
/* 208 */     AFF_Ack_Auth_Data[5] = SerialNo[1];
/* 209 */     AFF_Ack_Auth_Data[6] = ReqID[0];
/* 210 */     AFF_Ack_Auth_Data[7] = ReqID[1];
/* 211 */     AFF_Ack_Auth_Data[8] = UserIP[0];
/* 212 */     AFF_Ack_Auth_Data[9] = UserIP[1];
/* 213 */     AFF_Ack_Auth_Data[10] = UserIP[2];
/* 214 */     AFF_Ack_Auth_Data[11] = UserIP[3];
/* 215 */     AFF_Ack_Auth_Data[12] = 0;
/* 216 */     AFF_Ack_Auth_Data[13] = 0;
/* 217 */     AFF_Ack_Auth_Data[14] = 0;
/* 218 */     AFF_Ack_Auth_Data[15] = 1;
/*     */ 
/* 220 */     for (int i = 0; i < 16; i++) {
/* 221 */       BBuff[i] = AFF_Ack_Auth_Data[i];
/*     */     }
/* 223 */     byte[] Attrs = new byte[6];
/*     */ 
/* 225 */     byte[] BasIP = new byte[4];
/* 226 */     String[] basips = Bas_IP.split("[.]");
/*     */ 
/* 228 */     for (int i = 0; i < 4; i++) {
/* 229 */       int m = NumberUtils.toInt(basips[i]);
/* 230 */      byte b = (byte)m;
/* 231 */       BasIP[i] = b;
/*     */     }
/* 233 */     Attrs[0] = 10;
/* 234 */     Attrs[1] = 6;
/* 235 */     Attrs[2] = BasIP[0];
/* 236 */     Attrs[3] = BasIP[1];
/* 237 */     Attrs[4] = BasIP[2];
/* 238 */     Attrs[5] = BasIP[3];
/*     */ 
/* 240 */     byte[] BAuthen = Authenticator.MK_Authen(BBuff, Attrs, sharedSecret);
/* 241 */     for (int i = 0; i < 16; i++) {
/* 242 */       AFF_Ack_Auth_Data[(16 + i)] = BAuthen[i];
/*     */     }
/* 244 */     for (int i = 0; i < 6; i++) {
/* 245 */       AFF_Ack_Auth_Data[(32 + i)] = Attrs[i];
/*     */     }
/*     */     try
/*     */     {
/* 253 */       dataSocket = new DatagramSocket();
/*     */ 
/* 255 */       DatagramPacket requestPacket = new DatagramPacket(
/* 256 */         AFF_Ack_Auth_Data, 38, InetAddress.getByName(Bas_IP), 
/* 257 */         bas_PORT);
/* 258 */       dataSocket.send(requestPacket);
/* 260 */         log.info("发送AFF_Ack_Auth认证成功回复报文成功！！");
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 265 */         log.info("发送AFF_Ack_Auth认证成功回复出错！！");
/*     */     }
/*     */     finally
/*     */     {
/* 269 */       dataSocket.close();
/*     */     }
/* 271 */     ErrorInfo[0] = 20;
/* 272 */     return ErrorInfo;
/*     */   }
/*     */ }

/* Location:           F:\ghos\OpenPortalServer V3.3.5.6 Stable 2016-1-16\OpenPortalServer\webapps\ROOT\WEB-INF\classes\
 * Qualified Name:     com.leeson.portal.core.service.action.v2.chap.Chap_Auth_V2
 * JD-Core Version:    0.6.2
 */