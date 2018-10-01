package com.example.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileDataComparator implements Comparator<Map<String, String>> {

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger LOG = LoggerFactory.getLogger(FileDataComparator.class);

	@Override
	public int compare(Map<String, String> map1, Map<String, String> map2) {
		int result = 0;
		try {
			result = simpleDateFormat.parse(map1.get("SNAPSHOTDATE")).compareTo(simpleDateFormat.parse(map2.get("SNAPSHOTDATE")));
		} catch (ParseException e) {
			LOG.error("Error in SubscriberDataComparator", e);
		}
		return result;

	}

}