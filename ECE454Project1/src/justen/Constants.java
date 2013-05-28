package justen;

public final class Constants {
	// Global meta data
	public final static int CHUNK_SIZE = 65536; // 64 kB
	
	// Error codes
	public final static int ERR_OK = 0;
	public final static int ERR_UNKNOWN_WARNING = -1;
	public final static int ERR_UNKNOWN_FATAL = -2;
	public final static int ERR_CANNOT_CONNECT = -3;
	public final static int ERR_NO_PEERS_FOUND = -4;
	public final static int ERR_PEER_NOT_FOUND = -5;
}
