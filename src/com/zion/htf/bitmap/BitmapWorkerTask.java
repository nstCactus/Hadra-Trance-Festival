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
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;

import java.lang.ref.WeakReference;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap>{
	private static final String TAG = "BitmapWorkerTask";
	private final WeakReference<ImageView> imageViewReference;
	public int data = 0;
	public static Bitmap placeHolderBitmap;

	public BitmapWorkerTask(ImageView imageView){
		this.imageViewReference = new WeakReference<ImageView>(imageView);
		BitmapWorkerTask.placeHolderBitmap = BitmapFactory.decodeResource(Application.getContext().getResources(), R.drawable.no_image);
	}

	@Override
	protected Bitmap doInBackground(Integer... params){
		final int paramCount = params.length;
		final int width = paramCount > 1 ? params[1] : 100;
		final int height = paramCount > 2 ? params[2] : 100;
		final Bitmap bitmap = this.decodeSampledBitmapFromResource(Application.getContext().getResources(), params[0], width, height);
		AsyncDrawable.addBitmapToMemoryCache(params[0], bitmap);
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap){
		if(isCancelled()) bitmap = null;

		if(imageViewReference != null && bitmap != null){
			final ImageView imageView = imageViewReference.get();
			final BitmapWorkerTask bitmapWorkerTask = AsyncDrawable.getBitmapWorkerTask(imageView);
			if(this == bitmapWorkerTask && imageView != null) imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * Calculate the dimensions of a resampled image to fill a rectangle having the provided dimensions (preserving aspect ratio)
	 *
	 * @param options   The result of a call to BitmapFactory.decodeResource(). Must contain outHeight and outWidth
	 * @param reqWidth  The desired width
	 * @param reqHeight The desired height
	 * @return A percentage to apply to the original image dimension
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if(height > reqHeight || width > reqWidth){

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float)height / (float)reqHeight);
			final int widthRatio = Math.round((float)width / (float)reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * Creates a Bitmap from a resource
	 *
	 * @param res       A reference to Resource
	 * @param resId     The id of the resource to load
	 * @param reqWidth  The desired width
	 * @param reqHeight The desired height
	 * @return A Bitmap copy of the resource having resId sampled to fill a [reqWidth x reqHeight] rectangle (preserving aspect ratio)
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		if(BuildConfig.DEBUG) if(BuildConfig.DEBUG) Log.v(TAG, "Resampling resource");
		return BitmapFactory.decodeResource(res, resId, options);
	}

}
