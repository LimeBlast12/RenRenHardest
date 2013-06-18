package helper;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

/**
 * 简易图片下载器，不带缓存，不依赖于上下文，根据url下载图片，输出Bitmap
 * @author daisy
 *
 */
public class ImageDownloader {
	@SuppressLint("NewApi")
	/**
	 * 下载图片
	 * @param url 图片地址
	 * @return 图片
	 */
	public static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.w("ImageDownloader", "Error while retrieving bitmap from "
					+ url, e);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}
}
