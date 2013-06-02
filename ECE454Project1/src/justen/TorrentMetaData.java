package justen;

public class TorrentMetaData {
	
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
}
