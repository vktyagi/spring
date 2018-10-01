package com.example.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInitCriteria {

    private String snapshotDate;
    private String accountNumber;
    private String subscriberId;
    private String esn;
    private String uccid;
    private String mdn;
    private String devNxtUpgrdEligDate;
    private String emailAddress;
    private String emailMarketingOptout;
    private String action;
    private String currentInd;
    private String version;
}