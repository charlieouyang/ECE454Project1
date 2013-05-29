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
	
	/**
	 * @param fileNumber
	 * @return Fraction of the file present locally (chunks on peer/total number of chunks in file)
	 */
	public float fractionPresentLocally(int fileNumber)
	{
		if (fileNumber < 0 || fileNumber > numFiles)
			return -1;
		return local[fileNumber];
	}
	
	/**
	 * @param fileNumber
	 * @return Fraction of file present in the system (chunks in the system/total number of chunks in the file)
	 */
	public float fractionPresent(int fileNumber)
	{
		if (fileNumber < 0 || fileNumber > numFiles)
			return -1;
		return system[fileNumber];
	}
	
	public int minimumReplicationLevel(int fileNumber)
	{
		if (fileNumber < 0 || fileNumber > numFiles)
			return -1;
		return leastReplication[fileNumber];
	}
	
	public float averageReplicationLevel(int fileNumber)
	{
		if (fileNumber < 0 || fileNumber > numFiles)
			return -1;
		return weightedLeastReplication[fileNumber];
	}
}
