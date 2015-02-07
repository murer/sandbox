package com.murerz.subz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubzFileCrawler extends FileCrawler {

	private List<SubzFile> files = new ArrayList<SubzFile>();

	@Override
	protected void found(File file) {
		System.out.println(file);
		files.add(new SubzFile().setFile(file));
	}

	public List<SubzFile> getFiles() {
		return files;
	}

}
