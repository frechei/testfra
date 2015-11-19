package org.haitao.common.utils;

import java.io.File;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.VideoColumns;

public class CameraUtils {

	public static final int TAKE_PICTURE = 1;
	public static final int TAKE_VIDEO = 2;
	public static final int LOCAL_PICTURE = 3;
	public static final int LOCAL_VIDEO = 4;
	public static String photoPath = null;

	public static String takePhoto(Activity ac, String path, String pictureName) {

		if (FileUtils.makeDir(path)) {
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
			photoPath = path + "/" + pictureName;
			AppLog.e(photoPath);
			Uri imageUri = Uri.fromFile(new File(path, pictureName));
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			ac.startActivityForResult(cameraIntent, TAKE_PICTURE);
		} else {
			ToastUtil.longShowToast("SD卡不可用");
			photoPath = null;
			return null;
		}
		return path + "/" + pictureName;

	}

	/**
	 * 拍照
	 * 
	 * @param ac
	 * @return
	 */
	public static String takePhoto(Activity ac) {
		return takePhoto(ac, FileUtils.getAppPath(), getRandomName() + ".png");

	}

	public static String getRandomName() {
		return TimeUtil.dateToString(new Date(), TimeUtil.FORMAT_DATE_TIME_SECOND);

	}

	/**
	 * 打开相机录像
	 * 
	 * @param ac
	 */
	public static void takeVideo(Activity ac) {

		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		ac.startActivityForResult(intent, TAKE_VIDEO);
	}

	/**
	 * 选取本地照片
	 * 
	 * @param ac
	 */
	public static void getLocalPicture(Activity ac) {

		Intent intent = new Intent();
		// 开启Pictures画面Type设定为image
		intent.setType("image/*");
		// 使用Intent.ACTION_GET_CONTENT这个Action
		intent.setAction(Intent.ACTION_GET_CONTENT);
		ac.startActivityForResult(intent, LOCAL_PICTURE);
		// 取得相片后返回本画面
		/*
		 * Intent intent = new Intent( Intent.ACTION_PICK,
		 * android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * ac.startActivityForResult(intent, LOCAL_PICTURE);
		 */
	}

	/**
	 * 选取本地视频
	 * 
	 * @param ac
	 */
	public static void getLocalVideo(Activity ac) {

		Intent intent = new Intent();
		/* 开启Pictures画面Type设定为image */
		intent.setType("video/*");
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
		ac.startActivityForResult(intent, LOCAL_VIDEO);
	}

	/**
	 * 获取系统图片地址 要判断是否是null
	 * 
	 * @param ac
	 * @param data
	 * @return
	 */
	public static String getPhotoByIntent(Activity ac, Intent data) {
		String path = null;
		if (data == null) {
			path = null;
		} else {
			String[] proj = { MediaStore.Images.Media.DATA };
			Uri originalUri = data.getData();
			Cursor cursor = ac.getContentResolver().query(originalUri, proj, null, null, null);
			if (cursor == null) {
				// file:// 去除 文件管理器
				/*
				 * if (originalUri!=null && originalUri.toString().length()>11
				 * && originalUri.toString().startsWith("file://") ) { String
				 * ext
				 * =originalUri.toString().substring(originalUri.toString().length
				 * ()-3); if (ext.equalsIgnoreCase("jpg") ||
				 * ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("gif")) {
				 * path = originalUri.toString().substring(7); }else{
				 * ToastUtil.shortShowToast("不是图片"); } }else{
				 * ToastUtil.shortShowToast("文件不支持"); }
				 */
				if (originalUri != null) {
					path = originalUri.getPath();
				} else {
					ToastUtil.shortShowToast("不是图片");
				}
			} else {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path = cursor.getString(column_index);
				if (path == null) {
					ToastUtil.shortShowToast("该图库不支持");
				}
				cursor.close();
			}
		}
		return path;
	}

	/**
	 * 获取图库视屏地址 要判断是否是null
	 * 
	 * @param ac
	 * @param data
	 * @return
	 */
	public static String getVideoByIntent(Activity ac, Intent data) {
		String path = null;
		if (data == null) {
			path = null;
		} else {
			String[] proj = { MediaStore.Images.Media.DATA };
			Uri originalUri = data.getData();
			Cursor cursor = ac.getContentResolver().query(originalUri, proj, null, null, null);
			if (cursor == null) {
				// file:// 去除 文件管理器
				if (originalUri != null && originalUri.toString().length() > 11 && originalUri.toString().startsWith("file://")) {
					String ext = originalUri.toString().substring(originalUri.toString().length() - 3);
					if (ext.equalsIgnoreCase("mp4") || ext.equalsIgnoreCase("avi") || ext.equalsIgnoreCase("flv") || ext.equalsIgnoreCase("mov")) {
						path = originalUri.toString().substring(7);
					} else {
						ToastUtil.shortShowToast("不是支持的视屏");
					}
				} else {
					ToastUtil.shortShowToast("不是支持的视屏");
				}
			} else {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path = cursor.getString(column_index);
				if (path == null) {
					ToastUtil.shortShowToast("获取视频路径失败");
				}
				cursor.close();
			}
		}
		return path;
	}

	/**
	 * 获取录像的地址 要判断是否是null
	 * 
	 * @param ac
	 * @param data
	 * @return
	 */
	public static String getTakeVideoByIntent(Activity ac, Intent data) {

		String path = null;
		if (data != null) {
			Uri uri = data.getData();
			Cursor cursor = ac.getContentResolver().query(uri, null, null, null, null);
			if (cursor != null && cursor.moveToNext()) {
				path = cursor.getString(cursor.getColumnIndex(VideoColumns.DATA));
				cursor.close();
			}
		}

		return path;
	}

	/**
	 * 获取视频缩略图
	 * 
	 * @param videoPath
	 * @param width
	 * @param height
	 * @param kind
	 * @return
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		AppLog.e(bitmap);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		AppLog.e(bitmap);
		return bitmap;
	}
	/**  
	 *  获取帧缩略图，根据容器的高宽进行缩放
	 *  @param filePath
	 *  @return   
	 */
	@SuppressLint("NewApi")
	public static  Bitmap getVideoThumbnail(String filePath,int rightwidth, int rightheight) {  
		Bitmap bitmap = null;  
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
		try {  
			retriever.setDataSource(filePath);  
			bitmap = retriever.getFrameAtTime(-1);  
		}   
		catch(IllegalArgumentException e) {  
			e.printStackTrace();  
		}   
		catch (RuntimeException e) {  
			e.printStackTrace();  
		}   
		finally {  
			try {  
				retriever.release();  
			}   
			catch (RuntimeException e) {  
				e.printStackTrace();  
			}  
		} 
		if(bitmap==null)
			return null;
		// Scale down the bitmap if it's too large.
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int pWidth=rightwidth;// 容器宽度
		int pHeight=rightheight;//容器高度
		//获取宽高跟容器宽高相比较小的倍数，以此为标准进行缩放
		float scale = Math.min((float)width/pWidth, (float)height/pHeight);
		int w = Math.round(scale * pWidth);
		int h = Math.round(scale * pHeight);
		bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
		return bitmap;  
	} 
	/**
	 * 判断视频是否太大 200的不让选择
	 * @return
	 */
	public static boolean isVideRight(String path){
		boolean flag = false;
		if (!FileUtils.fileExit(path)) {
			ToastUtil.shortShowToast("文件不存在");
		}else{
			if (FileUtils.getDirSize(new File(path))>300) {
				ToastUtil.shortShowToast("视频太大,请选择小于300m的视频");
			}else{
				flag=true;
			}
		}
		return flag;
		
	}
}
