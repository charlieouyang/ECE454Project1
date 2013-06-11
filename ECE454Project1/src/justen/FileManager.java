package justen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Hashtable;

import Data.PropertiesOfPeer;

public class FileManager {

	public String chunkPath;
	public String completedPath;

	public FileManager(String name) {
		chunkPath = name + "\\chunks";
		completedPath = name + "\\completed";
		File dir = new File(chunkPath);
		if (!dir.exists())
			dir.mkdir();
	}

	/**
	 * Checks if the peer contains the completed file
	 * 
	 * @param fileName
	 *            Name of the File
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
	 * 
	 * @param fileName
	 *            Name of file
	 * @param chunkNum
	 *            Chunk number of file
	 * @return If peer has the specified chunk
	 */
	public boolean hasChunk(String fileName, int chunkNum) {
		HashSet<String> chunks = 
				HelperClass.GetChunkListFromAggregate(fileName, getAllLocalChunks().get(fileName));
		for (String s : chunks) {
			if (s.equals(fileName + "_chunk_" + chunkNum))
				return true;
		}
		return false;
	}
	
	public Hashtable<String, String> getAllLocalChunks() {
		File dir = new File(chunkPath);
//		System.out.println("Getting all local chunks");
		File[] subDirs = dir.listFiles();
		if (subDirs == null) 
			return null; // no chunks
		
		Hashtable<String, String> temp = new Hashtable<String, String>();
		
		for (File sd : subDirs) {
			if (sd.isDirectory()) {
				temp.put(sd.getName(), DirectoryHelper.getAggregateChunkList(sd.getAbsolutePath()));
			} else {
				// error, we should not be here
			}
		}
		
		return temp;
	}
	
	/**
	 * @return Gets chunks associated with peer We will use the
	 *         aggregateChunkListMethodHere
	 */
	public HashSet<String> getLocalChunkList(String fileName) {
		File dir = new File(chunkPath, fileName);
		HashSet<String> chunkList = new HashSet<String>();

		if (dir.isDirectory()) {
			for (String chunkName : dir.list()) {
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
	public HashSet<TorrentFile> getCompletedFiles() {
		File dir = new File(completedPath);
		HashSet<TorrentFile> fileList = new HashSet<TorrentFile>();

		if (dir.isDirectory()) {
			for (String fileName : dir.list()) {
				File f = new File(fileName);
				if (!f.isHidden() && !f.isDirectory())
					fileList.add(new TorrentFile(f));
			}
		}
		return fileList;
	}

	public TorrentFile copyFileFromRepo(String fileName) throws IOException {
		File file = new File(fileName);
		FileChannel in = new FileInputStream(fileName).getChannel();
		FileChannel out = new FileOutputStream(completedPath + "\\" + file.getName()).getChannel();
		in.transferTo(0, in.size(), out);
		out.close();
		in.close();
		return new TorrentFile(file);
	}

	/**
	 * Writes the chunk to the chunk directory. If all chunks are there, build
	 * the file.
	 * 
	 * @param tFile
	 *            Torrent file
	 * @param chunkNum
	 *            Chunk number
	 * @param chunk
	 *            Chunk data
	 */
	public void writeChunkToMemory(TorrentMetaData tFile, int chunkNum, byte[] chunk) throws Exception {
		if (chunkNum >= tFile.getNumberOfChunks())
			return; // error

		File file = new File(chunkPath + "\\" + tFile.getFileName(), tFile.getChunkName(chunkNum));
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			fos.write(chunk);
		} catch (Exception e) {
			System.out.println("Error writing the chunk to memory "
					+ tFile.getChunkName(chunkNum));
			throw new Exception("Error writing chunk to memory "
					+ tFile.getChunkName(chunkNum));
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					// wtf
				}
		}

		// Check if all chunks are available.
		HashSet<String> writtenChunks = this.getLocalChunkList(tFile.getFileName());
		for (int i = 0; i < tFile.getNumberOfChunks(); i++) {
			if (!writtenChunks.contains(tFile.getChunkName(i)))
				return; // missing a chunk
		}

		// All chunks are available, write the complete file to the completed
		// directory
		File seedingFile = new File(completedPath, tFile.getFileName());
		seedingFile.setWritable(true);
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
			if (fos != null) {
				try {
					fos.close();
					
				} catch (IOException e) {
					// wtf
				}
			}
		}
		PropertiesOfPeer.updateCurrentPeerStatus();
		PropertiesOfPeer.broadcastStatus();
	}

	// from completed directory
	public byte[] getChunk(String fileName, int chunkNum) {
		return ChunkManager.getChunkFromOffset(completedPath + "\\" + fileName, chunkNum
				* Constants.CHUNK_SIZE);
	}

	public byte[] getChunkFromIncompleteFile(String fileName, int chunkNum) {
		return ChunkManager.getChunk(chunkPath + "\\" + fileName + "\\" + fileName + "_chunk_" + chunkNum);
	}

	/**
	 * Gets the data of the specified chunk from chunk directory.
	 * 
	 * @param tFile
	 * @param chunkNum
	 * @return Chunk data
	 */
	public byte[] getChunkData(TorrentMetaData tFile, int chunkNum) {
		if (chunkNum >= tFile.getNumberOfChunks())
			return null;

		File chunkFile = new File(chunkPath + "\\" +tFile.getFileName(), tFile.getChunkName(chunkNum));
		FileInputStream fis = null;
		byte[] chunkData;
		int bytesRead;

		try {
			fis = new FileInputStream(chunkFile);
			chunkData = new byte[(int) chunkFile.length()];

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
	
	public String getAggregateChunks(String fileName) {
		return DirectoryHelper.getAggregateChunkList(chunkPath + "\\" + fileName);
	}
}