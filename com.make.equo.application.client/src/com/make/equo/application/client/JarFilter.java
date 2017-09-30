package com.make.equo.application.client;

import java.io.File;
import java.io.FilenameFilter;

public class JarFilter implements FilenameFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {
		if (name.toLowerCase().endsWith(".jar")) {
			return true;
		} else {
			return false;
		}
	}

}