package com.example.bean;

import java.util.ArrayList;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.example.route.FileProcesserRoute;

public class FileArrayListAggregator implements AggregationStrategy {

	@SuppressWarnings("unchecked")
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Map<String, String> newBody = (Map<String, String>) newExchange.getIn().getBody();
		newBody.put("batchId", String.valueOf(FileProcesserRoute.currentLoadUUID));
		ArrayList<Map<String, String>> list = null;
		if (oldExchange == null) {
			list = new ArrayList<Map<String, String>>();
			list.add(newBody);
			newExchange.getIn().setBody(list);
			return newExchange;
		} else {
			list = oldExchange.getIn().getBody(ArrayList.class);
			list.add(newBody);
			return oldExchange;
		}
	}
}