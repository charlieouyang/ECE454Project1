package justen;
import java.io.File;

public class MiscFunctions {
	
	public boolean createDirectory(String name)
	{
		File dir = new File(name);
		return dir.mkdir();
	}
	
	public boolean createCompletedDirectory(String name)
	{
		File completedDir = new File(name + "\\completed");
		return completedDir.mkdir();
	}
	
	public boolean createChunkDirectory(String name)
	{
		File chunkDir = new File(name + "\\chunks");
		return chunkDir.mkdir();
	}
	
}
