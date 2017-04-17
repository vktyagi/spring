package com.spark.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.SQLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ImportResource("classpath:applicationContext.xml")
public class BaseJob {
    private static final Logger LOGGER = Logger.getLogger(BaseJob.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private AppConfiguration appConfiguration;

    @Scheduled(cron = "${spark.job.cron}")
    public void runEveryDay() {
	LOGGER.info("Cron expression - Run every day at  - " + dateFormat.format(new Date()));
	LOGGER.info("Job started");
	String URL = appConfiguration.getDburl()+ "?user=" + appConfiguration.getDbuser() + "&password=" + appConfiguration.getDbpassword();
	JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("DBConnection").setMaster("local[*]"));
	SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, -1);
	String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	DataFrame tagDf = sqlContext.read().jdbc(URL, "s3_log_line_segments", new Properties()).filter("created_at='" + modifiedDate + "'");
	tagDf.registerTempTable("s3_log_line_segments_temp");

	DataFrame targetDf = sqlContext.sql("select count(segment_id) as fire_count, segment_id, first(created_at) as fire_date from  s3_log_line_segments_temp group by segment_id");
	targetDf.show();
	DataFrameWriter dfr = new DataFrameWriter(targetDf);
	dfr.mode("append");
	dfr.jdbc(URL, "segment_fires_test", new Properties());
	LOGGER.info("Job ended");
    }
}