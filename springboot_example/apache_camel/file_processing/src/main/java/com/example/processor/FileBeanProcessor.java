package com.example.processor;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.example.bean.FileInitCriteria;

public class FileBeanProcessor implements Processor{

	/*@Autowired
	private SqlSessionTemplate templateBatch;*/
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
					//templateBatch.insert("manager.insert", dataMap);
			         }
 				break;

			case "UPDATE":
				//initCriteria = templateBatch.selectOne("manager.selectAll", dataMap);
				if (subscriberInitCriteria != null) {
					version = Integer.parseInt(subscriberInitCriteria.getVersion());
					dataMap.put("CURRENT_IND", "N");
					synchronized (this) {
						//templateBatch.update("manager.update", dataMap);
					}
				}
				version = version + 1;
				dataMap.put("VERSION", String.valueOf(version));
				dataMap.put("CURRENT_IND", "Y");
			//	templateBatch.insert("manager.insert", dataMap);
				break;

			case "DELETE":
				//initCriteria = templateBatch.selectOne("manager.selectAll", dataMap);
				//templateBatch.insert("manager.insert", dataMap);
				break;

			default:
				throw new IllegalArgumentException("Invalid Action: " + dataMap.get("ACTION"));
			}

		}
	}

}
