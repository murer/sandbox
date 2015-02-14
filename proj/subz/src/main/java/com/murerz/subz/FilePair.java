package com.murerz.subz;

public class FilePair implements Comparable<FilePair> {

	private SubzFile first;

	private SubzFile second;

	public SubzFile getFirst() {
		return first;
	}

	public FilePair setFirst(SubzFile first) {
		this.first = first;
		return this;
	}

	public SubzFile getSecond() {
		return second;
	}

	public FilePair setSecond(SubzFile second) {
		this.second = second;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
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
		FilePair other = (FilePair) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

	public int compareTo(FilePair o) {
		if (equals(o)) {
			return 0;
		}
		int ret = first.compareTo(o.first);
		if (ret == 0) {
			ret = second.compareTo(o.second);
		}
		if (ret == 0) {
			ret = first.compareTo(o.second);
		}
		if (ret == 0) {
			ret = second.compareTo(o.first);
		}
		return ret;
	}

	@Override
	public String toString() {
		return "[" + Util.name(first) + " " + Util.human(first) + " - " + Util.name(second) + " " + Util.human(second) + "]";
	}

	public SubzFile getVideo() {
		if (first.isSub() && second.isSub()) {
			throw new RuntimeException("wrong: " + toString());
		}
		if (first.isSub()) {
			return second;
		}
		if (second.isSub()) {
			return first;
		}
		throw new RuntimeException("wrong: " + toString());
	}

	public SubzFile getSub() {
		if (first.isSub() && second.isSub()) {
			throw new RuntimeException("wrong: " + toString());
		}
		if (first.isSub()) {
			return first;
		}
		if (second.isSub()) {
			return second;
		}
		throw new RuntimeException("wrong: " + toString());
	}

}
