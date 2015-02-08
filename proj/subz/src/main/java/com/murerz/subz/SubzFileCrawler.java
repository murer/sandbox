package com.murerz.subz;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SubzFileCrawler extends FileCrawler {

	private static final Set<String> EXTS = new TreeSet<String>(Arrays.asList("mp4", "mov", "avi", "mkv", "264", "trec", "arf", "dv4", "h264", "dash", "ifv", "fbr", "h260", "dav",
			"avc", "trp", "fla", "webm", "n3r", "vvf", "dvr", "ssif", "g64", "yify", "vghd", "wlmp", "strg", "vse", "ifv", "mk3d", "flv", "vob", "tts", "swf", "pds", "vpj", "ts",
			"vcr", "ivf", "prproj", "bik", "rec", "tvs", "mts", "bup", "djanimations", "bvr", "usm", "wmv", "dxr", "camrec", "ogv", "moff", "dxa", "ismv", "m2ts", "h64", "irf",
			"3gp", "m2p", "vfo", "sdr2", "demo", "rcproject", "mjpeg", "hmt", "dat", "epm", "mswmm", "ezp", "avf", "f4v", "gifv", "pyv", "rdg", "mod", "vgz", "crec", "dvt", "asx",
			"vmlf", "wp3", "wma", "mpg", "m2t", "swi", "h3r", "dir", "es3", "m4v", "tp", "eti", "str", "lrec", "vp6", "ub1", "flm", "stx", "cine", "vep", "ravi", "mks", "sol",
			"rv", "scn", "kux", "prel", "mmv", "kmv", "mts1", "v264", "dfxp", "mvy", "ts4", "camproj", "bdmv", "hup", "dcf", "3gp_128x96", "rt4", "dmsm", "otrkey", "rvl", "vf",
			"mv", "nvc", "hlv", "swc", "mxf", "awlive", "h265", "tod", "viv", "vdm", "vid", "swz", "imovieevent", "mpg4", "stu", "urc", "mps", "dvr-ms", "m65", "265", "bdm",
			"evo", "rpl", "cct", "mxv", "tp0", "bix", "sdv", "3gpp", "vro", "ssm", "veg", "tgv", "nvl", "60d", "ratDVD", "scr", "mbv", "dscf", "gts", "nmm", "svi", "y4m", "mcv",
			"xpv", "slc", "vbc", "xvid", "hgd", "ivm", "bnk", "vs4", "asf", "mv4", "flm", "dv", "mtv", "mjpg", "dsf", "mpgv", "mmm", "amv", "xvw", "ml20", "jpv", "vcm", "mj2",
			"mvd", "wtv", "hmv", "asd", "iva", "mnv", "dlx", "wmv3", "san", "vpg", "theater", "fm2", "mmp", "dtv", "vp3", "flexolibrary", "3g2", "cpk", "avr", "mpeg", "m4f",
			"clpi", "ilm", "k3g", "dsm", "cvc", "rtv", "rdt", "dmsd", "rtsp", "vep4", "lsf", "uvf", "xba", "ev2", "apz", "jmf", "3gpp2", "imovieproj", "cam", "eye", "mvr", "ml20",
			"fmv", "p2", "exo", "mio", "3mm", "flc", "mpv", "cx3", "dv-avi", "fcp", "dvddata", "par", "dce", "mkv3D", "bts", "iis", "603", "rm", "h263", "dvm", "lsx", "w32",
			"rmvb", "eva", "vpd", "vdr", "rca", "sqf", "fvt", "mpcpl", "mjp2", "tmi", "tdt2", "m1v", "rki", "bdav", "nut", "swt", "div", "ppp", "ast", "gvi", "pxm", "mhg", "800",
			"mpeg1", "bsf", "ppj", "pmf", "xavc", "890", "bdtp", "mvv", "mpeg4", "mcf", "stk", "video", "mvp", "tvv", "fli", "movie", "gfp", "xmm", "h261", "3gp2", "ipr", "demo4",
			"divx", "gir", "fpb", "bay", "mv1", "dvdrip", "tx3g", "wm3", "xas", "TriDefMovie", "m2v", "moo", "rax", "cam", "032", "qt", "st4", "cip", "dmss", "ivs", "vc1",
			"viewlet", "r3d", "sec", "am7", "m-jpeg", "px", "mpe", "moov", "bs4", "uvs", "ncor", "gmm", "dvdmedia", "aut", "lsproj", "fsv", "sqz", "mgv", "noa", "smv", "hdmov",
			"h.263", "mp4v", "wm", "trn", "ogm", "film", "rec_part0", "aec", "nde", "aqt", "44", "m4e", "mvw", "sce", "vod", "bub", "pro", "gxf", "scm", "gmt", "mqv", "roq",
			"biz", "mp2v", "cin", "dof", "bbv", "seq", "vs2", "ogx", "flic", "hnm", "vg", "263", "rpl", "hav", "mxm", "jtv", "dpd", "sbst", "amc", "c2r", "mp7", "mpeg2",
			"mpgindex", "pvr", "scm", "l3", "htp", "vcd", "rvid", "dwz", "imovieproject", "dif", "zeg", "ssw", "ivr", "dpg", "mvf", "box", "htd", "l32", "tix", "pxv", "dmv",
			"vmm", "jmm", "vcl", "tivo", "tmf", "flvat", "m1s", "dc8", "mfv", "ask", "tpd", "smk", "swt", "mrd", "qtm", "vgx", "svd", "tv", "nuv", "flux", "xtodvd", "dmb", "qmx",
			"dvx", "mpj", "qtc", "jts", "xmv", "mjp", "crv", "sec", "bvz", "mpg2", "vivo", "m2s", "dvsd", "snd", "vp8", "vcs", "4xm", "pmp", "sfvidcap", "msh", "vprj", "svcd",
			"cmproj", "nvavi", "tstream", "qvt", "vbs", "vp7", "mys", "dmf", "thp", "trt", "rmd", "lza", "flh", "am2", "d2v", "xlmv", "rts", "vfw", "261", "rec_part1", "fli_",
			"s2e", "vtf", "h4v", "rvx", "drt", "scs", "met", "badongo", "ctd", "ctd", "mfp", "xwmv", "fbz", "snapfireshow", "siv", "mpv2", "olproj", "h263+", "dif", "mp", "zrb",
			"w3d", "h-263", "wsv", "qtvr", "m4u", "Common", "srt"));

	private List<SubzFile> files = new ArrayList<SubzFile>();

	@Override
	protected void found(File file) {
		if (filter(file)) {
			files.add(new SubzFile().setFile(file));
		}
	}

	private boolean filter(File file) {
		String ext = Util.ext(file);
		ext = Util.trim(ext);
		if (ext == null) {
			return false;
		}
		return EXTS.contains(ext);
	}

	public List<SubzFile> getFiles() {
		return files;
	}

}
