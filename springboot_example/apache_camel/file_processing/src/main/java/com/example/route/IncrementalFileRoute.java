package com.example.route;


import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.bean.FileDataComparator;
import com.example.bean.FileArrayListAggregator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Component("incrementalFileRoute")
public class IncrementalFileRoute extends RouteBuilder {

    public static UUID currentLoadUUID = null;
    
    @Value("${file.token}")
    private String fileToken;
    
	@Override
	public void configure() throws Exception {
	    from("file:/opt/app/example/files?include=FileName__\\d*.dat&readLock=rename&doneFileName=${file:name}.DONE&move=processedFiles/${bean:fileProcess?method=renameFile(${file:name})}")
        .log("File received  --- ${file:name}")
        .setProperty("fileName",simple("${file:name}"))
        .setProperty("fileToken",simple(fileToken))
		.process(new Processor() {
			 /**
			  * Generating UUID to store in load and further renaming the file with it. 
			  */
			@Override
			public void process(Exchange arg0) throws Exception {
			    IncrementalFileRoute.currentLoadUUID = UUID.randomUUID();
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
    	.sort(body(),new FileDataComparator())
    	.log("Incremental file sorted on SNAPSHOTDATE")
		.split(body())
        .streaming()
        .setHeader("filetestheader", constant("filetestheader"))
        .aggregate(header("filetestheader"), new FileArrayListAggregator()).completionSize(5000).completionTimeout(1500)
        .bean("fileBean","upsertData")
        .log("Processed 5000 batch records..")
	    .end();
	}

}