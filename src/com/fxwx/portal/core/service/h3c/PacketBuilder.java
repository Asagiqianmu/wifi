// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PacketBuilder.java

package com.fxwx.portal.core.service.h3c;

import java.io.*;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;
import org.apache.log4j.Logger;

import com.fxwx.portal.core.model.Config;
import com.fxwx.portal.core.service.utils.PortalUtil;

// Referenced classes of package com.leeson.portal.core.service.h3c:
//			CommonFunctions, MD5

public class PacketBuilder
{

	private static Config config = Config.getInstance();
	private static Logger log = Logger.getLogger(PacketBuilder.class);
	private Number remoteIp;
	private int remotePort;
	private byte headVer;
	private byte headType;
	private byte headChapOrPap;
	private byte headReserved;
	private short headSerialNo;
	private byte headReqID0;
	private byte headReqID1;
	private int headUserIPv4;
	private BigInteger headUserIPv6;
	private short headUserPort;
	private byte headErrCode;
	private byte headAttrNum;
	private byte sAuthenticator[];
	private byte dAuthenticator[];
	private int destinePort;
	private InetAddress destineAddress;
	private byte sharedSecret[];
	private ByteArrayOutputStream requestAttributes;

	public PacketBuilder()
	{
		headVer = 2;
		headType = 1;
		headChapOrPap = 0;
		headReserved = 0;
		headSerialNo = 0;
		headReqID0 = 0;
		headReqID1 = 0;
		headUserIPv4 = 0;
		headUserIPv6 = null;
		headUserPort = 0;
		headErrCode = 0;
		headAttrNum = 0;
		sAuthenticator = new byte[16];
		dAuthenticator = new byte[16];
		destinePort = 0;
		sharedSecret = "hello".getBytes();
		requestAttributes = new ByteArrayOutputStream();
	}

	public int setHead(byte paramByte1, byte paramByte2)
	{
		int i = 1;
		switch (paramByte1)
		{
		case 80: // 'P'
			headVer = paramByte2;
			i = 0;
			break;

		case 81: // 'Q'
			headType = paramByte2;
			i = 0;
			break;

		case 82: // 'R'
			headChapOrPap = paramByte2;
			i = 0;
			break;

		case 83: // 'S'
			headReserved = paramByte2;
			i = 0;
			break;

		case 85: // 'U'
			headReqID0 = paramByte2;
			i = 0;
			break;

		case 86: // 'V'
			headReqID1 = paramByte2;
			i = 0;
			break;

		case 89: // 'Y'
			headErrCode = paramByte2;
			i = 0;
			break;

		case 84: // 'T'
		case 87: // 'W'
		case 88: // 'X'
		default:
			i = 1;
			break;
		}
		return i;
	}

	public int setHead(byte paramByte, short paramShort)
	{
		int i = 1;
		switch (paramByte)
		{
		case 84: // 'T'
			headSerialNo = paramShort;
			i = 0;
			break;

		case 88: // 'X'
			headUserPort = paramShort;
			i = 0;
			break;

		case 85: // 'U'
		case 86: // 'V'
		case 87: // 'W'
		default:
			i = 1;
			break;
		}
		return i;
	}

	public int setHead(byte paramByte, int paramInt)
	{
		int i = 1;
		switch (paramByte)
		{
		case 87: // 'W'
			headUserIPv4 = paramInt;
			i = 0;
			break;

		case 68: // 'D'
			remotePort = paramInt;
			// fall through

		default:
			i = 1;
			break;
		}
		return i;
	}

	public int setHead(byte paramByte, BigInteger paramBigInteger)
	{
		int i = 1;
		switch (paramByte)
		{
		case 87: // 'W'
			headUserIPv4 = paramBigInteger.intValue();
			i = 0;
			break;

		case 92: // '\\'
			headUserIPv6 = paramBigInteger;
			i = 0;
			break;

		case 67: // 'C'
			remoteIp = paramBigInteger;
			break;

		default:
			i = 1;
			break;
		}
		return i;
	}

