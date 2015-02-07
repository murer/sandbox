package com.murerz.subz;

import java.util.Enumeration;

import javax.swing.DefaultListModel;

public class SubzModel<T> extends DefaultListModel<T> {

	private static final long serialVersionUID = 1L;

	@Override
	public void add(int index, T element) {
		throw new RuntimeException("unsupported");
	}

	@Override
	public void addElement(T element) {
		int idx = 0;
		Enumeration<T> elements = elements();
		while (elements.hasMoreElements()) {
			T x = elements.nextElement();
			if (compare(element, x) >= 0) {
				super.add(idx, element);
				return;
			}
			idx++;
		}
		super.addElement(element);
	}

	@SuppressWarnings("unchecked")
	private int compare(T element, T x) {
		Comparable<Object> c1 = (Comparable<Object>) element;
		Comparable<Object> c2 = (Comparable<Object>) x;
		return c1.compareTo(c2);
	}

}
