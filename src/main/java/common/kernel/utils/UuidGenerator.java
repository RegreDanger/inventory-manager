package common.kernel.utils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UuidGenerator {
	private UuidGenerator() {}
	
	public static String generateUUID(String... fields) {
		StringBuilder sb = new StringBuilder();
		for(String field : fields) {
			sb.append(field);
		}
		String combinedString = sb.toString();
		byte[] bytesToUUID = combinedString.getBytes(StandardCharsets.UTF_8);
		return UUID.nameUUIDFromBytes(bytesToUUID).toString();
	}
	
}
