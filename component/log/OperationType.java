package com.neo.interceptor;


public enum OperationType {

    ADD(1, "add", "添加"),
    UPDATE(2, "update", "更新"),
    DELETE(3, "delete", "删除"),
    MOVE(4, "move", "迁移"),
    DETAIL(5, "detail", "详情"),
    BIND(5, "bind", "绑定"),
    UNBIND(6, "unbind", "解绑"),;
    private int id;
    private String operation;
    private String desc;

    OperationType(int id, String operation, String desc) {
        this.id = id;
        this.operation = operation;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public String getDesc() {
        return desc;
    }
}
