package com.cgltech.cat_conn.util;

public class ByteUtils {

	public ByteUtils() {
	}

	public static byte[] shortToBytes(int number) {
		byte[] result = new byte[2];
		
		result[0] = (byte)number;
		result[1] = (byte)(number >> 8);
		
		return result;
	}

	public static int byteToShort(byte[] packetLength) {
		if(packetLength.length != 2)
			return -1;
		
		//int result = packetLength[0] << 8 + (packetLength[1]);
		int result = (packetLength[1] << 8 & 0xFF00) | (packetLength[0] & 0xFF);
		return result;
	}
}
