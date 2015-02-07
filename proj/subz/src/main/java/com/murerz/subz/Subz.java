package com.murerz.subz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.util.Arrays;
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
		box.add(createList(files));
		box.add(Box.createVerticalGlue());
		box.add(createList(files));
		box.add(Box.createVerticalGlue());
		ret.add(box, BorderLayout.CENTER);
		return ret;
	}

	private JScrollPane createList(List<File> files) {
		JList<File> list = new JList<File>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		DefaultListModel<File> model = new DefaultListModel<File>();
		for (File file : files) {
			model.addElement(file);
		}
		list.setModel(model);
		JScrollPane ret = new JScrollPane(list);
		return ret;
	}

	private JPanel createControl() {
		JPanel ret = new JPanel();
		ret.setLayout(new BorderLayout());

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		btnPanel.add(new JButton("down"));
		btnPanel.add(new JButton("up"));
		ret.add(btnPanel, BorderLayout.NORTH);

		JList<FilePair> list = new JList<FilePair>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		DefaultListModel<FilePair> model = new DefaultListModel<FilePair>();
		list.setModel(model);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(100, 70));
		ret.add(scroll, BorderLayout.CENTER);
		return ret;
	}

	public static void main(String[] args) {
		new Subz().src("src/test/resources/com/murerz/subz").show();
	}

}
