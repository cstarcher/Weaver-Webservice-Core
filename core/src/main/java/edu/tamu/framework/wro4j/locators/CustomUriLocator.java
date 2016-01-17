package edu.tamu.framework.wro4j.locators;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import edu.tamu.framework.service.ThemeManagerService;
import ro.isdc.wro.model.resource.locator.UriLocator;

public class CustomUriLocator implements UriLocator {
	private ThemeManagerService themeManagerService;
	
	public CustomUriLocator(ThemeManagerService themeManagerService) {
		this.themeManagerService = themeManagerService;
	}
	
	@Override
	public boolean accept(String serviceName) {
		// TODO Auto-generated method stub
		return "themeManagerService".equals(serviceName);
	}

	@Override
	public InputStream locate(String serviceName) throws IOException {
		String resourceText = "/* The custom locator fetched the following SASS vars from the ThemeManagerService: */\n\n"; 
		resourceText += themeManagerService.getFormattedProperties();
		InputStream stream = new ByteArrayInputStream(resourceText.getBytes(StandardCharsets.UTF_8));

		return stream;
	}

}
