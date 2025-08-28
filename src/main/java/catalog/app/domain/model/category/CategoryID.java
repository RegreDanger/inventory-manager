package catalog.app.domain.model.category;

import java.util.Objects;

import common.kernel.utils.UuidGenerator;

public class CategoryID {
	private final String value;
	
	private CategoryID(String value) {
		this.value = value;
	}
	
	public static CategoryID from(String id) {
		return new CategoryID(id);
	}
	
	public static CategoryID generate(String... fields) {
		return new CategoryID(UuidGenerator.generateUUID(fields));
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		CategoryID other = (CategoryID) obj;
		return Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	
}
