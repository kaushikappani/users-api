package com.sapient.userapi.model;

import java.util.List;
import java.util.Objects;

public class UserResponse {

	private List<ExternalUser> users;

	public List<ExternalUser> getUsers() {
		return users;
	}

	public void setUsers(List<ExternalUser> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		return Objects.hash(users);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserResponse other = (UserResponse) obj;
		return Objects.equals(users, other.users);
	}
	
	
	 
}
