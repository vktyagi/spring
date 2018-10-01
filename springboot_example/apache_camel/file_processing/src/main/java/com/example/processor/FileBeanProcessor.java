package com.example.processor;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.bean.FileInitCriteria;

public class FileBeanProcessor implements Processor{

	/*@Autowired
	private SqlSessionTemplate dcsnTemplateBatch;*/
	@Override
	public void process(Exchange exchange) throws Exception {
		List<Map<String, String>> subscriberList = (List<Map<String, String>>)exchange.getIn().getBody();
		String fileName = exchange.getProperty("fileName").toString();
		String subscriberIncrementalFileToken = exchange.getProperty("subscriberIncrementalFileToken").toString();
		for (Map<String, String> dataMap : subscriberList) {
			int version = 0;
			FileInitCriteria subscriberInitCriteria = null;
			switch (dataMap.get("ACTION")) {
			case "INSERT":
			        if(!fileName.contains(subscriberIncrementalFileToken)){
			                dataMap.put("VERSION", "1");
					dataMap.put("CURRENT_IND", "Y");
					//dcsnTemplateBatch.insert("decisionManager.insertSubscriber", dataMap);
			         }
 				break;

			case "UPDATE":
				//subscriberInitCriteria = dcsnTemplateBatch.selectOne("decisionManager.selectAllSubscriberCriteria", dataMap);
				if (subscriberInitCriteria != null) {
					version = Integer.parseInt(subscriberInitCriteria.getVersion());
					dataMap.put("CURRENT_IND", "N");
					synchronized (this) {
						//dcsnTemplateBatch.update("decisionManager.updateSubscriber", dataMap);
					}
				}
				version = version + 1;
				dataMap.put("VERSION", String.valueOf(version));
				dataMap.put("CURRENT_IND", "Y");
			//	dcsnTemplateBatch.insert("decisionManager.insertSubscriber", dataMap);
				break;

			case "DELETE":
				//subscriberInitCriteria = dcsnTemplateBatch.selectOne("decisionManager.selectAllSubscriberCriteria", dataMap);
				if (subscriberInitCriteria != null) {
					version = Integer.parseInt(subscriberInitCriteria.getVersion());
					dataMap.put("CURRENT_IND", "N");
					synchronized (this) {
						//dcsnTemplateBatch.update("decisionManager.updateSubscriber", dataMap);
					}
				}
				version = version + 1;
				dataMap.put("VERSION", String.valueOf(version));
				dataMap.put("CURRENT_IND", "N");
				//dcsnTemplateBatch.insert("decisionManager.insertSubscriber", dataMap);
				break;

			default:
				throw new IllegalArgumentException("Invalid Action: " + dataMap.get("ACTION"));
			}

		}
	}

}
