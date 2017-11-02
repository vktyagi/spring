package com.example.bean;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("subscriberBean")
public class SubscriberBean {

	/*@Autowired
	private SqlSessionTemplate templateBatch;
*/
	@Transactional
	public void upsertData(Exchange exchange, List<Map<String, String>> subscriberList) {
		String fileName = exchange.getProperty("fileName").toString();
		String subscriberIncrementalFileToken = exchange.getProperty("subscriberIncrementalFileToken").toString();
		for (Map<String, String> dataMap : subscriberList) {
			int version = 0;
			SubscriberInitCriteria subscriberInitCriteria = null;
			switch (dataMap.get("ACTION")) {
			case "INSERT":
                		if (fileName.contains(subscriberIncrementalFileToken)) {
                		   // subscriberInitCriteria = templateBatch.selectOne("manager.selectAllSubscriberCriteria", dataMap);
                		    if (subscriberInitCriteria != null) {
                			version = Integer.parseInt(subscriberInitCriteria.getVersion());
                			dataMap.put("CURRENT_IND", "N");
                			synchronized (this) {
                			//    templateBatch.update("manager.updateSubscriber", dataMap);
                			}
                		    }
                		    version = version + 1;
                		    dataMap.put("VERSION", String.valueOf(version));
                		    dataMap.put("CURRENT_IND", "Y");
                		  //  templateBatch.insert("manager.insertSubscriber", dataMap);
                
                		} else {
                		    dataMap.put("VERSION", "1");
                		    dataMap.put("CURRENT_IND", "Y");
                		   // templateBatch.insert("manager.insertSubscriber", dataMap);
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
				//dcsnTemplateBatch.insert("decisionManager.insertSubscriber", dataMap);
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
