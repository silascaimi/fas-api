package com.silascaimi.fasapi.config.property;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("fas")
public class FASProperty {

	private boolean enableHttps;

	private List<String> origensPermitidas;

	@Getter
	private final Mail mail = new Mail();

	public boolean isEnableHttps() {
		return enableHttps;
	}

	public void setEnableHttps(boolean enableHttps) {
		this.enableHttps = enableHttps;
	}

	public List<String> getOrigensPermitidas() {
		return origensPermitidas;
	}

	public void setOrigensPermitidas(List<String> origensPermitidas) {
		this.origensPermitidas = origensPermitidas;
	}

	@Getter
	@Setter
	public static class Mail {

		private String host;

		private Integer port;

		private String username;

		private String password;
	}

}
