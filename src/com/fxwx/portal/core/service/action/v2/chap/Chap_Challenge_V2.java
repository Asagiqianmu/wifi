/*    */ package com.fxwx.portal.core.service.action.v2.chap;
/*    */ import java.io.IOException;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.InetAddress;
/*    */ import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;
import com.fxwx.portal.core.service.utils.Authenticator;
import com.fxwx.portal.core.service.utils.PortalUtil;
/*    */ 
/*    */ public class Chap_Challenge_V2
/*    */ {
/* 22 */   private static Config config = Config.getInstance();
/* 23 */   private static Logger log = Logger.getLogger(Chap_Challenge_V2.class);
/*    */ 
/*    */   public static byte[] challenge(String Bas_IP, int bas_PORT, int timeout_Sec, byte[] SerialNo, byte[] UserIP, String sharedSecret)
/*    */   {
/* 27 */     DatagramSocket dataSocket = null;
/* 28 */     byte[] ErrorInfo = new byte[1];
/* 29 */     byte[] Req_Challenge = new byte[32];
/* 30 */     byte[] BBuff = new byte[16];
/* 31 */     byte[] Attrs = new byte[0];
/* 32 */     Req_Challenge[0] = 2;
/* 33 */     Req_Challenge[1] = 1;
/* 34 */     Req_Challenge[2] = 0;
/* 35 */     Req_Challenge[3] = 0;
/* 36 */     Req_Challenge[4] = SerialNo[0];
/* 37 */     Req_Challenge[5] = SerialNo[1];
/* 38 */     Req_Challenge[6] = 0;
/* 39 */     Req_Challenge[7] = 0;
/* 40 */     Req_Challenge[8] = UserIP[0];
/* 41 */     Req_Challenge[9] = UserIP[1];
/* 42 */     Req_Challenge[10] = UserIP[2];
/* 43 */     Req_Challenge[11] = UserIP[3];
/* 44 */     Req_Challenge[12] = 0;
/* 45 */     Req_Challenge[13] = 0;
/* 46 */     Req_Challenge[14] = 0;
/* 47 */     Req_Challenge[15] = 0;
/* 48 */     for (int i = 0; i < 16; i++) {
/* 49 */       BBuff[i] = Req_Challenge[i];
/*    */     }
/* 51 */     byte[] Authen = Authenticator.MK_Authen(BBuff, Attrs, sharedSecret);
/* 52 */     for (int i = 0; i < 16; i++) {
/* 53 */       Req_Challenge[(16 + i)] = Authen[i];
/*    */     }
/*    */     try
/*    */     {
/* 60 */       dataSocket = new DatagramSocket();
/* 61 */       DatagramPacket requestPacket = new DatagramPacket(Req_Challenge, 
/* 62 */         32, InetAddress.getByName(Bas_IP), bas_PORT);
/* 63 */       dataSocket.send(requestPacket);
/* 64 */       byte[] ACK_Challenge_Data = new byte[50];
/* 65 */       DatagramPacket receivePacket = new DatagramPacket(
/* 66 */         ACK_Challenge_Data, 50);
/* 67 */       dataSocket.setSoTimeout(timeout_Sec * 1000);
/* 68 */       dataSocket.receive(receivePacket);
/*    */ 
/* 74 */       if ((ACK_Challenge_Data[14] & 0xFF) == 0) {
/* 76 */           log.info("发送Challenge请求成功,准备发送REQ Auth!!!");
/* 79 */         return ACK_Challenge_Data;
/* 80 */       }if ((ACK_Challenge_Data[14] & 0xFF) == 1) {
/* 82 */           log.info("发送Challenge请求被拒绝!!!");
/* 85 */         ErrorInfo[0] = 11;
/* 86 */         return ErrorInfo;
/* 87 */       }if ((ACK_Challenge_Data[14] & 0xFF) == 2) {
/* 89 */           log.info("发送Challenge连接已建立!!!");
/* 92 */         ErrorInfo[0] = 12;
/* 93 */         return ErrorInfo;
/* 94 */       }if ((ACK_Challenge_Data[14] & 0xFF) == 3) {
/* 96 */           log.info("系统繁忙，请稍后再试!!!");
/* 99 */         ErrorInfo[0] = 13;
/* 100 */         return ErrorInfo;
/* 101 */       }if ((ACK_Challenge_Data[14] & 0xFF) == 4) {
/* 103 */           log.info("发送Challenge请求出现未知错误!!!");
/* 106 */         ErrorInfo[0] = 14;
/* 107 */         return ErrorInfo;
/*    */       }
/*    */ 
/* 113 */       ErrorInfo[0] = 14;
/* 114 */       return ErrorInfo;
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/*    */       byte[] arrayOfByte1;
/* 118 */         log.info("发送Challenge请求无响应!!!");
/* 121 */       ErrorInfo[0] = 1;
/* 122 */       return ErrorInfo;
/*    */     } finally {
/* 124 */       dataSocket.close();
/*    */     }
/*    */   }
/*    */ }

/* Location:           F:\ghos\OpenPortalServer V3.3.5.6 Stable 2016-1-16\OpenPortalServer\webapps\ROOT\WEB-INF\classes\
 * Qualified Name:     com.leeson.portal.core.service.action.v2.chap.Chap_Challenge_V2
 * JD-Core Version:    0.6.2
 */