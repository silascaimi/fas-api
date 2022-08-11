package com.silascaimi.fasapi.config.property;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("fas")
public class FASProperty {

	private boolean enableHttps;

	private List<String> origensPermitidas;

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

}
