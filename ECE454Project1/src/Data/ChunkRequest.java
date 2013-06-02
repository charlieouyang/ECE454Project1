package Data;

public class ChunkRequest {
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
