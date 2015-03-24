package com.murerz.dsopz.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.text.MaskFormatter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static final Logger LOG = LoggerFactory.getLogger(Util.class);

	private static final String CONTENT_TYPE_REGEX = "^\\s*([^\\s]+)\\s*;\\s*charset\\s*=\\s*([^\\s]+)\\s*$";

	public static void close(AutoCloseable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (Exception e) {
				LOG.info("Error closing", e);
			}
		}
	}

	public static void close(Process process) {
		if (process != null) {
			try {
				process.destroy();
			} catch (Exception e) {
				LOG.error("error closing", e);
			}
		}
	}

	public static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (Exception e) {
				LOG.info("Error closing", e);
			}
		}
	}

	public static String encodeURI(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String dateToString(Date data, String format) {
		try {
			DateFormat df = new SimpleDateFormat(format);
			return df.format(data.getTime());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Date stringToDate(String data) {
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.parse(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> newList(Collection<String> c) {
		if (c == null) {
			return new ArrayList<String>();
		}
		return new ArrayList<String>(c);
	}

	public static List<String> newList(String[] c) {
		if (c == null) {
			return new ArrayList<String>();
		}
		List<String> arr = new ArrayList<String>();
		for (String string : c) {
			arr.add(string);
		}
		return arr;
	}

	public static String joinColl(String delimiter, Iterable<?> args) {
		StringBuilder ret = new StringBuilder();
		Iterator<?> it = args.iterator();
		while (it.hasNext()) {
			Object value = it.next();
			if (value == null) {
				value = "";
			}
			ret.append(value);
			if (it.hasNext()) {
				ret.append(delimiter);
			}
		}
		return ret.toString();
	}

	public static String join(String delimiter, Object... args) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			Object value = args[i];
			if (value == null) {
				value = "";
			}
			if (i > 0) {
				ret.append(delimiter);
			}
			ret.append(value);
		}
		return ret.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Collection<String>> T extract(T ret, String regex, String str) {
		if (ret == null) {
			ret = (T) new ArrayList<String>();
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			ret.add(matcher.group());
		}
		return ret;
	}

	public static Long max(Long... values) {
		if (values == null || values.length == 0) {
			return null;
		}
		Long ret = values[0];
		for (int i = 1; i < values.length; i++) {
			if (ret == null || (values[i] != null && ret < values[i])) {
				ret = values[i];
			}
		}
		return ret;
	}

	public static void exists(Collection<?> coll) {
		if (coll == null) {
			throw new RuntimeException("it is required");
		}
		for (Object object : coll) {
			if (object == null) {
				throw new RuntimeException("it is required");
			}
			if (object instanceof String && ((String) object).trim().length() == 0) {
				throw new RuntimeException("it is required");
			}
		}
	}

	public static String str(Object words) {
		if (words == null) {
			return null;
		}
		String ret = words.toString().trim();
		return ret.length() == 0 ? null : ret;
	}

	public static String stringfy(Object words) {
		String str = str(words);
		return str == null ? "" : str;
	}

	public static Properties classpathProperties(String path) {
		URL url = classpath(path);
		return properties(url);
	}

	public static Properties properties(URL url) {
		if (url == null) {
			return null;
		}
		Reader in = null;
		try {
			in = new InputStreamReader(new BufferedInputStream(url.openStream()), "utf-8");
			Properties ret = new Properties();
			ret.load(in);
			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(in);
		}
	}

	public static URL classpath(String path) {
		return Util.class.getResource(path);
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] readAll(InputStream in) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			copyAll(in, out);
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void copyAll(InputStream in, OutputStream out) {
		try {
			IOUtils.copyLarge(in, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String classpathString(String path, String enc) {
		URL url = classpath(path);
		if (url == null) {
			return null;
		}
		Reader in = null;
		try {
			in = new InputStreamReader(new BufferedInputStream(url.openStream()), "utf-8");
			String ret = readAll(in);
			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(in);
		}
	}

	public static String readAll(Reader in) {
		CharArrayWriter writer = new CharArrayWriter();
		copyAll(in, writer);
		return writer.toString();
	}

	public static void copyAll(Reader in, Writer out) {
		try {
			IOUtils.copy(in, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String read(Console console, String label, String def) {
		String ret = console.readLine(label, def);
		if (ret == null) {
			ret = "";
		}
		ret = ret.trim();
		if (ret.length() == 0) {
			return def;
		}
		return ret;
	}

	public static String read(URL url, String charset) {
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(url.openStream(), charset);
			return readAll(inputStreamReader);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (inputStreamReader != null) {
				close(inputStreamReader);
			}
		}
	}

	public static Properties argsToProperties(String[] args) {
		if (args == null || args.length % 2 != 0) {
			throw new RuntimeException("requires pairs of values");
		}
		Properties ret = new Properties();
		for (int i = 0; i < args.length; i += 2) {
			String name = str(args[i]);
			String value = str(args[i + 1]);
			if (name == null) {
				throw new RuntimeException("name is required");
			}
			ret.put(name, value);
		}
		return ret;
	}

	public static String[] trim(Object[] array) {
		String[] ret = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			ret[i] = stringfy(array[i]);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static <T> int compare(Comparable<T> a, Comparable<T> b) {
		if (a == null || b == null) {
			return a == b ? 0 : a == null ? -1 : 1;
		}
		return a.compareTo((T) b);
	}

	public static byte[] toBytes(String content, String enc) {
		try {
			return content.getBytes(enc);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getStack(Throwable e) {
		if (e == null) {
			return null;
		}
		StringWriter str = new StringWriter();
		PrintWriter writer = new PrintWriter(str);
		e.printStackTrace(writer);
		writer.close();
		return str.toString();
	}

	public static String toString(byte[] buffer, String enc) {
		try {
			return new String(buffer, enc);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isWindows() {
		String property = System.getProperty("os.name", "");
		return (property.toLowerCase().indexOf("win") >= 0);
	}

	public static boolean isIOS() {
		String property = System.getProperty("os.name", "");
		return (property.toLowerCase().indexOf("mac") >= 0);
	}

	public static void copyFile(File source, File destination) {
		if (!destination.exists()) {
			// destination.mkdirs();
		}
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;

		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destinationChannel = new FileOutputStream(destination).getChannel();

			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);

			if (sourceChannel != null && sourceChannel.isOpen()) {
				sourceChannel.close();
			}
			if (destinationChannel != null && destinationChannel.isOpen()) {
				destinationChannel.close();
			}
		} catch (FileNotFoundException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getOS() {
		if (isWindows()) {
			return "Windows";
		} else {
			return "Linux";
		}
	}

	public static String removeAccentuation(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		return (str.replaceAll("[^\\p{ASCII}]", ""));
	}

	public static int randomInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static Date[] beetwenHorario(Date date) {

		Date[] hoje = new Date[2];
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		hoje[0] = cal.getTime();

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		hoje[1] = cal.getTime();

		return hoje;
	}

	public static Date removerHorario(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static String formatCpfCNPJ(String str) {

		str = str.replaceAll("[\\D]", "");

		String pattern;
		if (str.length() <= 11) {
			str = String.format("%011d", Long.parseLong(str));
			pattern = "###.###.###-##";
		} else {
			str = String.format("%014d", Long.parseLong(str));
			pattern = "##.###.###/####-##";
		}

		try {
			MaskFormatter mask = new MaskFormatter(pattern);
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(str);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String padRight(String texto, int tamanhoCampo) {
		return String.format("%-" + tamanhoCampo + "s", texto);
	}

	public static String addZero(String texto, int tamanhoCampo) {
		while (texto.length() < tamanhoCampo) {
			texto = "0" + texto;
		}
		return texto;
	}

	public static Date addMonth(Date date, int mes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, mes);
		return calendar.getTime();
	}

	public static String formatCEP(String str) {

		str = str.replaceAll("[\\D]", "");
		try {
			str = String.format("%08d", Long.parseLong(str));
			String pattern = "#####-###";
			try {
				MaskFormatter mask = new MaskFormatter(pattern);
				mask.setValueContainsLiteralCharacters(false);
				return mask.valueToString(str);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> names(Enum<?>... values) {
		if (values == null) {
			return null;
		}
		List<String> ret = new ArrayList<String>(values.length);
		for (Enum<?> value : values) {
			ret.add(value == null ? null : value.name());
		}
		return ret;
	}

	public static String onlyNumbers(String str) {

		return str.replaceAll("[\\D]", "");
	}

	public static String extention(String path) {
		path = str(path);
		String ret = path.replaceAll("^.*\\.([^//.]*)$", "$1");
		return str(ret);
	}

	public static String generateString(String str, int size) {
		StringBuilder ret = new StringBuilder(str.length() * size);
		for (int i = 0; i < size; i++) {
			ret.append(str);
		}
		return ret.toString();
	}

	public static void validateUsascii(CharSequence sb) {
		for (int i = 0; i < sb.length(); i++) {
			int b = 0xFFFFFF & sb.charAt(i);
			if (b < 0 || b > 127) {
				throw new RuntimeException("invalid char: " + b);
			}
		}
	}

	public static void deleteChildsRecursively(File base) {
		try {
			File[] files = base.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					FileUtils.deleteDirectory(file);
				} else {
					if (!file.delete()) {
						throw new RuntimeException("can not be deleted: " + file);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void deleteRecursively(File f) {
		try {
			if (f.isDirectory()) {
				FileUtils.deleteDirectory(f);
			} else {
				if (!f.delete()) {
					throw new RuntimeException("can not be deleted: " + f);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void write(File meta, String format) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(meta);
			out.write(format.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(out);
		}
	}

	public static String mountURL(String url, String... params) {
		if (params == null) {
			params = new String[0];
		}
		StringBuilder builder = new StringBuilder();
		builder.append(url);
		if (params.length > 0) {
			builder.append("?");
		}
		builder.append(params(params));
		return builder.toString();
	}

	public static String params(String... params) {
		if (params == null) {
			params = new String[0];
		}
		StringBuilder builder = new StringBuilder();
		if (params.length % 2 != 0) {
			throw new RuntimeException("wrong: " + params.length);
		}
		for (int i = 0; i < params.length; i += 2) {
			String name = params[i];
			String value = params[i + 1];
			builder.append(name).append("=");
			builder.append(encodeURI(value));
			if (i + 2 < params.length) {
				builder.append("&");
			}
		}
		return builder.toString();
	}

	public static long randomPositiveLong() {
		long ret = new Random().nextLong();
		if (ret < 0) {
			ret = ret * -1;
		}
		return ret;
	}

	public static String nowZ() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());
	}

	public static byte[] gzip(String str, String charset) {
		try {
			byte[] bytes = str.getBytes(charset);
			return gzip(bytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] gzip(byte[] data) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			GZIPOutputStream out = new GZIPOutputStream(bout);
			out.write(data);
			out.close();
			return bout.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String gunzip(byte[] data, String encode) {
		try {
			ByteArrayInputStream bin = new ByteArrayInputStream(data);
			GZIPInputStream in = new GZIPInputStream(bin);
			byte[] ret = readAll(in);
			in.close();
			return Util.toString(ret, encode);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] gunzip(byte[] data) {
		try {
			ByteArrayInputStream bin = new ByteArrayInputStream(data);
			GZIPInputStream in = new GZIPInputStream(bin);
			byte[] ret = readAll(in);
			in.close();
			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static File gzip(File file) {
		File f = new File(file.getPath() + ".gz");
		InputStream in = null;
		OutputStream out = null;
		try {
			out = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
			in = new BufferedInputStream(new FileInputStream(file));
			copyAll(in, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(in);
			Util.close(out);
		}
		return f;
	}

	public static List<String> readLines(URL url, String charset) {
		InputStream in = null;
		try {
			in = url.openStream();
			return readLines(in, charset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(in);
		}
	}

	public static String readLinesAsString(InputStream in, String charset) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
			StringBuilder lines = new StringBuilder();
			String line = "";
			while (line != null) {
				line = reader.readLine();
				if (line != null) {
					lines.append(line).append('\n');
				}
			}
			return lines.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> readLines(InputStream in, String charset) {
		try {
			return readLines(new InputStreamReader(in, charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("resource")
	public static List<String> readLines(Reader reader) {
		BufferedReader br = null;
		if (reader instanceof BufferedReader) {
			br = (BufferedReader) reader;
		} else {
			br = new BufferedReader(reader);
		}
		try {
			List<String> lines = new ArrayList<String>();
			String line = "";
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					lines.add(line);
				}
			}
			return lines;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T get(Future<T> f) {
		try {
			return f.get();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	public static StringBuilder ascii(byte[] buffer) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < buffer.length; i++) {
			byte b = buffer[i];
			if (b < 20 || b > 127) {
				b = (char) '.';
			}
			char c = (char) (0xFFFF & b);
			sb.append(c);
		}
		return sb;
	}

	public static String mediaType(String contentType) {
		if (contentType == null) {
			return contentType;
		}
		return contentType.replaceAll(CONTENT_TYPE_REGEX, "$1");
	}

	public static String charset(String contentType) {
		if (contentType == null) {
			return contentType;
		}
		if (!contentType.contains("charset")) {
			return null;
		}
		return contentType.replaceAll(CONTENT_TYPE_REGEX, "$2");
	}

	public static Properties reqProperties(String name) {
		URL url = Util.class.getClassLoader().getResource(name);
		if (url == null) {
			throw new RuntimeException("properties not found: " + name);
		}
		return properties(url);
	}

	public static void require(Object obj, String msg) {
		if (obj == null) {
			throw new RuntimeException(msg);
		}
	}

	public static void close(HttpURLConnection url) {
		try {
			if (url != null) {
				url.disconnect();
			}
		} catch (Exception e) {
			LOG.error("error closing", e);
		}
	}

	public static String mountURL(String url, List<String> params) {
		if (params == null) {
			return mountURL(url);
		}
		return mountURL(url, params.toArray(new String[params.size()]));
	}

	public static Long parseDateLong(String pattern, String date) {
		Date ret = parseDate(pattern, date);
		return ret.getTime();
	}

	public static Date parseDate(String pattern, String date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			Date ret = formatter.parse(date);
			return ret;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String formatDate(String pattern, Long time) {
		return formatDate(pattern, new Date(time));
	}

	public static String formatDate(String pattern, Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String ret = formatter.format(date);
		return ret;
	}

	public static byte[] gunzip(URL url) {
		InputStream in = null;
		try {
			in = new BufferedInputStream(url.openStream());
			return gunzip(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(in);
		}
	}

	public static byte[] gunzip(InputStream in) {
		try {
			GZIPInputStream gz = new GZIPInputStream(in);
			return readAll(gz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static int indexOf(byte[] data, byte... values) {
		for (int i = 0; i < data.length; i++) {
			byte d = data[i];
			for (byte v : values) {
				if (d == v) {
					return i;
				}
			}
		}
		return -1;
	}

	public static int lastIndexOf(byte[] bytes, byte... values) {
		for (int i = bytes.length - 1; i >= 0; i--) {
			byte d = bytes[i];
			for (byte b : values) {
				if (d == b) {
					return i;
				}
			}
		}
		return -1;
	}

	public static byte[] cut(byte[] bytes, int length) {
		byte[] tmp = new byte[length];
		System.arraycopy(bytes, 0, tmp, 0, length);
		return tmp;
	}

	public static byte[] cutEnd(byte[] bytes, int offset) {
		byte[] tmp = new byte[bytes.length - offset];
		System.arraycopy(bytes, offset, tmp, 0, tmp.length);
		return tmp;
	}

	public static byte[] concat(byte[]... arrays) {
		int len = 0;
		for (byte[] array : arrays) {
			len += array.length;
		}
		byte[] ret = new byte[len];
		int offset = 0;
		for (byte[] array : arrays) {
			System.arraycopy(array, 0, ret, offset, array.length);
			offset += array.length;
		}
		return ret;
	}

	public static StringBuilder ascii(String str, String enc) {
		byte[] bytes = toBytes(str, enc);
		return ascii(bytes);
	}

	public static String formatDateIso(Long date) {
		return formatDateIso(new Date(date));
	}

	public static String formatDateIso(Date date) {
		return formatDate("yyyy-MM-dd'T'HH:mm:ss.SZ", date);
	}

	public static Throwable getRootCause(Throwable e) {
		if (e == null) {
			return null;
		}
		while (true) {
			Throwable cause = e.getCause();
			if (cause == null) {
				return e;
			}
			e = cause;
		}
	}

	public static String formatTime(Long end) {
		if (end == null) {
			return null;
		}
		return formatTime(new Date(end));
	}

	private static String formatTime(Date date) {
		return formatDate("HH:mm:ss.S", date);
	}

	public static void copyAll(InputStream in, File file) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			copyAll(in, out);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			close(out);
		}
	}

}
