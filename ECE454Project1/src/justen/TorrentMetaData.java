package justen;

import java.io.Serializable;

public class TorrentMetaData implements Serializable{
	
	private String fileName;
	private int numChunks;
	
	public TorrentMetaData(String name, int chunks) {
		fileName = name;
		numChunks = chunks;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public int getNumberOfChunks() {
		return numChunks;
	}
	
	public String getChunkName(int i) {
		if (numChunks <= i) 
			return null;
		return fileName + "_chunk_" + i;
	}
}
