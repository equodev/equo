package com.make.equo.node.packages.tests.common;

public class ChromiumSetup {
	
	public ChromiumSetup() {
		System.setProperty("swt.chromium.debug", "true");
		System.setProperty("swt.chromium.args",
				"--proxy-server=localhost:9896;--ignore-certificate-errors;--allow-file-access-from-files;--disable-web-security;--enable-widevine-cdm;--proxy-bypass-list=127.0.0.1");
	}
}
