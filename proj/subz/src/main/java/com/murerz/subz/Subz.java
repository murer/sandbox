package com.murerz.subz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Subz {

	private String src;
	private JList<File> first;
	private JList<File> second;
	private JList<FilePair> result;

	public Subz src(String src) {
		this.src = src;
		return this;
	}

	private List<File> crawl() {
		SubzFileCrawler crawler = new SubzFileCrawler();
		crawler.crawl(new File(src));
		return crawler.getFiles();
	}

	private void show() {
		List<File> files = crawl();
		JFrame frame = new JFrame("Subz: " + src);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel = createListPanel(files);
		frame.add(panel, BorderLayout.CENTER);
		JPanel control = createControl();
		frame.add(control, BorderLayout.SOUTH);
		frame.pack();
		frame.setSize(300, 300);
		frame.setVisible(true);
	}

	private JPanel createListPanel(List<File> files) {
		JPanel ret = new JPanel();
		ret.setLayout(new BorderLayout());
		Box box = Box.createVerticalBox();
		first = createList(files);
		box.add(new JScrollPane(first));
		box.add(Box.createVerticalGlue());
		second = createList(files);
		box.add(new JScrollPane(second));
		box.add(Box.createVerticalGlue());
		ret.add(box, BorderLayout.CENTER);
		return ret;
	}

	private JList<File> createList(List<File> files) {
		JList<File> list = new JList<File>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		DefaultListModel<File> model = new DefaultListModel<File>();
		for (File file : files) {
			model.addElement(file);
		}
		list.setModel(model);
		return list;
	}

	private JPanel createControl() {
		JPanel ret = new JPanel();
		ret.setLayout(new BorderLayout());

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		btnPanel.add(createDownButton());
		btnPanel.add(createUpButton());
		ret.add(btnPanel, BorderLayout.NORTH);

		result = new JList<FilePair>();
		result.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		result.setLayoutOrientation(JList.VERTICAL);
		DefaultListModel<FilePair> model = new DefaultListModel<FilePair>();
		result.setModel(model);
		JScrollPane scroll = new JScrollPane(result);
		scroll.setPreferredSize(new Dimension(100, 70));
		ret.add(scroll, BorderLayout.CENTER);
		return ret;
	}

	private JButton createDownButton() {
		JButton ret = new JButton("down");
		ret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				down();
			}
		});
		return ret;
	}

	private JButton createUpButton() {
		JButton ret = new JButton("up");
		ret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				up();
			}
		});
		return ret;
	}

	private void up() {
		FilePair pair = this.result.getSelectedValue();
		if (pair == null) {
			return;
		}
		listAdd(this.first, pair.getFirst(), pair.getSecond());
		listAdd(this.second, pair.getFirst(), pair.getSecond());
		listRemove(this.result, pair);
	}

	private void down() {
		File first = this.first.getSelectedValue();
		File second = this.second.getSelectedValue();
		if (first == null || second == null || first.equals(second)) {
			return;
		}
		FilePair pair = new FilePair().setFirst(first).setSecond(second);
		listAdd(result, pair);
		listRemove(this.first, first, second);
		listRemove(this.second, first, second);
	}

	private <T> void listRemove(JList<T> list, T... elements) {
		DefaultListModel<T> model = (DefaultListModel<T>) list.getModel();
		for (T element : elements) {
			model.removeElement(element);
		}
	}

	private <T> void listAdd(JList<T> list, T... elements) {
		DefaultListModel<T> model = (DefaultListModel<T>) list.getModel();
		for (T element : elements) {
			model.addElement(element);
		}
	}

	public static void main(String[] args) {
		new Subz().src("src/test/resources/com/murerz/subz").show();
	}

}
