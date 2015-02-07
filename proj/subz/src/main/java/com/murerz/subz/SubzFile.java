package com.murerz.subz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubzFile {

	private File file;

	private List<Long> numbers;

	public File getFile() {
		return file;
	}

	public SubzFile setFile(File file) {
		this.file = file;
		this.numbers = Util.capture(file.getName(), "\\d+");
		return this;
	}

	public List<Long> getNumbers() {
		return numbers;
	}

	@Override
	public String toString() {
		return "[" + file + " " + numbers + "]";
	}

	public ArrayList<Long> copyNumbers() {
		return new ArrayList<Long>(numbers);
	}

}
