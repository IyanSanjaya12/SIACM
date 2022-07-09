package id.co.promise.procurement.entity;

public class PRStatus {
	private Integer value;
	private String key;
	private String name;

	public PRStatus(Integer value, String key, String name) {
		this.value = value;
		this.key = key;
		this.name = name;

	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
