package com.murerz.subz;

import java.io.File;

public abstract class FileCrawler {

	public void crawl(File... file) {
		for (File f : file) {
			if (!f.exists()) {
				continue;
			}
			String name = f.getName();
			if (name.equals("..") || name.equals("_subz")) {
				continue;
			}

			if (f.isDirectory()) {
				File[] list = f.listFiles();
				crawl(list);
			} else {
				found(f);
			}
		}
	}

	protected abstract void found(File file);

}
