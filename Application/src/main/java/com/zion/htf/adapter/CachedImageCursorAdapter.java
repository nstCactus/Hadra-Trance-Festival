/*
 *
 *     Copyright 2013-2014 Yohann Bianchi
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *     or see <http://www.gnu.org/licenses/>.
 *
 */

package com.zion.htf.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.zion.htf.R;

import java.lang.ref.WeakReference;

public abstract class CachedImageCursorAdapter extends CursorAdapter{
    protected final Context context;
    private final LruCache<String, Bitmap> memoryCache;

    public CachedImageCursorAdapter(Context context, Cursor cursor, boolean autoRequery){
        super(context, cursor, autoRequery);

        this.context = context;

        // Get memory class of this device, exceeding this amount will throw an OutOfMemory exception.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        this.memoryCache = new LruCache<String, Bitmap>( maxMemory / 8){
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in bytes rather than number of items.
                return bitmap.getByteCount();
            }
        };
    }


    public void loadBitmap(int resId, ImageView imageView){
        if(CachedImageCursorAdapter.cancelPotentialWork(resId, imageView)){
            final CachedImageCursorAdapter.BitmapWorkerTask task = new CachedImageCursorAdapter.BitmapWorkerTask(imageView);
            imageView.setBackgroundResource(R.drawable.no_image);
            task.execute(resId);
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<CachedImageCursorAdapter.BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, CachedImageCursorAdapter.BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            this.bitmapWorkerTaskReference = new WeakReference<CachedImageCursorAdapter.BitmapWorkerTask>(bitmapWorkerTask);
        }

        public CachedImageCursorAdapter.BitmapWorkerTask getBitmapWorkerTask() {
            return this.bitmapWorkerTaskReference.get();
        }
    }

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final CachedImageCursorAdapter.BitmapWorkerTask bitmapWorkerTask = CachedImageCursorAdapter.getBitmapWorkerTask(imageView);

        if (null != bitmapWorkerTask) {
            final int bitmapData = bitmapWorkerTask.data;
            if (bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was
        // cancelled
        return true;
    }

    private static CachedImageCursorAdapter.BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (null != imageView) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof CachedImageCursorAdapter.AsyncDrawable) {
                final CachedImageCursorAdapter.AsyncDrawable asyncDrawable = (CachedImageCursorAdapter.AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if(null == this.getBitmapFromMemCache(key)){
            this.memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key){
        return this.memoryCache.get(key);
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        public int data = 0;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView){
            // Use a WeakReference to ensure the ImageView can be garbage
            // collected
            this.imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params){
            this.data = params[0];
            final Bitmap bitmap = CachedImageCursorAdapter.decodeSampledBitmapFromResource(CachedImageCursorAdapter.this.context.getResources(), this.data, 100, 100);
            CachedImageCursorAdapter.this.addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(null != this.imageViewReference && null != bitmap) {
                final ImageView imageView = this.imageViewReference.get();
                if (null != imageView) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = CachedImageCursorAdapter.calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth){
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
