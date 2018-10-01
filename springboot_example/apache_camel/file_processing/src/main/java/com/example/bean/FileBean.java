package com.example.bean;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("fileBean")
public class FileBean {

	/*@Autowired
	private SqlSessionTemplate templateBatch;
*/
	@Transactional
	public void upsertData(Exchange exchange, List<Map<String, String>> subscriberList) {
		String fileName = exchange.getProperty("fileName").toString();
		String fileToken = exchange.getProperty("fileToken").toString();
		for (Map<String, String> dataMap : subscriberList) {
			int version = 0;
			FileInitCriteria fileInitCriteria = null;
			switch (dataMap.get("ACTION")) {
			case "INSERT":
                		if (fileName.contains(fileToken)) {
                		   // fileInitCriteria = templateBatch.selectOne("manager.selectAllCriteria", dataMap);
                		   //  templateBatch.insert("manager.insert", dataMap);
                
                		} else {
                		   // templateBatch.insert("manager.insert", dataMap);
                		}
                		break;

			case "UPDATE":
				//fileInitCriteria = dcsnTemplateBatch.selectOne("manager.selectAllCriteria", dataMap);
				if (fileInitCriteria != null) {
					version = Integer.parseInt(fileInitCriteria.getVersion());
					dataMap.put("CURRENT_IND", "N");
					synchronized (this) {
						//dcsnTemplateBatch.update("manager.updateSubscriber", dataMap);
					}
				}
				version = version + 1;
				dataMap.put("VERSION", String.valueOf(version));
				dataMap.put("CURRENT_IND", "Y");
				//dcsnTemplateBatch.insert("manager.insert", dataMap);
				break;

			case "DELETE":
				//fileInitCriteria = dcsnTemplateBatch.selectOne("manager.selectAllCriteria", dataMap);
				if (fileInitCriteria != null) {
					synchronized (this) {
						//dcsnTemplateBatch.update("manager.updateSubscriber", dataMap);
					}
				}
				break;

			default:
				throw new IllegalArgumentException("Invalid Action: " + dataMap.get("ACTION"));
			}

		}
	}
}
