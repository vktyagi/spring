package com.store.persistence.lifecycle;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import com.store.spring.config.ConfigurationSpring;

@SuppressWarnings("serial")
@Component("flywayContext")
public class FlywayContext implements Serializable, SmartLifecycle {

    @Resource
    private DataSource dataSource;

    @Autowired
    public FlywayContext(DataSource c3p0DataSource) {

	System.out.println("Inside FlywayContext deploying db-scripts...");
	ConfigurationSpring cs = new ConfigurationSpring();
	this.dataSource = cs.getDataSource();

    }

    private void flywayinit() {
	System.out.println("Deploying database changes if any...");
	Flyway flyway = new Flyway();
	flyway.setDataSource(dataSource);

	MigrationInfoService infoService = flyway.info();

	if (null != infoService) {
	    MigrationInfo[] info = infoService.all();
	    if (null != info && info.length == 0) {
		flyway.baseline();
	    }
	}
	// STORE1.1_Sprint1.sql
	flyway.setSqlMigrationPrefix("STORE");
	flyway.setSqlMigrationSeparator("_");
	flyway.setBaselineOnMigrate(true);
	// Start the migration
	flyway.setLocations("db/migration");
	flyway.migrate();
	System.out.println("Deployed database successfully...");

    }

    @Override
    public boolean isRunning() {
	System.out.println("flywayContext isRunning returned false");
	return false;
    }

    @Override
    public void start() {
	System.out.println("Starting flyaway context calling flyway-init");
	flywayinit();
    }

    @Override
    public void stop() {
	System.out.println("stoping flyway context");
    }

    @Override
    public int getPhase() {
	System.out.println(" phase is set to 10");
	return 10;
    }

    @Override
    public boolean isAutoStartup() {
	System.out.println("isAutoStartup is true");
	return true;
    }

    @Override
    public void stop(Runnable arg0) {
	System.out.println("stoping flyway context in runnable...");
    }

}
