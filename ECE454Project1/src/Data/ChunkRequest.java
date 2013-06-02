package Data;

import java.io.Serializable;

public class ChunkRequest implements Serializable{
	private String fileName;
	private int chunkNumber;
	
	public ChunkRequest(String fileName, int chunkNumber){
		this.fileName = fileName;
		this.chunkNumber = chunkNumber;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	public int getChunkNumber(){
		return this.chunkNumber;
	}
}
