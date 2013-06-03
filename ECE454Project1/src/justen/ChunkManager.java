package justen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class ChunkManager {
	
	/*
	 * For incomplete files
	 */
	public static byte[] getChunk(String filePath)
	{
		File file = null;
		FileInputStream fis = null;
		byte[] value = new byte[Constants.CHUNK_SIZE];
		int bytesRead;

		try
		{
			file = new File(filePath);
			
			if (!file.exists() || file.isDirectory())
				throw new Exception("File does not exist or file is a directory");
				
			if (file.length() != Constants.CHUNK_SIZE)
				throw new Exception("File length does not match CHUNK_SIZE");
			
			fis = new FileInputStream(filePath);
			
			bytesRead = fis.read(value);
			
			if (bytesRead != Constants.CHUNK_SIZE || bytesRead == -1)
				throw new Exception("Could not read chunk");
		}
		catch (IOException ioe)
		{
			// Error reading data from FileInputStream
			return null;
		}
		catch (Exception e)
		{
			// File does not exist or file is a directory
			// Chunk file size does not match specified CHUNK_SIZE
			// No data in file
			return null;
		}
		finally
		{
			try
			{
			if (fis != null)
				fis.close();
			}
			catch (Exception ex) 
			{
				// error closing FileInputStream
				// should check if value was correctly populated
			}
		}
		return value;
	}
	
	/*
	 * For completed files (seeding hosts)
	 */
	public static byte[] getChunkFromOffset(String filePath, int offset)
	{
		RandomAccessFile raf = null;
		File file = new File(filePath);
		if (!file.canRead())
			file.setReadable(true);
		byte[] value = new byte[Constants.CHUNK_SIZE];
		int bytesRead = 0;
		try
		{
			raf = new RandomAccessFile(filePath, "r");
			
			raf.seek(offset);
			bytesRead = raf.read(value, 0, Constants.CHUNK_SIZE);
			if (bytesRead != Constants.CHUNK_SIZE) {
				// underflow
				// copy to new array with appropriate size
				byte[] temp = value;
				value = new byte[bytesRead];
				for (int i = 0; i < bytesRead; i++) {
					value[i] = temp[i];
				}
			}
			if (bytesRead == -1)
				throw new Exception();
		}
		catch (IOException ioe)
		{
			// should do some logging
			return null;
		}
		catch (Exception e)
		{
			// Data not read properly.
			// should do some logging
			return null;
		}
		finally
		{
			try
			{
			if (raf != null)
				raf.close();
			}
			catch (Exception ex) 
			{
				// error closing FileInputStream
			}
		}
		return value;
	}
}
