package com.example.bean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("listDetailFileBean")
public class ListDetailFileBean {



   /* @Autowired
    private SqlSessionTemplate templateBatch;*/
    List<String> headersList = Arrays.asList("ACTION", "LIST_NAME", "LIST_TYPE", "VALUE", "RETURN_CODE", "VALIDATION_FLAG", "PRECEDENCE", "USERID");
    
    private String ERROR_FILE_DIRECTORY = "/opt/app/example/files/error";

    @Transactional
    public void upsertData(Exchange exchange, List<Map<String, String>> listDetailMap) throws IOException {
	String fileName = exchange.getProperty("fileName").toString();
	Map<String, Set<String>> failureMap = new HashMap<String, Set<String>>();
	validate(listDetailMap, failureMap);
	  int batchSize =  exchange.getProperty("batchSize", Integer.class);
	  int camelAggregatedSize =  exchange.getProperty("CamelAggregatedSize", Integer.class);
	if (null != failureMap && !failureMap.isEmpty()) {
	    Set<String> filesList = new HashSet<String>();
	    filesList.add(fileName);
	    failureMap.put("FILE_NAME", filesList);
	    Set<String> batches = new HashSet<String>();
	    int camelSprintIndex = exchange.getProperty("CamelSplitIndex", Integer.class);

	    int failBatchesNo = (camelSprintIndex / batchSize) + 1;
	    batches.add(String.valueOf(failBatchesNo) + "(" + (camelSprintIndex + 1) + "-" + (camelSprintIndex + camelAggregatedSize) + " )");
	    failureMap.put("FAIL_BATCH_DETAILS", batches);
	    insertFailMessageErrorFIle(exchange, failureMap);

	}else{

	    for (Map<String, String> dataMap : listDetailMap) {
		switch (dataMap.get("ACTION")) {
		case "INSERT":
		    String validationFlag = dataMap.get("VALIDATION_FLAG");
		    if ("WHITE".equalsIgnoreCase(validationFlag)) {
			dataMap.put("VALIDATION_FLAG", "1");
		    } else if ("BLACK".equalsIgnoreCase(validationFlag)) {
			dataMap.put("VALIDATION_FLAG", "0");
		    } else if ("GREEN".equalsIgnoreCase(validationFlag)) {
			dataMap.put("VALIDATION_FLAG", "2");
		    }
		  // templateBatch.insert("manager.insertFileProcessing", dataMap);
		    break;

		case "DELETE":
		    //templateBatch.delete("manager.deleteFileProcessing", dataMap);
		    break;

		default:
		    throw new IllegalArgumentException("Invalid Action: " + dataMap.get("ACTION"));
		}
	    }
	}
	if(camelAggregatedSize!= batchSize){
	    exchange.setProperty("processcomplete", "done");
	}
    }

    private void insertFailMessageErrorFIle(Exchange exchange, Map<String, Set<String>> failureMap) throws IOException {

	    String fileName = exchange.getProperty("fileName").toString();
	    ObjectMapper mapperObj = new ObjectMapper();
	    Path dir = Paths.get(ERROR_FILE_DIRECTORY);
	    if(!Files.exists(dir)){
	    	Files.createDirectories(dir);
	    }
	    File file = new File(ERROR_FILE_DIRECTORY+"/"+fileName+"_error");
	    if (!file.exists()) {
		file.createNewFile();
		String jsonResp = mapperObj.writeValueAsString(failureMap);
		FileWriter fw = new FileWriter(file, false);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(jsonResp);
		bw.close();
	    } else {
		Map<String, Set<String>> map = mapperObj.readValue(file, Map.class);
		for (String key : failureMap.keySet()) {
		    if (map.containsKey(key)) {
			List<String> maplist = (List<String>) map.get(key);
			Set<String> failSets =failureMap.get(key);
			for (String str : failSets) {
			    if(!maplist.contains(str)){
				 maplist.add(str);
			    }
			}
		    } else {
			map.put(key, failureMap.get(key));
		    }
		}
		String jsonString= mapperObj.writeValueAsString(map);
		FileWriter fw = new FileWriter(file, false);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(jsonString);
		bw.flush();
		bw.close();
	    
	    }

    }

