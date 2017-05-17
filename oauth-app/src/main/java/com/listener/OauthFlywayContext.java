package com.listener;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component("oauthFlywayContext")
public class OauthFlywayContext implements Serializable, SmartLifecycle {

	private static final Logger logger = Logger
			.getLogger(OauthFlywayContext.class);

	@Resource
	private DataSource securityDataSource;

	private void flywayinit() {
		try {
			logger.info("Deploying database changes if any...");
			Flyway flyway = new Flyway();
			flyway.setDataSource(securityDataSource);
			MigrationInfoService infoService = flyway.info();
			if (null != infoService) {
				MigrationInfo[] info = infoService.all();
				if (null != info && info.length == 0) {
					flyway.clean();
					flyway.baseline();
				}
			}

			flyway.setSqlMigrationPrefix("OAUTH");
			flyway.setSqlMigrationSeparator("_");
			flyway.setBaselineOnMigrate(true);
			// Start the migration
			flyway.setLocations("db/migration");
			flyway.migrate();
		} catch (FlywayException e) {
			logger.error("Error while running flyway..server fail to start....." + e);
			System.exit(0);
		}
		logger.info("Deployed database successfully...");

	}

	@Override
	public boolean isRunning() {
		logger.debug("oathFlywayContext isRunning returned false");
		return false;
	}

	@Override
	public void start() {
		logger.debug("Starting flyaway context calling flyway-init");
		//flywayinit();
	}

	@Override
	public void stop() {
		logger.debug("stoping flyway context");
	}

	@Override
	public int getPhase() {
		logger.debug(" phase is set to 10");
		return 10;
	}

	@Override
	public boolean isAutoStartup() {
		logger.debug("isAutoStartup is true");
		return true;
	}

	@Override
	public void stop(Runnable arg0) {
		logger.debug("stoping flyway context in runnable...");
	}

}
