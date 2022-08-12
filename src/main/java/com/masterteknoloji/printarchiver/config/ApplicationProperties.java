package com.masterteknoloji.printarchiver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Printarchiver.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	String archiveRepo;

	String exportDirectory;
	
	public String getArchiveRepo() {
		return archiveRepo;
	}

	public void setArchiveRepo(String archiveRepo) {
		this.archiveRepo = archiveRepo;
	}

	public String getExportDirectory() {
		return exportDirectory;
	}

	public void setExportDirectory(String exportDirectory) {
		this.exportDirectory = exportDirectory;
	}
	
	
}
