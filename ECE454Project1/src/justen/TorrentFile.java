package justen;

import java.io.File;

public class TorrentFile {
	private File file;
	private long fileSize;
	private int numChunks;
	private String fileName;
	
	public TorrentFile(File f) {
		file = f;
		fileName = f.getName();
		fileSize = file.length();
		double tempVal = (double)fileSize/(double)Constants.CHUNK_SIZE;
		double ceilVal = Math.ceil(tempVal);
		numChunks = (int)ceilVal;
	}
	
	public String getChunkName(int i) {
		if (numChunks <= i) 
			return null;
		return fileName + "_chunk_" + i;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	
	public String getFileName() {
		return file.getName();
	}
	
	public long getNumberOfChunks() {
		return numChunks;
	}
}
