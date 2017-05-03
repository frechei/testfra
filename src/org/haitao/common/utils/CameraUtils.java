package org.haitao.common.utils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.VideoColumns;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

/**
 * <b>decription:</b> 相机工具 <br>
 * <b>creat:</b>  2016-8-5 下午3:41:24 
 * @author haitao
 * @version 1.0
 */
public class CameraUtils {

	public static final int TAKE_PICTURE = 1;
	public static final int TAKE_VIDEO = 2;
	public static final int LOCAL_PICTURE = 3;
	public static final int LOCAL_VIDEO = 4;
	public static final int REQUEST_CAMERA_PERMISSION = 5;
	public static String photoPath = null;

//	<provider
//    android:name="android.support.v4.content.FileProvider"
//    android:authorities="${applicationId}.fileprovider"
//    android:grantUriPermissions="true"
//    android:exported="false">
//    <meta-data
//        android:name="android.support.FILE_PROVIDER_PATHS"
//        android:resource="@xml/file_paths" />
//</provider>
//	<?xml version="1.0" encoding="utf-8"?>
//	<resources>
//	    <paths>
//	        <root-path path="" name="camera_photos" />
//	    </paths>
//	</resources>
	/**
	 * 7.0 需要配置
	* @param ac
	* @param path 照片存储路径
	* @param pictureName 照片名字
	* @return    参数
	* @return String 
	*/
	public static String takePhoto(Activity ac, Fragment fragment,String path, String pictureName) {
		if(!hasCamera(ac)){
			ToastUtil.shortShowCustom("没有相机");
			return "";
		}
		if (FileUtils.makeDir(path)) {
			photoPath = path + "/" + pictureName;
			AppLog.e(photoPath);
			if(android.os.Build.VERSION.SDK_INT<24){
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
				Uri imageUri = Uri.fromFile(new File(path, pictureName));
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				if(null!=fragment){
					fragment.startActivityForResult(cameraIntent, TAKE_PICTURE);
				}else{
					ac.startActivityForResult(cameraIntent, TAKE_PICTURE);
				}
			}else{
				 File file=new File(photoPath);
				if (!file.getParentFile().exists())file.getParentFile().mkdirs();
				Uri imageUri = FileProvider.getUriForFile(ac,ac.getPackageName()+".fileprovider", file);//通过FileProvider创建一个content类型的Uri
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
				if(null!=fragment){
					fragment.startActivityForResult(intent, TAKE_PICTURE);
				}else{
					ac.startActivityForResult(intent, TAKE_PICTURE);
				}
			}
			
		} else {
			ToastUtil.shortShowCustom("SD卡不可用");
			photoPath = null;
			return null;
		}
		return path + "/" + pictureName;

	}
	/**
	 * @param ac
	 * @param path 照片存储路径
	 * @param pictureName 照片名字
	 * @return    参数
	 * @return String 
	 */
	public static String takePhoto(Activity ac, String path, String pictureName) {
		return takePhoto(ac,null,path,pictureName);
	}

	/**
	 * 拍照  fix 没有相机的bug
	 * @param ac
	 * @return
	 */
	public static String takePhoto(Activity ac) {
		return takePhoto(ac, FileUtils.getAppPath(), FileUtils.getRandomName() + ".png");
	}
	/**
	 * 拍照  fix 没有相机的bug
	 * @param ac
	 * @return
	 */
	public static String takePhoto(Activity ac,Fragment fragment) {
		return takePhoto(ac,fragment, FileUtils.getAppPath(), FileUtils.getRandomName() + ".png");
	}
	/**
	* 是否有相机
	* @param ac
	* @return boolean 
	*/
	public static boolean hasCamera(Activity ac) {
		PackageManager packageManager =ac.getPackageManager();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> list =packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return (list!=null && list.size()>0);
	}

	 public static String getRandomName() {
	        return TimeUtil.dateToString(new Date(), "yyyy_MM_dd_HH_mm_ss") + getRandom(0, 1000);
	  }
		public static int getRandom(int min,int max){
			 Random random = new Random();
			return random.nextInt(max)%(max-min+1) + min;
		}
	/**
	 * 打开相机录像
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

/*	*//**
	 * 获取系统图片地址 要判断是否是null
	 * 
	 * @param ac
	 * @param data
	 * @return
	 *//*
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
				
				 * if (originalUri!=null && originalUri.toString().length()>11
				 * && originalUri.toString().startsWith("file://") ) { String
				 * ext
				 * =originalUri.toString().substring(originalUri.toString().length
				 * ()-3); if (ext.equalsIgnoreCase("jpg") ||
				 * ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("gif")) {
				 * path = originalUri.toString().substring(7); }else{
				 * ToastUtil.shortShowCustom("不是图片"); } }else{
				 * ToastUtil.shortShowCustom("文件不支持"); }
				 
				if (originalUri != null) {
					path = originalUri.getPath();
				} else {
					ToastUtil.shortShowCustom("不是图片");
				}
			} else {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path = cursor.getString(column_index);
				Log.e("cursor", cursor.toString());
				if (path == null) {
					ToastUtil.shortShowCustom("该图库不支持");
				}
				cursor.close();
			}
		}
		return path;
	}*/
    @SuppressLint("NewApi")
	public static String getPhotoByIntent(final Context context, Intent data) {

		if (data == null) {
			return null;
		} 
		String[] proj = { MediaStore.Images.Media.DATA };
		Uri uri = data.getData();
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    
	/**
	 * 获取图库视屏地址 要判断是否是null
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
						ToastUtil.shortShowCustom("不是支持的视屏");
					}
				} else {
					ToastUtil.shortShowCustom("不是支持的视屏");
				}
			} else {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path = cursor.getString(column_index);
				if (path == null) {
					ToastUtil.shortShowCustom("获取视频路径失败");
				}
				cursor.close();
			}
		}
		return path;
	}

	/**
	 * 获取录像的地址 要判断是否是null
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
	public static boolean isVideoBigger(String path,int sizeM ){
		boolean flag = false;
		if (!FileUtils.fileExit(path)) {
			ToastUtil.shortShowCustom("文件不存在");
		}else{
			if (FileUtils.getDirSize(new File(path))>sizeM) {
				ToastUtil.shortShowCustom("视频太大,请选择小于"+sizeM+"的视频");
			}else{
				flag=true;
			}
		}
		return flag;
		
	}
}
