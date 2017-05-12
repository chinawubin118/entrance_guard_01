package cn.semtec.www.epcontrol;

import android.content.Context;
import android.media.MediaPlayer;

public class Util {
	public static MediaPlayer mediaplayer;

	public static void mediaPlay(int raw,Context context) {
		if (mediaplayer != null) {
			mediaplayer.stop();
			mediaplayer.release();
		}
			mediaplayer = MediaPlayer.create(context, raw);
			mediaplayer.start();
	}

	public static void mediaStop() {
		if (mediaplayer != null) {
			mediaplayer.stop();
			mediaplayer.release();
			mediaplayer = null;
		}
	}
	//获取MCU的UID指令
		public static byte[] getUID = new byte[]{(byte) 0xFA,0x05,0x02,0x00,0x00,0x00,(byte) 0xAA};
		final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

		public static String bytesToHex(byte[] bytes, int size) {
			char[] hexChars = new char[size * 2];
			for (int j = 0; j < size; j++) {
				int v = bytes[j] & 0xFF;
				hexChars[j * 2] = hexArray[v >>> 4];
				hexChars[j * 2 + 1] = hexArray[v & 0x0F];
			}
			return new String(hexChars);
		}
}
