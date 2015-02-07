package com.murerz.subz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.border.Border;

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
		SubzPanel panel = new SubzPanel();
		panel.showFiles(files);
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(300, 300);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new Subz().src("target").show();
	}

}
