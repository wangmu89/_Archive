package com.wmirs.archive.poeaa.patterns.domainlogic.transactionscript;

import com.wmirs.archive.poeaa.patterns.PoeaaConnection;
import java.sql.Connection;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-19
 */
public class TsExample {
    /*
     * MYSQL数据库表和初始化: classpath:/sql/transaction_script.sql
     *  - products: 产品表
     *  - contracts: 卖出产品的合同表
     *  - revenue_recognitions: 合同收入确认表
     */

    public static void main(String[] args) {
        Connection conn = PoeaaConnection.getConnection();
    }
}