    private void validate(List<Map<String, String>> listDetailMap, Map<String, Set<String>> failureMap) {

		for (String headerName : headersList) {
		    for (Map<String, String> listMap : listDetailMap) {
			if (!listMap.containsKey(headerName)) {
			   Set<String> headerList = failureMap.get("header");
			    if(null != headerList){
				headerList.add("Invalid Header:" + headerName);
				
			    }else{
				headerList = new HashSet<String>();
				headerList.add("Invalid Header:" + headerName);
			    }
			    failureMap.put("header", headerList);
			}
		    }
		}
		for (Map<String, String> listMap : listDetailMap) {

		    if (((listMap.containsKey("ACTION")) && !("INSERT".equalsIgnoreCase(listMap.get("ACTION")) || "DELETE".equalsIgnoreCase(listMap.get("ACTION"))))) {
			Set<String> actionList = failureMap.get("ACTION");
			    if(null != actionList){
				actionList.add("Invalid value :" + listMap.get("ACTION"));
				
			    }else{
				actionList = new HashSet<String>();
				actionList.add("Invalid value :" + listMap.get("ACTION"));
			    }
			    failureMap.put("ACTION", actionList);
		    }
		    if (listMap.containsKey("LIST_NAME")) {
			String keyValue = listMap.get("LIST_NAME");
			if (keyValue.isEmpty() && keyValue.length() <= 10) {
			    Set<String> listName = failureMap.get("LIST_NAME");
			    if(null != listName){
				listName.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
				
			    }else{
				listName = new HashSet<String>();
				listName.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
			    }
			    failureMap.put("LIST_NAME", listName);
			}
		    }
		    if (listMap.containsKey("LIST_TYPE")) {
			String keyValue = listMap.get("LIST_TYPE");
			if (keyValue.isEmpty() || !("TN".equalsIgnoreCase(keyValue) || "DEALER".equalsIgnoreCase(keyValue)) || !(keyValue.length() <= 10)) {
			  
				 Set<String> listType = failureMap.get("LIST_TYPE");
				    if(null != listType){
					listType.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
					
				    }else{
					listType = new HashSet<String>();
					listType.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
				    }
				    failureMap.put("LIST_TYPE", listType);
			    }
		    }
		    if (("INSERT".equalsIgnoreCase(listMap.get("ACTION"))) && listMap.containsKey("VALUE")) {
			String keyValue = listMap.get("VALUE");
			if (keyValue.isEmpty() || ("TN".equalsIgnoreCase(listMap.get("LIST_TYPE")) && !((keyValue.trim().length() > 0 && keyValue.trim().length() <= 10) || !StringUtils.isNumeric(keyValue)))
				|| ("DEALER".equalsIgnoreCase(listMap.get("LIST_TYPE")) && !(keyValue.trim().length() > 0 && keyValue.trim().length() <= 25))) {
			    Set<String> values = failureMap.get("VALUE");
			    if(null != values){
				values.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
				
			    }else{
				values = new HashSet<String>();
				values.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
			    }
			    failureMap.put("VALUE", values);
			}
		    }
		    if (("INSERT".equalsIgnoreCase(listMap.get("ACTION"))) && listMap.containsKey("RETURN_CODE")) {
			String keyValue = listMap.get("RETURN_CODE");
			if (keyValue.isEmpty() || !(keyValue.length() <= 30 && keyValue.length() >= 0)) {
			    Set<String> returnCode = failureMap.get("RETURN_CODE");
			    if(null != returnCode){
				returnCode.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
				
			    }else{
				returnCode = new HashSet<String>();
				returnCode.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
			    }
			    failureMap.put("RETURN_CODE", returnCode);
			}
		    }
		    if (("INSERT".equalsIgnoreCase(listMap.get("ACTION"))) && listMap.containsKey("VALIDATION_FLAG")) {
			String keyValue = listMap.get("VALIDATION_FLAG");
			if (keyValue.isEmpty() || !("WHITE".equalsIgnoreCase(keyValue) || "GREEN".equalsIgnoreCase(keyValue) || "BLACK".equalsIgnoreCase(keyValue))) {
			    Set<String> validationFlag = failureMap.get("VALIDATION_FLAG");
			    if(null != validationFlag){
				validationFlag.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
				
			    }else{
				validationFlag = new HashSet<String>();
				validationFlag.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
			    }
			    failureMap.put("VALIDATION_FLAG", validationFlag);
			}
		    }
		    if (("INSERT".equalsIgnoreCase(listMap.get("ACTION"))) &&  (listMap.containsKey("PRECEDENCE"))) {
			String keyValue = listMap.get("PRECEDENCE");
			if (keyValue.isEmpty() || !(keyValue.length() <= 4)) {
			    Set<String> precendenceList = failureMap.get("PRECEDENCE");
			    if(null != precendenceList){
				precendenceList.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
				
			    }else{
				precendenceList = new HashSet<String>();
				precendenceList.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
			    }
			    failureMap.put("PRECEDENCE", precendenceList);
			}
		    }
		    if (listMap.containsKey("USERID")) {
			String keyValue = listMap.get("USERID");
			if (keyValue.isEmpty() || !(keyValue.length() <= 25)) {
			    Set<String> userId = failureMap.get("USERID");
			    if(null != userId){
				userId.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
				
			    }else{
				userId = new HashSet<String>();
				userId.add("Invalid value : "+ (keyValue.isEmpty() ? "BLANK" : keyValue));
			    }
			    failureMap.put("USERID", userId);
			}
		    }
		}

    }


}
