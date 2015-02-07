package com.murerz.subz;

import java.io.File;
import java.util.Arrays;

public abstract class FileCrawler {

	public void crawl(File... file) {
		System.out.println("t " + Arrays.toString(file));
		for (File f : file) {
			System.out.println("x ");
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
