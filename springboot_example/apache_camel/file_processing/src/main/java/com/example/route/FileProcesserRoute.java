package com.example.route;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.example.bean.FileArrayListAggregator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Component("fileProcesserRoute")

public class FileProcesserRoute extends RouteBuilder {
	
    public static UUID currentLoadUUID = null;
	
	@Override
	public void configure() throws Exception {

		from("file:/opt/app/example/files?include=\\d*_list.dat&readLock=rename&doneFileName=${file:name.noext}.done&move=processedFiles/${bean:fileProcess?method=renameFile(${file:name})}")
		.routeId("listFileProcesserRoute")
		.log("List_Detail File Processing started..!"+ "${file:name}")
	        .setProperty("fileName",simple("${file:name}"))
	         .setProperty("batchSize",simple("20000"))
			.process(new Processor() {
				 /**
				  * Generating UUID to store in load and further renaming the file with it. 
				  */
				@Override
				public void process(Exchange arg0) throws Exception {
				    FileProcesserRoute.currentLoadUUID = UUID.randomUUID();
				}
			})
			.process(new Processor() {
				CsvMapper csvMapper = new CsvMapper();
				CsvSchema schema = csvMapper.schema().withColumnSeparator('|').withHeader();
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getOut().setBody(csvMapper.readerFor(Map.class).with(schema).readValues(exchange.getIn().getBody(InputStream.class)));
				}
			}) 
			
			.split(body())
	        .streaming()
	        .setHeader("listdetailFileProcess", constant("listdetailFileProcess"))
	        .aggregate(header("listdetailFileProcess"), new FileArrayListAggregator()).completionSize(20000).completionTimeout(1500)
	        .bean("listDetailFileBean","upsertData")
	        .log("Processed 20000 batch records..")
	         .choice()
                     .when(simple("${exchangeProperty.processcomplete} == 'done'"))
                      .log("List_Detail file batching process complete.")
                    //  .bean("emailNotificationProcessor","processListDetailFileProcessNotification")
		       .log("EmailNotification : emailListDetailsFileProcess completed!")
                   .endChoice()
                   .end()
	       
		 .end();
		 
	      	}

}
