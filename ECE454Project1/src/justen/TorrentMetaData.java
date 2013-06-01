package justen;

public class TorrentMetaData {
	
	private String fileName;
	private int numChunks;
	
	public TorrentMetaData(TorrentFile tFile) {
		fileName = tFile.getFileName();
		numChunks = (int)tFile.getNumberOfChunks();
	}
}
