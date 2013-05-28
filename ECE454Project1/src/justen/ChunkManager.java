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
			if (file.length() != Constants.CHUNK_SIZE)
				throw new Exception();
			
			fis = new FileInputStream(filePath);
			
			bytesRead = fis.read(value);
			
			if (bytesRead != Constants.CHUNK_SIZE || bytesRead == -1)
				throw new Exception();
		}
		catch (IOException ioe)
		{
			// Error reading data from FileInputStream
			return null;
		}
		catch (Exception e)
		{
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
		byte[] value = new byte[Constants.CHUNK_SIZE];
		int bytesRead = 0;
		try
		{
			raf = new RandomAccessFile(filePath, "r");
			
			raf.seek(offset);
			bytesRead = raf.read(value, offset, Constants.CHUNK_SIZE);
			
			if (bytesRead == -1)
				throw new IOException();
		}
		catch (IOException ioe)
		{
			// should do some logging
			return null;
		}
		catch (Exception e)
		{
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
