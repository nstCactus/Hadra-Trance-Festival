/*
 * Copyright 2013 Yohann Bianchi
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or see <http://www.gnu.org/licenses/>.
 */

package com.zion.htf.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.zion.htf.Application;

import java.lang.ref.WeakReference;

public class AsyncDrawable extends BitmapDrawable{
	private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
	private LruCache<String, Bitmap> memoryCache;

	public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask){
		super(res, bitmap);
		this.bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);

		// Initialize memory cache with a size equal to 1/8 of max available VM memory, in kB
		final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		this.memoryCache = new LruCache<String, Bitmap>(cacheSize){
			@Override
			public int sizeOf(String key, Bitmap bitmap){
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	public BitmapWorkerTask getBitmapWorkerTask(){
		return this.bitmapWorkerTaskReference.get();
	}

	public static void loadBitmap(int resId, ImageView imageView, int width, int height){
		if(cancelPotentialWork(resId, imageView)){
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(Application.getContext().getResources(), BitmapWorkerTask.placeHolderBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(resId, width, height);
		}
	}

	public static void loadBitmap(int resId, ImageView imageView){
		loadBitmap(resId, imageView, 100, 100);
	}

	public static boolean cancelPotentialWork(int data, ImageView imageView){
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if(bitmapWorkerTask != null){
			final int bitmapData = bitmapWorkerTask.data;
			if(bitmapData != data){
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			}
			else{
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was cancelled
		return true;
	}

	public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView){
		if(imageView != null){
			final Drawable drawable = imageView.getDrawable();
			if(drawable instanceof AsyncDrawable){
				final AsyncDrawable asyncDrawable = (AsyncDrawable)drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			this.memoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return this.memoryCache.get(key);
	}
}
