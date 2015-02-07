package com.murerz.subz;

import java.io.File;

public abstract class FileCrawler {

	public void crawl(File... file) {
		for (File f : file) {
			if (!f.exists()) {
				continue;
			}
			String name = f.getName();
			if (name.equals("..")) {
				continue;
			}

			if (f.isDirectory()) {
				crawl(f.listFiles());
				return;
			}
			
			found(f);
		}
	}

	protected abstract void found(File file);

}
