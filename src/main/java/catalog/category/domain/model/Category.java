package catalog.category.domain.model;

import java.util.Objects;

public class Category {
	private CategoryID id;
	private String name;
	private String description;
	
	public Category(CategoryID id, String name, String description) {
        if (id == null || id.getValue().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if(description == null || description.isEmpty()) {
        	throw new IllegalArgumentException("Sescription cannot be null or empty");
        }
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public CategoryID getId() {
		return id;
	}
	
	public void setId(CategoryID id) {
		if (id == null || id.getValue().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
        if(description == null || description.isEmpty()) {
        	throw new IllegalArgumentException("Sescription cannot be null or empty");
        }
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Category other = (Category) obj;
		return Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(description, id, name);
	}
	
}
