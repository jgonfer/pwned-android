package com.jgonfer.pwned.utils;

import com.jgonfer.pwned.connection.PwnedAPIEndpointInterface;
import com.jgonfer.pwned.connection.ServiceGenerator;

/**
 * Created by jgonzalez on 17/6/16.
 */
public class UserServiceSingleton {
	static private PwnedAPIEndpointInterface userService = ServiceGenerator.createService(PwnedAPIEndpointInterface.class);

	private static UserServiceSingleton ourInstance = new UserServiceSingleton();

	public static void initInstance() {
		if (ourInstance == null) {
			ourInstance = new UserServiceSingleton();
		}
	}

	public static UserServiceSingleton getInstance() {
		return ourInstance;
	}

	private UserServiceSingleton() {}

	public static PwnedAPIEndpointInterface getUserService() {
		return userService;
	}
}
