package com.example.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Vinit Tyagi
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditTable {
   private String auditid;
   private String tablename;
   private String identifyingcriteria;
   private String event;
   private String update_timestamp;
   private String dbuser;
   private String colname;
   private String oldcolval;
   private String newcolval;
}
