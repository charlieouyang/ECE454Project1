package justen;

public class Chunk {
	private byte[] _data;
	
	public Chunk(byte[] data)
	{
		_data = data;
	}
	
	public byte[] getChunkData()
	{
		return _data;
	}
}
