package edu.tamu.framework.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.framework.model.CoreTheme;
import edu.tamu.framework.model.ThemeProperty;
import edu.tamu.framework.model.ThemePropertyName;
import edu.tamu.framework.model.repo.CoreThemeRepo;
import edu.tamu.framework.model.repo.ThemePropertyNameRepo;
import edu.tamu.framework.model.repo.ThemePropertyRepo;
import edu.tamu.framework.util.HttpUtility;

@Component
public class ThemeManagerService {
	@Autowired
	private CoreThemeRepo coreThemeRepo;
	
	@Autowired
	private ThemePropertyNameRepo themePropertyNameRepo;

	@Autowired
	private ThemePropertyRepo themePropertyRepo;
	
	@Autowired
	private HttpUtility httpUtility;
	
	@Autowired
	private ObjectMapper objectMapper;

	private CoreTheme currentTheme;

	@Value("${theme.default.css}")
	private String[] defaultCssGroup;

	@Value("${theme.defaults.location:''}")
	private String themeDefaultsFile;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ThemeManagerService() {}
	
	@PostConstruct
	public void goNow() {
		logger.debug("Prepping Defaults :"+coreThemeRepo.count()+"");
		if (coreThemeRepo.count() == 0 && !themeDefaultsFile.equals("")) {
			ClassPathResource themeDefaultsRaw = new ClassPathResource(themeDefaultsFile); 
			JsonNode themeDefaults = null;
			try {
				themeDefaults = objectMapper.readTree(new FileInputStream(themeDefaultsRaw.getFile()));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iterator<JsonNode> itProps = themeDefaults.get("propertyNames").elements();
			while (itProps.hasNext()) {
				JsonNode entry = itProps.next();
		    	logger.debug("Creating Theme Property: "+entry.textValue()+"");
		    	themePropertyNameRepo.create(entry.textValue());
			}
			
			Iterator<Entry<String,JsonNode>> it = themeDefaults.get("themes").fields();
			Long activateId = 0L;
			while (it.hasNext()) {
			    Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) it.next();
			    if (entry.getValue().isArray()) {
		        	logger.debug("New Props for: "+entry.getKey());
		        	if (coreThemeRepo.getByName(entry.getKey()) == null) {
		    			CoreTheme newTheme = coreThemeRepo.create(entry.getKey());
		    			if (activateId == 0) {
		    				activateId = newTheme.getId();
		    			}
		        		JsonNode defaultProperties = entry.getValue();
		        		for (ThemePropertyName propertyName : themePropertyNameRepo.findAll()) {
		        			String value = defaultProperties.findValue(propertyName.getName()).asText();
		        			if (!value.isEmpty()) {
			            		coreThemeRepo.updateThemeProperty(newTheme.getId(), themePropertyRepo.getThemePropertyByThemePropertyNameAndThemeId(propertyName,newTheme.getId()).getId(),value);
		        			}
		        		}
			    	}
			    }
			}
			CoreTheme defaultTheme = coreThemeRepo.findOne(activateId);
			this.setCurrentTheme(defaultTheme);
		} else {
			this.setCurrentTheme(coreThemeRepo.findByActiveTrue());
		}
	}
	
	public CoreTheme getCurrentTheme() {
		return currentTheme;
	}
	
	public void updateThemeProperty(Long themeId,Long propertyId,String value) {
		coreThemeRepo.updateThemeProperty(themeId,propertyId,value);
		//if the updated property is part of the active theme, get it fresh from the repo
		if (this.getCurrentTheme().getId() == themeId) {
			this.refreshCurrentTheme();
		}
	}
	
	/*
	 * Gets a fresh version of the active theme from the repo
	 */
	public void refreshCurrentTheme() {
		logger.debug("The properties were:");
		currentTheme.getProperties().forEach(tp -> {
			logger.debug(tp.getPropertyName().getName()+": "+tp.getValue());
		});
		currentTheme = coreThemeRepo.getById(currentTheme.getId());
		
		logger.debug("The properties are now:");
		currentTheme.getProperties().forEach(tp -> {
			logger.debug(tp.getPropertyName().getName()+": "+tp.getValue());
		});
		this.reloadCache();
	}
	
	//tell WRO to reset its resource cache
	private void reloadCache() {
		String urlString = "http://localhost:9000/wro/wroAPI/reloadCache";
		try {
			httpUtility.makeHttpRequest(urlString, "GET");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getFormattedProperties() {
		StringBuilder formattedProperties = new StringBuilder();
		StringBuilder formattedComments = new StringBuilder();
		formattedComments.append("/* The ThemeManagerService added the following SASS vars:\n\n");
		if (this.getCurrentTheme() != null) {
			for (ThemeProperty p : this.getCurrentTheme().getProperties()) {
				formattedProperties.append("$"+p.getPropertyName().getName()+": "+p.getValue()+";\n");
				formattedComments.append("* $"+p.getPropertyName().getName()+": "+p.getValue()+";\n");
			}
			formattedComments.append("*/\n\n");
			return formattedComments+formattedProperties.toString();
		}
		return formattedComments.toString()+" n/a\n*/\n";
	}

	public void setCurrentTheme(CoreTheme theme) {
		Boolean hadTheme = (this.currentTheme != null) ? true:false;
		this.currentTheme = theme;
		coreThemeRepo.updateActiveTheme(theme);
		if (hadTheme) {
			this.reloadCache();
		}
	}
	
	public String[] getCssResources() {
		return this.defaultCssGroup;
	}
}