package com.murerz.subz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Subz {

	private String src;
	private JList<SubzFile> first;
	private JList<SubzFile> second;
	private JList<FilePair> result;
	private List<JButton> buttons = new ArrayList<JButton>();

	public Subz src(String src) {
		this.src = src;
		return this;
	}

	private List<SubzFile> crawl() {
		SubzFileCrawler crawler = new SubzFileCrawler();
		crawler.crawl(new File(src));
		return crawler.getFiles();
	}

	private void show() {
		if (!new File(src).isDirectory()) {
			throw new RuntimeException("it is not a directory: " + src);
		}
		List<SubzFile> files = crawl();
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

	private JPanel createListPanel(List<SubzFile> files) {
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

	private JList<SubzFile> createList(List<SubzFile> files) {
		JList<SubzFile> list = new JList<SubzFile>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		SubzModel<SubzFile> model = new SubzModel<SubzFile>();
		for (SubzFile file : files) {
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
		btnPanel.add(createResolveButton());
		ret.add(btnPanel, BorderLayout.NORTH);

		result = new JList<FilePair>();
		result.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		result.setLayoutOrientation(JList.VERTICAL);
		SubzModel<FilePair> model = new SubzModel<FilePair>();
		result.setModel(model);
		JScrollPane scroll = new JScrollPane(result);
		scroll.setPreferredSize(new Dimension(100, 70));
		ret.add(scroll, BorderLayout.CENTER);
		return ret;
	}

	private Component createResolveButton() {
		JButton ret = new JButton("Resolve");
		ret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Thread t = new Thread("resolver") {
					@Override
					public void run() {
						try {
							resolve();
						} finally {
							setButtonsEnabled(true);
						}
					}
				};
				setButtonsEnabled(false);
				t.start();
			}

		});
		buttons.add(ret);
		return ret;
	}

	private void setButtonsEnabled(boolean d) {
		for (JButton b : buttons) {
			b.setEnabled(d);
		}
	}

	private JButton createDownButton() {
		JButton ret = new JButton("down");
		ret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				down();
			}
		});
		buttons.add(ret);
		return ret;
	}

	private JButton createUpButton() {
		JButton ret = new JButton("up");
		ret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				up();
			}
		});
		buttons.add(ret);
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
		SubzFile first = this.first.getSelectedValue();
		SubzFile second = this.second.getSelectedValue();
		if (first == null || second == null || first.equals(second)) {
			return;
		}
		FilePair pair = new FilePair().setFirst(first).setSecond(second);
		listAdd(result, pair);
		listRemove(this.first, first, second);
		listRemove(this.second, first, second);
	}

	private <T> void listRemove(JList<T> list, T... elements) {
		SubzModel<T> model = (SubzModel<T>) list.getModel();
		for (T element : elements) {
			model.removeElement(element);
		}
	}

	private <T> void listAdd(JList<T> list, T... elements) {
		SubzModel<T> model = (SubzModel<T>) list.getModel();
		for (T element : elements) {
			model.addElement(element);
		}
	}

	private void resolve() {
		File destination = new File(src, "_subz");
		SubzModel<FilePair> pairs = (SubzModel<FilePair>) result.getModel();
		while (!pairs.isEmpty()) {
			FilePair pair = pairs.get(0);
			new SubzResolver().dest(destination).pair(pair).resolve();
			pairs.removeElementAt(0);
		}
	}

	public static void main(String[] args) {
		String path = ".";
		if (args.length > 0) {
			path = args[0];
			if ("--help".equals(path) || "-h".equals(path)) {
				System.out.println("Subz [path]");
				return;
			}
		}
		File file = new File(path);
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		new Subz().src(path).show();
	}

}
