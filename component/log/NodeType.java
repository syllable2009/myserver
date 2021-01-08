package com.neo.interceptor;


public enum NodeType {

    PRODUCT_LINE(1, "product_line", "line", "产品线"),
    PRODUCT(2, "product", "product", "产品"),
    APP(3, "app", "app", "应用"),
    ALL(4, "all", "all", "所有节点"),
    TEAM(5, "team", "team", "Team"),
    MIDDLEWARE(6, "middleware", "middleware", "中间件"),
    DOMAIN(7, "domain", "domain", "域名"),
    INTERVIWER(8, "interview", "interview", "面试"),
    CLBX(8, "travel_reimbursement", "CLBX", "差旅报销");

    private Integer id;
    private String type;
    private String shortType;
    private String desc;

    NodeType(Integer id, String type, String shortType, String desc) {
        this.id = id;
        this.type = type;
        this.shortType = shortType;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getShortType() {
        return shortType;
    }
}
