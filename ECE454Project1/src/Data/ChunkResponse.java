package Data;

public class ChunkResponse {
	private String fileName;
	private int chunkNumber;
	private byte[] data;
	
	public ChunkResponse(String fileName, int chunkNumber, byte[] data){
		this.fileName = fileName;
		this.chunkNumber = chunkNumber;
		this.data = data;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public int getChunkNumber(){
		return chunkNumber;
	}
	
	public byte[] getData(){
		return data;
	}
}
