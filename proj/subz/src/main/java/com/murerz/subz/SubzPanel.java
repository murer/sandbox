package com.murerz.subz;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class SubzPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public SubzPanel() {
		setLayout(new BorderLayout());
	}

	public void showFiles(List<File> files) {
		Box box = Box.createHorizontalBox();
		box.add(createListPanel(files));
		box.add(Box.createHorizontalGlue());
		add(box, BorderLayout.CENTER);
	}

	private JScrollPane createListPanel(List<File> files) {
		JList<File> list = new JList<File>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		DefaultListModel<File> model = new DefaultListModel<File>();
		for (File file : files) {
			model.addElement(file);
		}
		list.setModel(model);
		return new JScrollPane(list);
	}

}
