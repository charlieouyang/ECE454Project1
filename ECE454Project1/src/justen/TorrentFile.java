package justen;

import java.io.File;
import java.nio.file.Files;

public class TorrentFile {
	private File file;
	private long fileSize;
	private long numChunks;
	private ChunkManager[] chunkArray;
	
	public TorrentFile(File f)
	{
		file = f;
		fileSize = file.length();
		numChunks = (long) Math.ceil(fileSize/Constants.CHUNK_SIZE);
	}
	
	private void partitionFileToChunks()
	{
		chunkArray = new ChunkManager[(int) numChunks];
	}
	
	public long getFileSize()
	{
		return fileSize;
	}
}
