package org.haitao.common.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 带进度条的WebView
 */

/**
 * @Description:带进度条的WebView
 */
public class ProgressWebView extends WebView {

	private ProgressBar progressbar;
	private Context context;
	public ProgressWebView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5, 0, 0));
		this.context=context;
		setWebChromeClient(new WebChromeClient());
		// 是否可以缩放
		getSettings().setSupportZoom(true);
		getSettings().setBuiltInZoomControls(true);
		setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{ //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    view.loadUrl(url);
                    return true;
                }
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					if (isInstall(intent)) {
						context.startActivity(intent );
						return true;
					}
				} catch (Exception e) {
					return false;
				}
                return true;
			}
		});
	}

	//判断app是否安装
	private boolean isInstall(Intent intent) {
		return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
	}

	/**
	* @param drawable
	* @return void    返回类型
	*/
	public void setProgressDrawable(Drawable drawable ){
		progressbar.setProgressDrawable(drawable);
		addView(progressbar);
	}
	/**
	 * @param drawable
	 * @return void    返回类型
	 */
	public void setProgressDrawableInt(int drawable ){
		progressbar.setProgress(drawable);
		addView(progressbar);
	}
	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(GONE);
			} else {
				if (progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
//CookieSyncManager.createInstance(this);  
//CookieManager cookieManager = CookieManager.getInstance();  
//cookieManager.setAcceptCookie(true);  
//String url = "http://testcookie.mobile.xxx.com/";  
//cookieManager.setCookie(url, "UID=EBFDA984906B62C444931EA0");  
//cookieManager.setCookie(url, "SID=EBFDA984906BEE91F90362C444931EA0");  
//cookieManager.setCookie(url, "PSTM=14572770");  
//cookieManager.setCookie(url, "TTDSS=Hl3NVU0N3ltZm9OWHhubHVQZW1BRThLdGhLaFc5TnVtQWd1S2g1REcwNVhTS3RXQVFBQ");  
//if (Build.VERSION.SDK_INT < 21) {  
//    CookieSyncManager.getInstance().sync();  
//} else {  
//    CookieManager.getInstance().flush();  
//} 
