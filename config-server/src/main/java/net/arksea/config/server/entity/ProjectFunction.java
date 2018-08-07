package net.arksea.config.server.entity;

/**
 * 项目权限类型
 * Created by xiaohaixing on 2018/8/1.
 */
public enum ProjectFunction {
    QUERY,   //Project及其所有Config项查询
    MANAGER, //Project修改, Config新增、删除、备注、Schema修改，Config权限授予
    CONFIG   //项目所有Config修改
}
