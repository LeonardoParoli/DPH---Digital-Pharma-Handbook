package com.dph.informationModel;

import java.util.Objects;

public class Drug {

	private String code;
	private String name;
	private String description;

	public Drug(String code, String name, String description) {
		if (code.isBlank() || code.isEmpty()) {
			throw new IllegalArgumentException("Cannot create new Drug entry with blank or empty Code");
		}
		if (name.isBlank() || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create new Drug entry with blank or empty Name");
		}
		if (description.isBlank() || description.isEmpty()) {
			throw new IllegalArgumentException("Cannot create new Drug entry with blank or empty Description");
		}
		this.code = code;
		this.name = name;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean setDescription(String description) {
		if (description.isBlank() || description.isEmpty()) {
			throw new IllegalArgumentException("Cannot modify Drug description to blank or empty");
		}
		this.description = description;
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, description, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Drug)) {
			return false;
		}
		Drug other = (Drug) obj;
		return Objects.equals(code, other.code) && Objects.equals(description, other.description)
				&& Objects.equals(name, other.name);
	}

}
