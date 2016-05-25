package org.haitao.common.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FileUtils {
	
	private static final String IMAGE_PATH_NAME = "/image/";
	private static final String VOICE_PATH_NAME = "/voice/";
	private static final String FILE_PATH_NAME = "/file/";
	private static final String VIDEO_PATH_NAME = "/video/";
	public static  String rootName = "neiquan";
	public static  String cachePath ;
	
	public static void init(Context context){
		cachePath =context.getCacheDir().getAbsolutePath();
	}
	/**
	 * 检验SDcard状态
	 * @return
	 */
	public static boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	public static String SDCachePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getPath();
		} else {
			//没有sd卡
			if(cachePath!=null){
				return	cachePath;
			}
			return Environment.getDataDirectory().getPath();
		}
	}


	/**
	 * 判断sd卡是否可用
	 * @return
	 */
	public static String getRandomName() {
		
		return TimeUtil.dateToString(new Date(), TimeUtil.FORMAT_DATE_TIME_SECOND) + getRandom(0, 1000);
	}
	public static int getRandom(int min,int max){
		 Random random = new Random();
		return random.nextInt(max)%(max-min+1) + min;
	}
	/**
	 * 获取app根路径
	 * @return
	 */
	public static String getAppPath() {
		if(hasSDCard() ){
			String path = SDCachePath();
			File f = new File(path + "/Android/data/"+rootName+"/");
			if (!f.exists()) {
				f.mkdirs();
				return f.getPath();
			} else {
				return f.getPath();
			}
		}
		return	cachePath;
		
	}

	/**
	 * 设置文件根路径的名字
	 * @param name
	 */
	public static void setRootPathName(String name) {
		rootName =name;
	}
	/**
	 * 获取文件夹大小
	 * @param file
	 * @return
	 */
	public static double getDirSize(File file) {

		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				if(children!=null){
					for (File f : children)
						size += getDirSize(f);
				}
				return size;
			} else {// 如果是文件则直接返回其大小,以“兆”为单位
				double size = (double) file.length() / 1024 / 1024;
				return size;
			}
		} else {
			return 0.0;
		}
	}

	/**
	 * 获取文件夹大小 返回m 兆
	 * @param file
	 * @return
	 */
	public static String getDirSizeStr(File file) {
		DecimalFormat df = new java.text.DecimalFormat("#0.0");
		String size = df.format(getDirSize(file));
		return size + "M";
	}
	/**
	 * 获取缓存大小  返回m 兆
	 * @param file
	 * @return
	 */
	public static String getCacheSize() {
		return getDirSizeStr(new File(getAppPath()));
	}
	
	/**
	 * 清除图片缓存
	 */
	public static void cleanCache() {
		deleteFile(new File(getAppPath()));
	}
	public static String getCacheImgSize() {
		return getDirSizeStr(new File(getImagePath()));
	}
	/**
	 * 清除图片缓存
	 */
	public static void cleanImageCache() {
		deleteFile(new File(getImagePath()));
	}
	/**
	 * 新建文件夹
	 * @param path
	 * @return
	 */
	public static boolean makeDir(String path) {

		if (!hasSDCard()) {
			return false;
		}
		File file = new File(path);
		if (!file.exists()) {
			boolean rs =file.mkdirs();
			AppLog.e("makeDir==" + path+rs ); //创建文件夹
			
		}
		return true;
	}
	/**
	 * 新建文件夹
	 * @param path
	 * @return
	 */
	public static boolean makeFile(String path) {
		
		if (!hasSDCard()) {
			return false;
		}
		File file = new File(path);
		if (!file.exists()) {
			AppLog.e("makeFile==" + path);
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				AppLog.e(e);
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 删除文件
	 * @param f
	 * @return
	 */
	public static boolean deleteFile(File f) {

		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					deleteFile(files[i]);
				}
			}
		}
		return f.delete();
	}

	/**
	 * 判断文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean fileExit(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {

				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	/**
	 * 图片路径
	 * 
	 * @return
	 */
	public static File getImageFile() {
		makeDir(getAppPath() + IMAGE_PATH_NAME);
		if(hasSDCard()){
			return new File(getAppPath() + IMAGE_PATH_NAME);
		}
		return new File( SDCachePath());
	}

	/**
	 * 语音路径
	 * 
	 * @return
	 */
	public static File getVoiceFile() {
		makeDir(getAppPath() + VOICE_PATH_NAME);
		
		if(hasSDCard()){
			return new File(getAppPath() + VOICE_PATH_NAME);
		}
		return new File( SDCachePath());
	}

	/**
	 * 文件路径
	 * 
	 * @return
	 */
	public static File getFileFile() {
		makeDir(getAppPath() + FILE_PATH_NAME);
		
		if(hasSDCard()){
			return new File(getAppPath() + FILE_PATH_NAME);
		}
		return new File( SDCachePath());
	}

	/**
	 * 视频文件
	 * 
	 * @return
	 */
	public static File getVideoFile() {
		makeDir(getAppPath() + VIDEO_PATH_NAME);
		if(hasSDCard()){
			return new File(getAppPath() + VIDEO_PATH_NAME);
		}
		return new File( SDCachePath());
	}

	public static String getImagePath(){
		return getImageFile().getAbsolutePath();
	}
	public static String getFilePath(){
		return getFileFile().getAbsolutePath();
	}
	public static String getVideoPath(){
		return getFileFile().getAbsolutePath();
	}
	/**
	 * 同步保存bitmap
	 * @param dirPath
	 * @param bitmap
	 * @param recycle
	 * @return
	 */
	public static File saveBitmap(String dirPath, Bitmap bitmap,boolean recycle) {

		//makeDir(dirPath);
		File file = new File(dirPath);
		FileOutputStream fOut = null;
		try {
			file.createNewFile();
			fOut = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

		} catch (IOException e1) {
			file = null;
			e1.printStackTrace();
		} finally {
			if (fOut != null) {
				try {
					fOut.flush();
					fOut.close();
					if (recycle) {
						bitmap.recycle();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 持久化对象
	 * @param obj
	 * @param fileName
	 */
	public static  void writeObj(Object obj,String fileName){
		
		File file = new File(getFilePath(), fileName);
		
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 读取持久化对象
	 * @param fileName
	 * @return
	 */
	public static Object readObj(String fileName){
		ObjectInputStream ois = null;

		try {
			File file = new File(getFilePath(), fileName);
			if(!file.exists()) return null;
			ois = new ObjectInputStream(new FileInputStream(file));
			return ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
    /**
     * 从文件中读取文本
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName()
                    + "readFile---->" + filePath + " not found");
        }
        return inputStream2String(is);
    }

    /**
     * 从assets中读取文本
     * 
     * @param name
     * @return
     */
    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName()
                    + ".readFileFromAssets---->" + name + " not found");
        }
        return inputStream2String(is);
    }

    /**
     * 输入流转字符串
     * 
     * @param is
     * @return 一个流中的字符串
     */
    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (Exception ex) {
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }
    /**
     * 关闭流
     * 
     * @param closeables
     */
    private static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(
                        FileUtils.class.getClass().getName(), e);
            }
        }
    }
    /**
     * 把uri转为File对象
     */
    public static File uri2File(Activity aty, Uri uri) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            // 在API11以下可以使用：managedQuery
            String[] proj = { MediaStore.Images.Media.DATA };
            @SuppressWarnings("deprecation")
            Cursor actualimagecursor = aty.managedQuery(uri, proj, null, null,
                    null);
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor
                    .getString(actual_image_column_index);
            return new File(img_path);
        } else {
            // 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
            String[] projection = { MediaStore.Images.Media.DATA };
            CursorLoader loader = new CursorLoader(aty, uri, projection, null,
                    null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }
    }

    /**
     * 复制文件
     * 
     * @param from
     * @param to
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            return;
        }
        if (null == to) {
            return;
        }
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            copyFileFast(is, os);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getClass().getName(), e);
        } finally {
            closeIO(is, os);
        }
    }
    
    /**
     * 快速复制文件（采用nio操作）
     * 
     * @param is
     *            数据来源
     * @param os
     *            数据目标
     * @throws IOException
     */
    public static void copyFileFast(FileInputStream is, FileOutputStream os)
            throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }
    /**
	 * 获取SD卡的总大小 为什么不直接返回格式化的<br>
	 * 因为可能开发中需要内存的做比较
	 * @return SD卡的总大小
	 */
	@SuppressWarnings("deprecation")
	public static long getSDCardSize()
	{
		File file =  Environment.getDataDirectory();
		StatFs statFs = new StatFs(file.getPath());
		long blockSize = statFs.getBlockSize();
		long blockCount = statFs.getBlockCount();
		return blockCount * blockSize;
	}
	
	/**
	 * 获取SD卡的可用大小 为什么不直接返回格式化<br> 
	 * 因为可能开发中需要内存的做比较
	 * @return SD卡的可用大小
	 */
	@SuppressWarnings("deprecation")
	public static long getSDcardFreeSize()
	{
		File file =  Environment.getDataDirectory();
		StatFs statFs = new StatFs(file.getPath());
		long blockSize = statFs.getBlockSize();
		long availableBlocks = statFs.getAvailableBlocks();
		return availableBlocks * blockSize;
	}
}
