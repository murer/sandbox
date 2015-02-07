package com.murerz.subz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubzFileCrawler extends FileCrawler {

	private List<File> files = new ArrayList<File>();

	@Override
	protected void found(File file) {
		System.out.println(file);
		files.add(file);
	}

	public List<File> getFiles() {
		return files;
	}

}
