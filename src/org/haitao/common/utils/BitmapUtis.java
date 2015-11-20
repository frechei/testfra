package org.haitao.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.haitao.common.utils.FileUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

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
		options.inSampleSize = computeSampleSize(options, -1, width*height);
		// 这正的bitmap
		Bitmap bitmap =null;
		try {
			bitmap = BitmapFactory.decodeFile(picPath, options);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
		return bitmap;
	}
	private static int computeSampleSize(BitmapFactory.Options options, 
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
}
