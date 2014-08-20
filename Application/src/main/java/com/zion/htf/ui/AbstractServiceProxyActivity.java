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

package com.zion.htf.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.zion.music.MediaPlayerService;

import gov.nasa.arc.mct.util.WeakHashSet;

public class AbstractServiceProxyActivity extends ActionBarActivity implements ServiceConnection{
	protected ServiceConnection serviceConnection = (ServiceConnection)this;
	private boolean serviceBound = false;

	private final WeakHashSet<ServiceProxyObserver> observers = new WeakHashSet<ServiceProxyObserver>();


	@Override
	public void onServiceConnected(ComponentName name, IBinder serviceBinder){
		this.serviceBound = true;
		MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder)serviceBinder;

		// Notify observers
		for(ServiceProxyObserver observer : this.observers){
			observer.onServiceConnectedToProxy(binder.getService());
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name){
		this.serviceBound = false;

		// Notify observers
		for(ServiceProxyObserver observer : this.observers){
			observer.onServiceConnectionLost(name);
		}
	}

	@Override
	protected void onPause(){
		super.onPause();
		Log.v("AbstractServiceProxyActivity", "Unbinding service");
		if(this.isServiceBound()){
			this.unbindService(this.serviceConnection);
		}
		else{
			Log.w("AbstractServiceProxyActivity", "Useless call");
		}
	}

	@Override
	public void unbindService(ServiceConnection connection){
		this.serviceBound = false;
		super.unbindService(connection);
	}

	@Override
	public boolean bindService(Intent service, ServiceConnection connection, int flags){
		this.serviceBound = true;
		return super.bindService(service, connection, flags);
	}

	/**
	 * Returns whether the service is currently bound
	 * @return {@code true} if the service is currently bound, {@code false} otherwise
	 */
	public boolean isServiceBound(){
		return this.serviceBound;
	}


	//////////////////////
	// BEGIN Observable //
	//////////////////////
	public void registerObserver(AbstractServiceProxyActivity.ServiceProxyObserver observer){
		this.observers.add(observer);
		Log.v("AbstractServiceProxyActivity", String.format("Number of observers (after adding) = %d", this.observers.size()));
	}

	public void unregisterObserver(AbstractServiceProxyActivity.ServiceProxyObserver observer){
		this.observers.remove(observer);
		Log.v("AbstractServiceProxyActivity", String.format("Number of observers (after removal) = %d", this.observers.size()));
	}
	////////////////////
	// END Observable //
	////////////////////


	public interface ServiceProxyObserver{
		void onServiceConnectedToProxy(MediaPlayerService service);
		void onServiceConnectionLost(ComponentName name);
	}
}
