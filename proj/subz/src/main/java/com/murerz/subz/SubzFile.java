package com.murerz.subz;

import java.io.File;

public class SubzFile implements Comparable<SubzFile> {

	private File file;

	public File getFile() {
		return file;
	}

	public SubzFile setFile(File file) {
		this.file = file;
		return this;
	}

	@Override
	public String toString() {
		return file == null ? "null" : "" + Util.numbers(file) + " " + file.getName() + " [" + file.getParent() + "]";
	}

	public int compareTo(SubzFile o) {
		if (equals(o)) {
			return 0;
		}
		int ret = file.getName().compareTo(o.getFile().getName());
		if (ret == 0) {
			ret = file.compareTo(o.getFile());
		}
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubzFile other = (SubzFile) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

	public String getFileName() {
		String name = file.getName();
		int idx = name.lastIndexOf(".");
		String ret = name.substring(0, idx);
		if (ret.length() == 0) {
			throw new RuntimeException("wrong: " + file);
		}
		return ret;
	}

	public String getFileExt() {
		String name = file.getName();
		int idx = name.lastIndexOf(".");
		String ret = name.substring(idx + 1);
		if (ret.length() == 0) {
			throw new RuntimeException("wrong: " + file);
		}
		return ret;
	}

}
