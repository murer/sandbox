package com.murerz.subz;

import java.io.File;

public class FilePair {

	private File first;

	private File second;

	public File getFirst() {
		return first;
	}

	public FilePair setFirst(File first) {
		this.first = first;
		return this;
	}

	public File getSecond() {
		return second;
	}

	public FilePair setSecond(File second) {
		this.second = second;
		return this;
	}

	@Override
	public String toString() {
		return "[" + Util.name(first) + " - " + Util.name(second) + "]";
	}

}
