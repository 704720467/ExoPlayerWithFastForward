package cn.zp.zpexoplayer.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaExtractor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MediaUtils {
	private MediaUtils() {

	}

	private static MediaUtils instance;
	private Context context;

	public Context getContext() {
		return context;
	}

	// 全局初始化，建议在appstart时调用该方法
	public static MediaUtils getInstance(Context context) {
		if (instance == null) {
			instance = new MediaUtils();
			instance.context = context;
		}
		return instance;
	}

	public static MediaUtils getInstance() {
		if (instance == null) {
			throw new RuntimeException("media shold be init with Context");
		}
		return instance;
	}

	/**
	 * @param extrator
	 * @param path
	 *            assert:// 为前缀，会从assert中读取，否则认为是绝对路径
	 * @throws IOException
	 */
	public void setDataSource(MediaExtractor extrator, String path) throws IOException {
		if (path.startsWith(Constants.ASSERT_FILE_PREFIX)) {
			String assertPath = path.substring(Constants.ASSERT_FILE_PREFIX.length());
			AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(assertPath);
			extrator.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
		} else {
			extrator.setDataSource(path);
		}
	}

	public void copyAssertToFile(String asset, String destPath) throws IOException {

		InputStream is = context.getAssets().open(asset);
		File destinationFile = new File(destPath);
		destinationFile.getParentFile().mkdirs();
		OutputStream os = new FileOutputStream(destinationFile);
		byte[] buffer = new byte[4096];
		int nread;

		while ((nread = is.read(buffer)) != -1) {
			if (nread == 0) {
				nread = is.read();
				if (nread < 0)
					break;
				os.write(nread);
				continue;
			}
			os.write(buffer, 0, nread);
		}
		os.close();

	}

	public void copyAssertToFile(AssetFileDescriptor afd, String destPath) throws IOException {

		InputStream is = afd.createInputStream();
		File destinationFile = new File(destPath);
		destinationFile.getParentFile().mkdirs();
		OutputStream os = new FileOutputStream(destinationFile);
		byte[] buffer = new byte[4096];
		int nread;

		while ((nread = is.read(buffer)) != -1) {
			if (nread == 0) {
				nread = is.read();
				if (nread < 0)
					break;
				os.write(nread);
				continue;
			}
			os.write(buffer, 0, nread);
		}
		os.close();

	}

	/**
	 * 根据视频路径获得相应的音频路径
	 * 
	 * @param videoPath
	 * @return
	 */
	public static String audioPath(String videoPath) {
		String aacPath = videoPath.substring(0, videoPath.lastIndexOf('.')) + ".aac";
		return aacPath;
	}

	/**
	 * 文件移动或者重命名
	 * 
	 * @param src
	 * @param dest
	 */
	public void move(String src, String dest) {
		File from = new File(src);
		File to = new File(dest);
		from.renameTo(to);

	}

	/**
	 * String assertName = "glsl/" + style.toString() + ".frag"; <br/>
	 * String script = getGlslFromAssert(assertName);
	 * 
	 * @param assertName
	 * @return
	 */
	public String getGlslFromAssert(String assertName) {
		AssetManager assetManager = context.getAssets();
		try {
			InputStream is = assetManager.open(assertName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer stringBuffer = new StringBuffer();
			String str = null;
			while ((str = br.readLine()) != null) {
				stringBuffer.append(str);
				stringBuffer.append("\n");
			}
			return stringBuffer.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Typeface createTypeFace(String fontName) {
		Typeface font = null;
		try {
			if (fontName == null || fontName.length() == 0) {
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
			} else if (fontName.startsWith(Constants.ASSERT_FILE_PREFIX)) {
				String fontAssertPath = fontName.substring(Constants.ASSERT_FILE_PREFIX.length());
				font = Typeface.createFromAsset(context.getAssets(), fontAssertPath);
			} else {
				font = Typeface.createFromFile(fontName);
			}
		} catch (Exception e) {
			Log.w(Constants.TAG, "MediaUtils_createTypeFace error,use default,fontName:" + fontName, e);
			Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
		}
		return font;
	}

	public Bitmap getImageFormAssertOfFile(String fileName) {
		if (fileName.startsWith(Constants.ASSERT_FILE_PREFIX)) {
			String assertPath = fileName.substring(Constants.ASSERT_FILE_PREFIX.length());
			Bitmap image = null;
			AssetManager am = MediaUtils.getInstance().getContext().getAssets();
			try {
				InputStream is = am.open(assertPath);
				image = BitmapFactory.decodeStream(is);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		} else {
			return BitmapFactory.decodeFile(fileName);
		}

	}
}