	public int setHead(byte paramByte, Number paramNumber)
	{
		if (paramNumber instanceof Byte)
			return setHead(paramByte, paramNumber.byteValue());
		int i = 1;
		switch (paramByte)
		{
		case 87: // 'W'
			headUserIPv4 = paramNumber.intValue();
			i = 0;
			break;

		case 92: // '\\'
			headUserIPv6 = (BigInteger)paramNumber;
			i = 0;
			break;

		case 67: // 'C'
			remoteIp = paramNumber;
			break;

		default:
			i = 1;
			break;
		}
		return i;
	}

	public int setHead(byte paramByte, byte paramArrayOfByte[])
	{
		int i = 1;
		if (paramArrayOfByte == null)
			return 1;
		switch (paramByte)
		{
		case 91: // '['
			System.arraycopy(paramArrayOfByte, 0, sAuthenticator, 0, 16);
			i = 0;
			break;

		default:
			i = 1;
			break;
		}
		return i;
	}

	public void setDestinePort(int paramInt)
	{
		destinePort = paramInt;
	}

	public void setDestineAddress(InetAddress paramInetAddress)
	{
		destineAddress = paramInetAddress;
	}

	public void setDestineAddress(int paramInt)
	{
		destineAddress = CommonFunctions.convertIntToInetAddress(paramInt);
	}

	public void setShareSecret(byte paramArrayOfByte[])
	{
		sharedSecret = paramArrayOfByte;
	}

	private void setAttribute(byte paramByte, int paramInt, byte paramArrayOfByte[])
	{
		if (paramInt > 253)
			paramInt = 253;
		requestAttributes.write(paramByte);
		requestAttributes.write(paramInt + 2);
		requestAttributes.write(paramArrayOfByte, 0, paramInt);
	}

	public void setAttribute(byte paramByte)
	{
		headAttrNum = (byte)(headAttrNum + 1);
		requestAttributes.write(paramByte);
		requestAttributes.write(2);
	}

	public void setAttribute(byte paramByte, byte paramArrayOfByte[])
	{
		if (paramArrayOfByte == null)
		{
			return;
		} else
		{
			headAttrNum = (byte)(headAttrNum + 1);
			setAttribute(paramByte, paramArrayOfByte.length, paramArrayOfByte);
			return;
		}
	}

	public void setAttribute(byte paramByte1, byte paramByte2)
	{
		headAttrNum = (byte)(headAttrNum + 1);
		requestAttributes.write(paramByte1);
		requestAttributes.write(3);
		requestAttributes.write(paramByte2);
	}

	private byte[] makeAuthenticator(byte paramArrayOfByte[])
	{
		MD5 localMD5 = new MD5();
		localMD5.update(paramArrayOfByte);
		localMD5.update(sharedSecret);
		return localMD5.finalFunc();
	}

	public DatagramPacket composeWebPacket()
		throws IOException
	{
		return composePacketData(true);
	}

	public DatagramPacket composePacket()
	{
		try {
			return composePacketData(false);
		} catch (Exception e) {
			return null;
		}
	}

