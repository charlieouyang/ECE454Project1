package justen;

import java.io.File;
import java.util.HashSet;

public class FileManager {
	
	String chunkPath;
	String completedPath;
	
	public FileManager(String name)
	{
		chunkPath = name + "\\chunks";
		completedPath = name + "\\completed";
		File dir = new File(chunkPath);
		if (!dir.exists())
			dir.mkdir();
	}
	
	/**
	 * @return Gets chunks associated with peer
	 */
	public HashSet<String> getLocalChunkList()
	{
		File dir = new File(chunkPath);
		HashSet<String> chunkList = new HashSet<String>();
		
		if (dir.isDirectory())
		{
			for (String chunkName : dir.list())
			{
				File f = new File(chunkName);
				if (!f.isHidden() && !f.isDirectory())
					chunkList.add(chunkName);
			}
		}
		return chunkList;
	}
	
	/**
	 * @return List of completed files on peer
	 */
	public HashSet<String> getCompletedFiles()
	{
		File dir = new File(completedPath);
		HashSet<String> fileList = new HashSet<String>();
		
		if (dir.isDirectory())
		{
			for (String fileName : dir.list())
			{
				File f = new File(fileName);
				if (!f.isHidden() && !f.isDirectory())
					fileList.add(fileName);
			}
		}
		return fileList;
	}
}
