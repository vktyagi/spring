package com.example.route;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.bean.SubscriberDataComparator;
import com.example.bean.SubscriberFileArrayListAggregator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Component("subscriberFileRoute")
public class SubscriberFileRoute extends RouteBuilder {

	public static UUID currentLoadUUID = null;

    @Value("${subscriber.incremental.file.token}")
    private String subscriberIncrementalFileToken;
    
	@SuppressWarnings("deprecation")
	@Override
	public void configure() throws Exception {
	    from("file:/opt/app/example/files?readLock=rename&doneFileName=${file:name}.DONE&move=processedFiles/${bean:fileProcess?method=renameFile(${file:name})}")
        .log("File received  --- ${file:name}")
        .setProperty("fileName",simple("${file:name}"))
        .setProperty("subscriberIncrementalFileToken",simple(subscriberIncrementalFileToken))
		.process(new Processor() {
			 /**
			  * Generating UUID to store in load and further renaming the file with it. 
			  */
			@Override
			public void process(Exchange arg0) throws Exception {
				currentLoadUUID = UUID.randomUUID();
			}
		})
		.threads(20)
		.process(new Processor() {
			CsvMapper csvMapper = new CsvMapper();
			CsvSchema schema = csvMapper.schema().withColumnSeparator('|').withHeader();
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getOut().setBody(csvMapper.readerFor(Map.class).with(schema).readValues(exchange.getIn().getBody(InputStream.class)));
			}
		}) 
		.choice()
    		.when(property("fileName").contains(subscriberIncrementalFileToken))
    			.log("Found incremental file. Starting sort on SNAPSHOTDATE")
    			.sort(body(),new SubscriberDataComparator())
		.end()
		.split(body())
        .streaming()
        .setHeader("subscriber", constant("subscriber"))
        .aggregate(header("subscriber"), new SubscriberFileArrayListAggregator()).completionSize(10000).completionTimeout(1500)
        .parallelProcessing()
        .bean("subscriberBean","upsertData")
        // .process(new SubscriberBeanProcessor())
	 .log("Processed batch records..")
	    .end();
	}

}