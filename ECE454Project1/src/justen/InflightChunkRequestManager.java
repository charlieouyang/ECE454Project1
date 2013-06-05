package justen;

import java.util.ArrayList;

public class InflightChunkRequestManager {
	private boolean[] inflightManager;
	private boolean[] receivedChunks;
	private boolean haveAllChunks = false;
	private int numChunks;
	private int count;
	
	public InflightChunkRequestManager(int numChunks) {
		inflightManager = new boolean[numChunks];
		receivedChunks = new boolean[numChunks];
		this.numChunks = numChunks;
	}
	
	public boolean isChunkInflight(int chunkNumber) {
		return inflightManager[chunkNumber];
	}
	
	public boolean chunkNeeded(int chunkNumber) {
		return !receivedChunks[chunkNumber];
	}
	
	public void noteChunkRequest(int chunkNumber) throws Exception {
		if (inflightManager[chunkNumber]) { // chunk already inflight... wtf?
			throw new Exception();
		} else {
			inflightManager[chunkNumber] = true;
		}
	}
	
	public void updateReceivedChunks(ArrayList<Integer> chunks) {
		for (int i = 0; i < chunks.size(); i++) {
			Integer temp = chunks.get(i);
			if (!receivedChunks[temp]) {
				receivedChunks[temp] = true;
				count++;
			}
			if (inflightManager[temp])
				inflightManager[temp] = false;
		}
	}
	
	public boolean isFileComplete() {
		return count == numChunks;
	}
}
