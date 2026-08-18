package com.epiis.app.auxobject;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
	public static String secretKey;
	public static int timeAuthMs;

	public void setSecretKey(String secretKey) {
		JwtProperties.secretKey = secretKey;
	}

	public void setTimeAuthMs(int timeAuthMs) {
		JwtProperties.timeAuthMs = timeAuthMs;
	}
	
}
