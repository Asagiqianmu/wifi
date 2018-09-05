/*     */ package com.fxwx.portal.core.service.action.v2.pap;
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
/*     */ public class PAP_Auth_V2
/*     */ {
/*  24 */   private static Config config = Config.getInstance();
/*  25 */   private static Logger log = Logger.getLogger(PAP_Auth_V2.class);
/*     */ 
/*     */   public static boolean auth(String Bas_IP, int bas_PORT, int timeout_Sec, String in_username, String in_password, byte[] SerialNo, byte[] UserIP, String sharedSecret)
/*     */   {
/*  31 */     byte[] Username = in_username.getBytes();
/*  32 */     byte[] password = in_password.getBytes();
/*  33 */     byte[] authbuff = new byte[4 + Username.length + password.length + 6];
/*  34 */     authbuff[0] = 1;
/*  35 */     authbuff[1] = ((byte)(Username.length + 2));
/*  36 */     for (int i = 0; i < Username.length; i++) {
/*  37 */       authbuff[(2 + i)] = Username[i];
/*     */     }
/*  39 */     authbuff[(2 + Username.length)] = 2;
/*  40 */     authbuff[(3 + Username.length)] = ((byte)(password.length + 2));
/*  41 */     for (int i = 0; i < password.length; i++) {
/*  42 */       authbuff[(4 + Username.length + i)] = password[i];
/*     */     }
/*     */ 
/*  45 */     byte[] BasIP = new byte[4];
/*  46 */     String[] basips = Bas_IP.split("[.]");
/*     */ 
/*  48 */     for (int i = 0; i < 4; i++) {
/*  49 */       int m = NumberUtils.toInt(basips[i]);
/*  50 */       byte b = (byte)m;
/*  51 */       BasIP[i] = b;
/*     */     }
/*  53 */     authbuff[(4 + Username.length + Username.length)] = 10;
/*  54 */     authbuff[(4 + Username.length + Username.length + 1)] = 6;
/*  55 */     authbuff[(4 + Username.length + Username.length + 2)] = BasIP[0];
/*  56 */     authbuff[(4 + Username.length + Username.length + 3)] = BasIP[1];
/*  57 */     authbuff[(4 + Username.length + Username.length + 4)] = BasIP[2];
/*  58 */     authbuff[(4 + Username.length + Username.length + 5)] = BasIP[3];
/*     */ 
/*  60 */     return Req_Auth(Username, password, SerialNo, UserIP, 
/*  61 */       authbuff, timeout_Sec, Bas_IP, bas_PORT, sharedSecret);
/*     */   }
/*     */ 
/*     */   public static boolean Req_Auth(byte[] Username, byte[] password, byte[] SerialNo, byte[] UserIP, byte[] authbuff, int timeout_Sec, String Bas_IP, int bas_PORT, String sharedSecret)
/*     */   {
/*  68 */     DatagramSocket dataSocket = null;
/*     */ 
/*  70 */     byte[] Req_Auth = new byte[36 + Username.length + password.length + 6];
/*  71 */     Req_Auth[0] = 2;
/*  72 */     Req_Auth[1] = 3;
/*  73 */     Req_Auth[2] = 1;
/*  74 */     Req_Auth[3] = 0;
/*  75 */     Req_Auth[4] = SerialNo[0];
/*  76 */     Req_Auth[5] = SerialNo[1];
/*  77 */     Req_Auth[6] = 0;
/*  78 */     Req_Auth[7] = 0;
/*  79 */     Req_Auth[8] = UserIP[0];
/*  80 */     Req_Auth[9] = UserIP[1];
/*  81 */     Req_Auth[10] = UserIP[2];
/*  82 */     Req_Auth[11] = UserIP[3];
/*  83 */     Req_Auth[12] = 0;
/*  84 */     Req_Auth[13] = 0;
/*  85 */     Req_Auth[14] = 0;
/*  86 */     Req_Auth[15] = 3;
/*  87 */     byte[] BBuff = new byte[16];
/*  88 */     for (int i = 0; i < 16; i++) {
/*  89 */       BBuff[i] = Req_Auth[i];
/*     */     }
/*  91 */     byte[] Authen = Authenticator.MK_Authen(BBuff, authbuff, sharedSecret);
/*  92 */     for (int i = 0; i < 16; i++) {
/*  93 */       Req_Auth[(16 + i)] = Authen[i];
/*     */     }
/*  95 */     for (int i = 0; i < authbuff.length; i++) {
/*  96 */       Req_Auth[(32 + i)] = authbuff[i];
/*     */     }
/*     */     try
/*     */     {
/* 103 */       dataSocket = new DatagramSocket();
/*     */ 
/* 105 */       DatagramPacket requestPacket = new DatagramPacket(Req_Auth, 
/* 106 */         Req_Auth.length, InetAddress.getByName(Bas_IP), bas_PORT);
/* 107 */       dataSocket.send(requestPacket);
/*     */ 
/* 109 */       byte[] ACK_Auth_Data = new byte[100];
/* 110 */       DatagramPacket receivePacket = new DatagramPacket(ACK_Auth_Data, 100);
/*     */ 
/* 112 */       dataSocket.setSoTimeout(timeout_Sec * 1000);
/* 113 */       dataSocket.receive(receivePacket);
/* 114 */       ACK_Auth_Data = new byte[receivePacket.getLength()];
/* 115 */       for (int i = 0; i < ACK_Auth_Data.length; i++) {
/* 116 */         ACK_Auth_Data[i] = receivePacket.getData()[i];
/*     */       }
/* 122 */       if (((ACK_Auth_Data[14] & 0xFF) == 0) || 
/* 123 */         ((ACK_Auth_Data[14] & 0xFF) == 2))
/*     */       {
/* 131 */           log.info("认证成功,准备发送AFF_ACK_AUTH!!!");
/* 134 */         return AFF_Ack_Auth(SerialNo, UserIP, Bas_IP, 
/* 135 */           bas_PORT, sharedSecret);
/*     */       }
/* 137 */       if ((ACK_Auth_Data[14] & 0xFF) == 1) {
/* 139 */           log.info("发送认证请求被拒绝!!!");
/*     */       }
/* 149 */       else if ((ACK_Auth_Data[14] & 0xFF) == 3) {
/* 151 */           log.info("系统繁忙，请稍后再试!!!");
/*     */       }
/* 154 */       else if ((ACK_Auth_Data[14] & 0xFF) == 4) {
/* 156 */           log.info("发送认证请求失败!!!");
/*     */ 
/*     */       }
/* 165 */       return false;
/*     */     }
/*     */     catch (IOException e) {
/* 169 */         log.info("发送认证请求无响应!!!");
/* 172 */       PAP_Quit_V2.quit(2, Bas_IP, bas_PORT, timeout_Sec, SerialNo, 
/* 173 */         UserIP, sharedSecret);
/* 174 */       return false;
/*     */     } finally {
/* 176 */       dataSocket.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean AFF_Ack_Auth(byte[] SerialNo, byte[] UserIP, String Bas_IP, int bas_PORT, String sharedSecret)
/*     */   {
/* 184 */     DatagramSocket dataSocket = null;
/*     */ 
/* 186 */     byte[] AFF_Ack_Auth_Data = new byte[32];
/*     */ 
/* 188 */     AFF_Ack_Auth_Data[0] = 2;
/* 189 */     AFF_Ack_Auth_Data[1] = 7;
/* 190 */     AFF_Ack_Auth_Data[2] = 1;
/* 191 */     AFF_Ack_Auth_Data[3] = 0;
/* 192 */     AFF_Ack_Auth_Data[4] = SerialNo[0];
/* 193 */     AFF_Ack_Auth_Data[5] = SerialNo[1];
/* 194 */     AFF_Ack_Auth_Data[6] = 0;
/* 195 */     AFF_Ack_Auth_Data[7] = 0;
/* 196 */     AFF_Ack_Auth_Data[8] = UserIP[0];
/* 197 */     AFF_Ack_Auth_Data[9] = UserIP[1];
/* 198 */     AFF_Ack_Auth_Data[10] = UserIP[2];
/* 199 */     AFF_Ack_Auth_Data[11] = UserIP[3];
/* 200 */     AFF_Ack_Auth_Data[12] = 0;
/* 201 */     AFF_Ack_Auth_Data[13] = 0;
/* 202 */     AFF_Ack_Auth_Data[14] = 0;
/* 203 */     AFF_Ack_Auth_Data[15] = 0;
/* 204 */     byte[] BBBuff = new byte[16];
/* 205 */     for (int i = 0; i < BBBuff.length; i++) {
/* 206 */       BBBuff[i] = AFF_Ack_Auth_Data[i];
/*     */     }
/* 208 */     byte[] Attrs = new byte[0];
/* 209 */     byte[] BAuthen = Authenticator.MK_Authen(BBBuff, Attrs, sharedSecret);
/* 210 */     for (int i = 0; i < 16; i++) {
/* 211 */       AFF_Ack_Auth_Data[(16 + i)] = BAuthen[i];
/*     */     }
/*     */     try
/*     */     {
/* 219 */       dataSocket = new DatagramSocket();
/*     */ 
/* 221 */       DatagramPacket requestPacket = new DatagramPacket(
/* 222 */         AFF_Ack_Auth_Data, 32, InetAddress.getByName(Bas_IP), 
/* 223 */         bas_PORT);
/* 224 */       dataSocket.send(requestPacket);
/* 226 */         log.info("发送AFF_Ack_Auth认证成功回复报文成功!!!");
/* 229 */       return true;
/*     */     } catch (IOException e) {
/* 232 */         log.info("发送AFF_Ack_Auth认证成功回复报文出错!!!");
/* 235 */       return false;
/*     */     } finally {
/* 237 */       dataSocket.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           F:\ghos\OpenPortalServer V3.3.5.6 Stable 2016-1-16\OpenPortalServer\webapps\ROOT\WEB-INF\classes\
 * Qualified Name:     com.leeson.portal.core.service.action.v2.pap.PAP_Auth_V2
 * JD-Core Version:    0.6.2
 */