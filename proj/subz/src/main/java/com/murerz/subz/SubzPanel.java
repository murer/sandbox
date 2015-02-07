package com.murerz.subz;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class SubzPanel extends JPanel {

	public SubzPanel() {
		setLayout(new BorderLayout());
	}

	public void showFiles(List<SubzFile> files) {
		Box box = Box.createHorizontalBox();
		box.add(createListPanel(files));
		box.add(Box.createHorizontalGlue());
		add(box, BorderLayout.CENTER);
	}

	private JScrollPane createListPanel(List<SubzFile> files) {
		JList<String> list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (SubzFile subzFile : files) {
			model.addElement("abc");
		}
		model.addElement("ab2c");
		model.addElement("abc3");
		list.setModel(model);
		return new JScrollPane(list);
	}

}
