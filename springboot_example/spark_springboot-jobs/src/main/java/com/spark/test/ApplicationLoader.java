package com.spark.test;

import org.apache.log4j.Logger;
/*import org.apache.spark.SparkConf;
 import org.apache.spark.api.java.JavaSparkContext;
 import org.apache.spark.sql.DataFrame;
 import org.apache.spark.sql.DataFrameWriter;
 import org.apache.spark.sql.SQLContext;*/
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationLoader {
    private static final Logger logger = Logger.getLogger(ApplicationLoader.class);

    public static void main(String[] args) {
	logger.info("started loading the application");
	SpringApplication.run(ApplicationLoader.class, args);
	logger.info("ended loading the application");

    }

}
