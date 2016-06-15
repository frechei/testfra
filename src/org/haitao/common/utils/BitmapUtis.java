package org.haitao.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.haitao.common.utils.FileUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**   
 * 压缩
* @Description: 图片压缩使用
* @author wang   
* @date 2015-8-5 下午6:06:25 
* @version V1.0   
*/
public class BitmapUtis {
	

	/**
	 * 原生异步图片压缩  避免oom  
	 * @param picPath
	 * @param width 建议600
	 * @param height 建议800
	 * @param callBack
	 */
	public static void compress(final String picPath,final int width,final int height, final CompressCallback callBack) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Bitmap bitmap = decodeBitmap(picPath, width, height);
				if (bitmap!=null) {
					String outPath =FileUtils.getImagePath()+"/"+FileUtils.getRandomName()+".png";
					FileOutputStream fOut = null;
					try {
						fOut = new FileOutputStream(outPath);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
					} catch (IOException e1) {
						e1.printStackTrace();
						outPath =null;
					} finally {
						if (fOut != null) {
							try {
								fOut.flush();
								fOut.close();
								bitmap.recycle();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (outPath ==null && callBack!=null) {
							runOnUI(new Runnable() {
								
								@Override
								public void run() {
									callBack.onfail();
								}
							});
						}else if(outPath !=null && callBack!=null){
							final String path = outPath;
							runOnUI(new Runnable() {
								
								@Override
								public void run() {
									callBack.onsucces(path);
								}
							});
						}
					}
					
				}else{
					if (callBack!=null) {
						runOnUI(new Runnable() {
							
							@Override
							public void run() {
								callBack.onfail();
							}
						});
					}
				}
			}
		}).start();
	}
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.obj != null) {
				Runnable r = (Runnable) msg.obj;
				r.run();
			}
		};
	};
	/**
	 * 主线程回到使用
	 */
	private static void runOnUI(Runnable run) {
		Message me = handler.obtainMessage();
		me.obj = run;
		handler.sendMessage(me);
	}
	/**
	 * 原生同步图片压缩  避免oom  
	 * @param picPath
	 * @param width 建议600
	 * @param height 建议800
	 * @param callBack
	 */
	@SuppressWarnings("finally")
	public static String compress(final String picPath,final int width,final int height) {
		
		Bitmap bitmap = decodeBitmap(picPath, width, height);
		if (bitmap!=null) {
			String outPath =FileUtils.getImagePath()+"/"+FileUtils.getRandomName()+".png";
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(outPath);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			} catch (IOException e1) {
				e1.printStackTrace();
				outPath =null;
			} finally {
				if (fOut != null) {
					try {
						fOut.flush();
						fOut.close();
						bitmap.recycle();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return outPath;
			}
			
		}else{
			return null;
		}
	}
	/**
	 * 原生的图片压缩方法  经过多重算法 能够避免oom  同步方法
	 * @param picPath
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap decodeBitmap(String picPath,int width,int height){
	    
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 通过这个bitmap获取图片的宽和高
		BitmapFactory.decodeFile(picPath,options);
		options.inJustDecodeBounds = false;
		//options.inSampleSize = computeSampleSize(options, -1, width*height);
		//Log.e("inSampleSize===","inSampleSize==="+computeSampleSize2(options, -1, width*height));
		//Log.e("inSampleSize===","inSampleSize==1="+computeSampleSize(options,width, height));
		options.inSampleSize = computeSampleSize(options,width, height);
		// 这正的bitmap
		Bitmap bitmap =null;
		try {
			bitmap = BitmapFactory.decodeFile(picPath, options);
			bitmap =reviewPicRotate(bitmap,picPath);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		// ExifInterface exifInterface = new ExifInterface(); 
		return bitmap;
	}
	private static int computeSampleSize(BitmapFactory.Options options, 
            int width, int height) { 
		int roundedSize=1;
		double rateWidth=options.outWidth/width;
		double rateHeight=options.outHeight/height;
		if(rateWidth>1&& rateHeight>1){
			// 都大
			roundedSize=rateWidth>rateHeight?(int)rateWidth:(int)rateHeight;
			
		}else{
			// 两个都小于1
			if(rateWidth<1&& rateHeight<1){
				roundedSize=1;
			}else if(rateWidth<1){
				roundedSize=(int)rateHeight;
			}else{
				roundedSize=(int)rateWidth;
			}
		}
		// 400* 800 800 400
        return roundedSize; 
    } 
	
    /**
	 * 获取图片文件的信息，是否旋转了90度，如果是则反转
	 * @param bitmap 需要旋转的图片
	 * @param path   图片的路径
	 */
	public static Bitmap reviewPicRotate(Bitmap bitmap,String path){
		int degree = getPicRotate(path);
		if(degree!=0){
			Matrix m = new Matrix();  
			int width = bitmap.getWidth();  
			int height = bitmap.getHeight();  
			m.setRotate(degree); // 旋转angle度  
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,m, true);// 从新生成图片  
		}
		return bitmap;
	}
	
	/**
	 * 读取图片文件旋转的角度
	 * @param path 图片绝对路径
	 * @return 图片旋转的角度
	 */
	public static int getPicRotate(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	private static int computeSampleSize2(BitmapFactory.Options options, 
			int minSideLength, int maxNumOfPixels) { 
		int initialSize = computeInitialSampleSize(options, minSideLength, 
				maxNumOfPixels); 
		
		int roundedSize; 
		if (initialSize <= 8) { 
			roundedSize = 1; 
			while (roundedSize < initialSize) { 
				roundedSize <<= 1; 
			} 
		} else { 
			roundedSize = (initialSize + 7) / 8 * 8; 
		} 
		
		return roundedSize; 
	} 
    private static int computeInitialSampleSize(BitmapFactory.Options options, 
            int minSideLength, int maxNumOfPixels) { 
        double w = options.outWidth; 
        double h = options.outHeight; 
      
        int lowerBound = (maxNumOfPixels == -1) ? 1 : 
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels)); 
        int upperBound = (minSideLength == -1) ? 128 : 
                (int) Math.min(Math.floor(w / minSideLength), 
                Math.floor(h / minSideLength)); 
      
        if (upperBound < lowerBound) { 
            // return the larger one when there is no overlapping zone. 
            return lowerBound; 
        } 
      
        if ((maxNumOfPixels == -1) && 
                (minSideLength == -1)) { 
            return 1; 
        } else if (minSideLength == -1) { 
            return lowerBound; 
        } else { 
            return upperBound; 
        } 
    }  
	/**   
	* @Description: interface for image compress
	* @author wang   
	* @date 2015-8-8 下午3:33:21 
	* @version V1.0   
	*/
	public interface CompressCallback{
		void onsucces(String path);
		void onfail();
	}
	/**
	 * 异步bitmap
	 * @param dirPath
	 * @param bitmap
	 * @param recycle
	 * @return
	 */
	public static void saveBitmap(final String dirPath, final Bitmap bitmap,final boolean recycle,final SaveCallBack callBack) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				File file = new File(dirPath);
				FileOutputStream fOut = null;
				try {
					file.createNewFile();
					fOut = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
					if (callBack!=null) {
						callBack.success(dirPath);
					}
				} catch (IOException e1) {
					file = null;
					e1.printStackTrace();
					if (callBack!=null) {
						callBack.fail();
					}
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
			}
		}).start();

	}
	public interface SaveCallBack{
		void success(String path);
		void fail();
	}
	/**

	 * 将图片转换为圆角图片

	 * @param bitmap

	 * @param pixels  圆角的弧度

	 * @return

	 */
	public static Bitmap roundCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectf = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectf, pixels, pixels, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);   
		return output;
	}
	/**
	* 判断文件是否为图片文件(GIF,PNG,JPG) 
	* @param srcFileName
	* @return  
	*/
	public static boolean isImage(String srcFileName) {
		InputStream bin = null; // a jpg file
        try {
            bin = new FileInputStream(srcFileName);
            int b[] = new int[4];
            b[0] = bin.read();
            b[1] = bin.read();
            bin.skip(bin.available() - 2);
            b[2] = bin.read();
            b[3] = bin.read();
            bin.close();
            StringBuffer buf = new StringBuffer("");
            for (int e : b) {
                buf.append(e);
                buf.append(", ");
            }
            buf.delete(buf.length() - 2, buf.length());
            return "255, 216, 255, 217".equals(buf.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
	 
    /**
     * 获取文件类型
     * @param filePath
     * @return
     */
    public static String getImageType(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        if("FFD8FF".equals(value)){
        	return "jpg";
        } else if("FFD8FF".equals(value)){
        	return "jpg";
        } else if("47494638".equals(value)){
        	return "gif";
        } else if("424D".equals(value)){
        	return "bmp";
        }
        return value;
    }
    private static String bytesToHexString(byte[] src){
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
//    mFileTypes.put("FFD8FF", "jpg");
//    mFileTypes.put("89504E47", "png");
//    mFileTypes.put("47494638", "gif");
//    mFileTypes.put("49492A00", "tif");
//    mFileTypes.put("424D", "bmp");
//    //
//    mFileTypes.put("41433130", "dwg"); //CAD
//    mFileTypes.put("38425053", "psd");
//    mFileTypes.put("7B5C727466", "rtf"); //日记本
//    mFileTypes.put("3C3F786D6C", "xml");
//    mFileTypes.put("68746D6C3E", "html");
//    mFileTypes.put("44656C69766572792D646174653A", "eml"); //邮件
//    mFileTypes.put("D0CF11E0", "doc");
//    mFileTypes.put("5374616E64617264204A", "mdb");
//    mFileTypes.put("252150532D41646F6265", "ps");
//    mFileTypes.put("255044462D312E", "pdf");
//    mFileTypes.put("504B0304", "zip");
//    mFileTypes.put("52617221", "rar");
//    mFileTypes.put("57415645", "wav");
//    mFileTypes.put("41564920", "avi");
//    mFileTypes.put("2E524D46", "rm");
//    mFileTypes.put("000001BA", "mpg");
//    mFileTypes.put("000001B3", "mpg");
//    mFileTypes.put("6D6F6F76", "mov");
//    mFileTypes.put("3026B2758E66CF11", "asf");
//    mFileTypes.put("4D546864", "mid");
//    mFileTypes.put("1F8B08", "gz");
//    mFileTypes.put("", "");
//    mFileTypes.put("", "");
}
