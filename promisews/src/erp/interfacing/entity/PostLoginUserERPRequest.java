package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PostLoginUserERPRequest {
	
	@SerializedName("username")
	@JsonProperty("username")
	private String username;
	
	@SerializedName("password")
	@JsonProperty("password")
	private String  password;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
