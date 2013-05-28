package justen;

public class Status {

	private int numFiles;
	float[] local;
	float[] system;
	int[] leastReplication;
	float[] weightedLeastReplication;
	
	public Status()
	{
		local = new float[100];
		system = new float[100];
		leastReplication = new int[100];
		weightedLeastReplication = new float[100];
	}
	
	public int numberOfFiles()
	{
		return numFiles;
	}
	
	public float fractionPresentLocally(int fileNumber)
	{
		return -1;
	}
	
	public float fractionPresent(int fileNumber)
	{
		return -1;
	}
	
	public int minimumReplicationLevel(int fileNumber)
	{
		return -1;
	}
	
	
}
