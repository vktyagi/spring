package com.constant;

public enum UserAttributes {
    DESIGNATION {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/designation";
	}
    },

    ORGANIZATION {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/organization";
	}
    },
    ROLE {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/role";
	}
    },
    APPLICATIONTIER {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/applicationtier";
	}
    },
    VERSION {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/version";
	}
    },
    ISS {
	@Override
	public String toString() {
	    return "iss";
	}
    },
    APPLICATIONNAME {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/applicationname";
	}
    },
    ENDUSERTENANTID {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/enduserTenantId";
	}
    },
    GIVENNAME {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/givenname";
	}
    },
    SUBSCRIBER {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/subscriber";
	}
    },
    TIER {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/tier";
	}
    },
    EMAILADDRESS {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/emailaddress";
	}
    },
    LASTNAME {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/lastname";
	}
    },
    FIRSTNAME {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/firstname";
	}
    },
    EXP {
	@Override
	public String toString() {
	    return "exp";
	}
    },
    APPLICATIONID {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/applicationid";
	}
    },
    USERTYPE {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/usertype";
	}
    },
    apicontext {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/apicontext";
	}
    },
    CUSTOMDATA {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/customData";
	}
    },
    ENDUSER {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/enduser";
	}
    },
    EMAIL {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/emailaddress";
	}
    },
    APICONTEXT {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/apicontext";
	}
    },
    LOCATION {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/region";
	}
    },
    KEYTYPE {
	@Override
	public String toString() {
	    return "http://wso2.org/claims/keytype";
	}
    }

}
