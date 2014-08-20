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
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AbstractServiceProxyActivity extends ActionBarActivity implements ServiceConnection{
	protected IBinder serviceBinder = null;
	protected ServiceConnection serviceConnection = (ServiceConnection)this;
	private boolean serviceBound = false;

	private final Set<WeakReference<AbstractServiceProxyActivity.ServiceProxyObserver>> observers = new HashSet<WeakReference<AbstractServiceProxyActivity.ServiceProxyObserver>>();

	@Override
	public void onServiceConnected(ComponentName name, IBinder serviceBinder){
		this.serviceBinder = serviceBinder;
		this.serviceBound = true;

		// Notify observers
		for(WeakReference<AbstractServiceProxyActivity.ServiceProxyObserver> observer : this.observers){
			observer.get().onServiceRegistered(serviceBinder);
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name){
		this.serviceBinder = null;
		this.serviceBound = false;

		// Notify observers
		for(WeakReference<AbstractServiceProxyActivity.ServiceProxyObserver> observer : this.observers){
			observer.get().onServiceUnregistered(name);
		}
	}

	@Override
	protected void onPause(){
		if(null != this.serviceBinder) this.unbindService(this.serviceConnection);
		super.onPause();
	}

	public void registerObserver(AbstractServiceProxyActivity.ServiceProxyObserver observer){
		Log.v("AbstractServiceProxyActivity", String.format("Number of observers = %d", this.observers.size()));
		if(!this.observerAlreadyExists(observer)) this.observers.add(new WeakReference<AbstractServiceProxyActivity.ServiceProxyObserver>(observer));
	}

	/**
	 * Checks if an observer is already register
	 * @param observer the observer to look for in
	 * @return {@code true} if the observer is already registered, {@code false} otherwise
	 */
	private boolean observerAlreadyExists(AbstractServiceProxyActivity.ServiceProxyObserver observer){
		boolean exists = false;
		Iterator<WeakReference<AbstractServiceProxyActivity.ServiceProxyObserver>> iterator = this.observers.iterator();

		while(!exists && iterator.hasNext()){
			WeakReference<AbstractServiceProxyActivity.ServiceProxyObserver> observerReference = iterator.next();
			if(observerReference.get() == observer) exists = true;
		}

		return exists;
	}

	public void unregisterObserver(AbstractServiceProxyActivity.ServiceProxyObserver observer){
		for(WeakReference<AbstractServiceProxyActivity.ServiceProxyObserver> observerReference : this.observers){
			if (observerReference.get() == observer){
				this.observers.remove(observerReference);
				break;
			}
		}
	}

	public interface ServiceProxyObserver{
		void onServiceRegistered(IBinder serviceBinder);
		void onServiceUnregistered(ComponentName name);
	}
}
