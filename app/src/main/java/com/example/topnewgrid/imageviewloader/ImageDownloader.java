package com.example.topnewgrid.imageviewloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class ImageDownloader {

	private static final String LOG_TAG = ImageDownloader.class.getSimpleName();
	
//	private ImageCache imageCache;
	ImageCache imageCache;
	FileCache fileCache;
	
	public ImageDownloader(Context context) {
        imageCache = new ImageCache();
        System.out.println("经过2" );

        fileCache = new FileCache(context);
	}
	public void download(String url, ImageView imageView) {
		imageCache.resetPurgeTimer();
		Bitmap bitmap = imageCache.getBitmapFromCache(url);
		if(bitmap == null) {
			forceDownload(url, imageView);
		} else {
			cancelPotentialDownload(url, imageView);
            imageView.setImageBitmap(bitmap);
		}
	}
	private void forceDownload(String url, ImageView imageView) {
		if (url == null) {
            imageView.setImageDrawable(null);   //TODO 鏇存敼涓洪粯璁ゅ浘鐗�            return;
        }
		if(cancelPotentialDownload(url, imageView)) {
			 BitmapDownloaderTask task= new BitmapDownloaderTask(imageView) ;
			 task = new BitmapDownloaderTask(imageView);
             DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
             imageView.setImageDrawable(downloadedDrawable);
             task.execute(url);
		}
	}
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
		
		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
	}
	
	private Bitmap downloadBitmap(String url) {
		
		//try to get image from file cache
		File file = fileCache.getFromFileCache(url);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new FlushedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e1) {
			bitmap = null;
		}
		if(bitmap != null) {
			return bitmap;
		}
		//end of try
		
		final HttpClient httpClient = AndroidHttpClient.newInstance("image_downloader");
//		final HttpClient httpClient = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);
		
		try {
			HttpResponse httpResponse = httpClient.execute(getRequest);
			final int statusCode = httpResponse.getStatusLine().getStatusCode();
			
			if(statusCode != HttpStatus.SC_OK) {
				Log.w(LOG_TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}
			
			final HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity != null) {
				InputStream inputStream = null;
				try {
					inputStream = httpEntity.getContent();
					
					//save file to file cache
					fileCache.addToFileCache(url, inputStream);
					//end of save
					//TODO 
					return BitmapFactory.decodeStream(new FlushedInputStream(new FlushedInputStream(new FileInputStream(file))));
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if(inputStream != null) {
						inputStream.close();
					}
					httpEntity.consumeContent();
				}
			}
		}  catch (IOException e) {
            getRequest.abort();
            Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            getRequest.abort();
            Log.w(LOG_TAG, "Incorrect URL: " + url);
        } catch (Exception e) {
            getRequest.abort();
            Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
        } finally {
            if ((httpClient instanceof AndroidHttpClient)) {
                ((AndroidHttpClient) httpClient).close();
            }
        }
		return null;
	} // end of downloadBitmap
	
	
	/*
     * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
     */
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    } // end of FlushedInputStream
    
    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

		private String url;
		private WeakReference<ImageView> imageViewWeakReference;
		
	    public BitmapDownloaderTask(ImageView imageView) {
			imageViewWeakReference = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0]; 
			return downloadBitmap(url);
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if(isCancelled()) {
				bitmap = null;
			}
			
			//add bitmap to cache
			imageCache.addBitmapToCache(url, bitmap);
			
			if(imageViewWeakReference != null) {
				ImageView imageView = imageViewWeakReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				if(this == bitmapDownloaderTask) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	} // end of BitmapDownloaderTask
    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(Color.TRANSPARENT);

            bitmapDownloaderTaskReference =
                new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    } // end of DownloadedDrawable

}
