package com.make.equo.application.server;

public class EquoHttpProxyExecutor implements Runnable {
	
	private volatile boolean exit = false;
	private EquoHttpProxy equoHttpProxy;
	
	public EquoHttpProxyExecutor(String url) {
		this.equoHttpProxy = new EquoHttpProxy(url);
	}

	@Override
	public void run() {
		while(!exit) {
			try {
				equoHttpProxy.startProxy();
			} catch (Exception e) {
				// TODO add proper logging...
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
        exit = true;
    }

	public boolean isStarted() {
		return equoHttpProxy.isStarted();
	}

	public String getAddress() {
		return equoHttpProxy.getAddress();
	}

}
