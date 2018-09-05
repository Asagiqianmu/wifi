/*    */ package com.fxwx.portal.core.service.utils;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;
/*    */ 
/*    */ public class Authenticator
/*    */ {
/* 18 */   private static Config config = Config.getInstance();
/* 19 */   private static Logger log = Logger.getLogger(Authenticator.class);
/*    */ 
/*    */   public static byte[] MK_Authen(byte[] Buff, byte[] Attrs, String sharedSecret)
/*    */   {
/* 36 */     byte[] Secret = sharedSecret.getBytes();
/* 37 */     byte[] Authen = new byte[16];
/*    */ 
/* 39 */     byte[] buf = new byte[Buff.length + 16 + Attrs.length + Secret.length];
/*    */ 
/* 41 */     for (int i = 0; i < Buff.length; i++) {
/* 42 */       buf[i] = Buff[i];
/*    */     }
/* 44 */     for (int i = 0; i < 16; i++) {
/* 45 */       buf[(Buff.length + i)] = 0;
/*    */     }
/* 47 */     if (Attrs.length > 0) {
/* 48 */       for (int i = 0; i < Attrs.length; i++) {
/* 49 */         buf[(Buff.length + 16 + i)] = Attrs[i];
/*    */       }
/* 51 */       for (int i = 0; i < Secret.length; i++)
/* 52 */         buf[(Buff.length + 16 + Attrs.length + i)] = Secret[i];
/*    */     }
/*    */     else {
/* 55 */       for (int i = 0; i < Secret.length; i++) {
/* 56 */         buf[(Buff.length + 16 + i)] = Secret[i];
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/*    */     try
/*    */     {
/* 67 */       MessageDigest md = MessageDigest.getInstance("MD5");
/* 68 */       md.update(buf);
/* 69 */       Authen = md.digest();
/* 70 */       if (/*config.getIsdebug().equals("1")*/true)
/* 71 */         log.info("生成Request Authenticator :" + 
/* 72 */           PortalUtil.Getbyte2HexString(Authen));
/*    */     }
/*    */     catch (NoSuchAlgorithmException e)
/*    */     {
/* 76 */       if (/*config.getIsdebug().equals("1")*/true) {
/* 77 */         log.info("生成Request Authenticator出错！");
/*    */       }
/*    */     }
/*    */ 
/* 81 */     return Authen;
/*    */   }
/*    */ }

/* Location:           F:\ghos\OpenPortalServer V3.3.5.6 Stable 2016-1-16\OpenPortalServer\webapps\ROOT\WEB-INF\classes\
 * Qualified Name:     com.leeson.portal.core.service.utils.Authenticator
 * JD-Core Version:    0.6.2
 */