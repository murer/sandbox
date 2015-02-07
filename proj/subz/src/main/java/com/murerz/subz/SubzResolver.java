package com.murerz.subz;

import java.io.File;

public class SubzResolver {

	private File dest;
	private FilePair pair;

	public SubzResolver dest(File destination) {
		this.dest = destination;
		return this;
	}

	public SubzResolver pair(FilePair pair) {
		this.pair = pair;
		return this;
	}

	public SubzResolver resolve() {
		SubzFile first = pair.getFirst();
		SubzFile second = pair.getSecond();

		String firstName = first.getFileName();
		String firstExt = first.getFileExt();
		String secondExt = second.getFileExt();

		File firstDest = new File(dest, firstName + "." + firstExt);
		File secondDest = new File(dest, firstName + "." + secondExt);

		dest.mkdirs();
		copy(first.getFile(), firstDest);
		copy(second.getFile(), secondDest);
		return this;
	}

	private void copy(File from, File to) {
		System.out.println("copy " + to + " " + Util.human(from.length()));
		Util.copy(from, to);
	}

}