	private DatagramPacket composePacketData(boolean paramBoolean)
		throws IOException
	{
		ByteArrayOutputStream localByteArrayOutputStream = null;
		DataOutputStream localDataOutputStream = null;
		DatagramPacket localDatagramPacket = null;
		localByteArrayOutputStream = new ByteArrayOutputStream();
		localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
		localDataOutputStream.writeByte(headVer);
		localDataOutputStream.writeByte(headType);
		localDataOutputStream.writeByte(headChapOrPap);
		localDataOutputStream.writeByte(headReserved);
		localDataOutputStream.writeShort(headSerialNo);
		localDataOutputStream.writeByte(headReqID0);
		localDataOutputStream.writeByte(headReqID1);
		localDataOutputStream.writeInt(headUserIPv4);
		localDataOutputStream.writeShort(headUserPort);
		localDataOutputStream.writeByte(headErrCode);
		localDataOutputStream.writeByte(headAttrNum);
		if (3 == headVer)
			if (headUserIPv6 != null && headUserIPv6.bitLength() > 32)
			{
				byte arrayOfByte2[] = CommonFunctions.convertBigIntegerTo16Bytes(headUserIPv6);
				localDataOutputStream.write(arrayOfByte2, 0, arrayOfByte2.length);
			} else
			{
				byte arrayOfByte2[] = new byte[16];
				Arrays.fill(arrayOfByte2, (byte)0);
				localDataOutputStream.write(arrayOfByte2, 0, 16);
			}
		if (2 == headVer || 3 == headVer)
		{
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info((new StringBuilder("sAuthenticator包：")).append(PortalUtil.Getbyte2HexString(sAuthenticator)).toString());
			localDataOutputStream.write(sAuthenticator, 0, 16);
		}
		if (requestAttributes.size() != 0)
		{
			localDataOutputStream.write(requestAttributes.toByteArray(), 0, requestAttributes.size());
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info((new StringBuilder("requestAttributes包：")).append(PortalUtil.Getbyte2HexString(requestAttributes.toByteArray())).toString());
		}
		requestAttributes.close();
		localDataOutputStream.flush();
		localDataOutputStream.close();
		byte arrayOfByte1[] = localByteArrayOutputStream.toByteArray();
		if (/*basConfig.getIsdebug().equals("1")*/true)
			log.info((new StringBuilder("基础包：")).append(PortalUtil.Getbyte2HexString(arrayOfByte1)).toString());
		localByteArrayOutputStream.close();
		int i = 3 != headVer ? 16 : 32;
		if (2 == headVer || 3 == headVer)
		{
			dAuthenticator = makeAuthenticator(arrayOfByte1);
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info((new StringBuilder("dAuthenticator包：")).append(PortalUtil.Getbyte2HexString(dAuthenticator)).toString());
			System.arraycopy(dAuthenticator, 0, arrayOfByte1, i, 16);
		}
		if (/*basConfig.getIsdebug().equals("1")*/true)
			log.info((new StringBuilder("原包：")).append(PortalUtil.Getbyte2HexString(arrayOfByte1)).toString());
		int j = arrayOfByte1.length;
		if (!paramBoolean)
		{
			byte arrayOfByte3[] = new byte[j + 20];
			Arrays.fill(arrayOfByte3, (byte)0);
			byte arrayOfByte4[];
			if (remoteIp instanceof BigInteger)
			{
				arrayOfByte4 = ((BigInteger)remoteIp).toByteArray();
				if (/*basConfig.getIsdebug().equals("1")*/true)
					log.info((new StringBuilder("remoteIp包：")).append(PortalUtil.Getbyte2HexString(arrayOfByte4)).toString());
			} else
			{
				arrayOfByte4 = CommonFunctions.convertIntToByteArray(remoteIp.intValue());
				if (/*basConfig.getIsdebug().equals("1")*/true)
					log.info((new StringBuilder("remoteIp包：")).append(PortalUtil.Getbyte2HexString(arrayOfByte4)).toString());
			}
			System.arraycopy(arrayOfByte4, 0, arrayOfByte3, 16 - arrayOfByte4.length, arrayOfByte4.length);
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info((new StringBuilder("remotePort包：")).append(PortalUtil.Getbyte2HexString(CommonFunctions.convertIntToByteArray(remotePort))).toString());
			System.arraycopy(CommonFunctions.convertIntToByteArray(remotePort), 0, arrayOfByte3, 16, 4);
			System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 20, j);
			localDatagramPacket = new DatagramPacket(new byte[j + 20], j + 20);
			localDatagramPacket.setLength(j + 20);
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info((new StringBuilder("REQ ")).append(PortalUtil.Getbyte2HexString(arrayOfByte3)).toString());
			localDatagramPacket.setData(arrayOfByte3);
		} else
		{
			localDatagramPacket = new DatagramPacket(new byte[j], j);
			localDatagramPacket.setLength(j);
			if (/*basConfig.getIsdebug().equals("1")*/true)
				log.info((new StringBuilder("REQ ")).append(PortalUtil.Getbyte2HexString(arrayOfByte1)).toString());
			localDatagramPacket.setData(arrayOfByte1);
		}
		localDatagramPacket.setPort(destinePort);
		localDatagramPacket.setAddress(destineAddress);
		return localDatagramPacket;
	}

	public byte[] getAuthenticatior()
	{
		return dAuthenticator;
	}

	public byte getHeadType()
	{
		return headType;
	}

	public short getHeadSerialNo()
	{
		return headSerialNo;
	}

}
