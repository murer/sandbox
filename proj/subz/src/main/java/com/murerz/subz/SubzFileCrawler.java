package com.murerz.subz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubzFileCrawler extends FileCrawler {

	private List<SubzFile> files = new ArrayList<SubzFile>();

	@Override
	protected void found(File file) {
		SubzFile subzFile = new SubzFile().setFile(file);
		System.out.println(subzFile);
		files.add(subzFile);
	}

	public List<SubzFile> getFiles() {
		return files;
	}

}
