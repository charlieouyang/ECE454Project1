package justen;

import java.io.File;
import java.nio.file.Files;

public class TorrentFile {
	private File file;
	private long fileSize;
	private long numChunks;
	private ChunkManager[] chunkArray;
	private String fileName;
	
	public TorrentFile(File f) {
		file = f;
		fileName = f.getName();
		fileSize = file.length();
		numChunks = (long) Math.ceil(fileSize/Constants.CHUNK_SIZE);
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
