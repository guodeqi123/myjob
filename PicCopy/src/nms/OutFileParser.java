package nms;

import java.io.File;

public class OutFileParser {
	
	private String srcFilePath;
	private String srcFileName;

	public OutFileParser(String sss) {
		this.srcFilePath = sss;
		File file = new File(srcFilePath);
		this.srcFileName = file.getName().substring(0, file.getName().lastIndexOf("."));
	}

	public String getSrcFilePath() {
		return srcFilePath;
	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}

	public String getSrcFileName() {
		return srcFileName;
	}

	public void setSrcFileName(String srcFileName) {
		this.srcFileName = srcFileName;
	}
	
	
}
