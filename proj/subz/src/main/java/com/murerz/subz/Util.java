package com.murerz.subz;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	public static List<Long> capture(String srt, String group) {
		Pattern p = Pattern.compile(group);
		Matcher m = p.matcher(srt);
		ArrayList<Long> ret = new ArrayList<Long>();
		while (m.find()) {
			ret.add(new Long(m.group()));
		}
		return ret;
	}

}
