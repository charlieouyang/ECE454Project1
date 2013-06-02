package justen;

import java.util.HashSet;

public class HelperClass {
	
	public static HashSet<String> GetChunkListFromAggregate(String fileName, String aggregate) {
		String[] tokens = aggregate.split(",");
		HashSet<String> list = new HashSet<String>();
		
		for (String t : tokens) {
			if (t.contains("-")) { // 13-45
				String[] indices = t.split("-");
				for (int i = Integer.parseInt(indices[0]); i < Integer.parseInt(indices[1]); i++) 
					list.add(fileName + "_chunk_" + i);
			} else //5
				list.add(fileName + "_chunk_" + t);
		}
		return list;
	}
}
