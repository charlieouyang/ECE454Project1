package justen;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class FileManager {
	
	public String chunkPath;
	public String completedPath;
	
	public FileManager(String name)
	{
		chunkPath = name + "\\chunks";
		completedPath = name + "\\completed";
		File dir = new File(chunkPath);
		if (!dir.exists())
			dir.mkdir();
	}
	
	/**
	 * Checks if the peer contains the completed file
	 * @param fileName Name of the File
	 * @return If the peer has the completed file
	 */
	public boolean hasCompleteFile(String fileName) {
		HashSet<TorrentFile> completedFiles = getCompletedFiles();
		for (TorrentFile t : completedFiles) {
			if (t.getFileName().equals(fileName))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if the peer contains the given chunk
	 * @param fileName Name of file
	 * @param chunkNum Chunk number of file
	 * @return If peer has the specified chunk
	 */
	public boolean hasChunk(String fileName, int chunkNum) {
		HashSet<String> chunks = getLocalChunkList();
		for (String s : chunks) {
			if (s.equals(fileName + "_chunk_" + chunkNum))
				return true;
		}
		return false;
	}
	
	/**
	 * @return Gets chunks associated with peer
	 * We will use the aggregateChunkListMethodHere
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
	public HashSet<TorrentFile> getCompletedFiles()
	{
		File dir = new File(completedPath);
		HashSet<TorrentFile> fileList = new HashSet<TorrentFile>();
		
		if (dir.isDirectory())
		{
			for (String fileName : dir.list())
			{
				File f = new File(fileName);
				if (!f.isHidden() && !f.isDirectory())
					fileList.add(new TorrentFile(f));
			}
		}
		return fileList;
	}
	
	public TorrentFile copyFileFromRepo(String fileName) {
		File file = new File(fileName);
		Path source = Paths.get(fileName);
		Path target = Paths.get(completedPath + "\\" + file.getName());
		try {
			Files.copy(source, target, REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("Error inserting: " + fileName);
			return null;
		}
		File[] subDirs = target.toFile().listFiles();
		
		for (File f : subDirs) {
			if (f.getName().equals(fileName))
				return new TorrentFile(f);
		}
		return null;
	}
	
	/**
	 * Writes the chunk to the chunk directory. If all chunks are there, build the file.
	 * @param tFile Torrent file
	 * @param chunkNum Chunk number
	 * @param chunk Chunk data
	 */
	public void writeChunkToMemory(TorrentFile tFile, int chunkNum, byte[] chunk) {
		if (chunkNum >= tFile.getNumberOfChunks())
			return; // error
		
		File file = new File(chunkPath, tFile.getChunkName(chunkNum));
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file);
			fos.write(chunk);
		} catch (Exception e) {
			System.out.println("Error writing the chunk to memory " + tFile.getChunkName(chunkNum));
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					// wtf
				}
		}
		
		// Check if all chunks are available.
		HashSet<String> writtenChunks = this.getLocalChunkList();
		for (int i = 0; i < tFile.getNumberOfChunks(); i++) {
			if (!writtenChunks.contains(tFile.getChunkName(i))) 
				return; // missing a chunk
		}
		
		// All chunks are available, write the complete file to the completed directory
		File seedingFile = new File(completedPath, tFile.getFileName());
		byte[] data;
		fos = null;
		
		try {
			fos = new FileOutputStream(seedingFile);
			for (int i = 0; i < tFile.getNumberOfChunks(); i++) {
				data = getChunkData(tFile, i);
				fos.write(data);
			}
		} catch (Exception e) {
			System.out.println("Fucked up rebuilding the file");
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					// wtf
				}
		}
	}
	
	public byte[] getChunk(String fileName) {
		return ChunkManager.getChunk(fileName);
	}
	
	public byte[] getChunkFromIncompleteFile(String fileName, int chunkNum) {
		return ChunkManager.getChunkFromOffset(fileName, chunkNum * Constants.CHUNK_SIZE);
	}
	
	/**
	 * Gets the data of the specified chunk from chunk directory.
	 * @param tFile
	 * @param chunkNum
	 * @return Chunk data
	 */
	public byte[] getChunkData(TorrentFile tFile, int chunkNum) {
		if (chunkNum >= tFile.getNumberOfChunks())
			return null;
		
		File chunkFile = new File(chunkPath, tFile.getChunkName(chunkNum));
		FileInputStream fis = null;
		byte[] chunkData;
		int bytesRead;
		
		try {
			fis = new FileInputStream(chunkFile);
			chunkData = new byte[(int)chunkFile.length()];
			
			bytesRead = fis.read(chunkData);
			if (bytesRead < 1)
				return null; // didn't read any bytes
			
			return chunkData;
		} catch (Exception e) {
			
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					// wtf
				}
		}
		return null;
	}
}