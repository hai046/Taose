package com.buy.holder;

public class ExtraItem {

	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ExtraItem [id=" + id + ", key=" + key + ", value=" + value
				+ ", name=" + name + "]";
	}

	private String key,value,name;

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
