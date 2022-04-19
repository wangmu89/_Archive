package com.wmirs.archive.poeaa.patterns.domainlogic.transactionscript;

import com.wmirs.archive.poeaa.patterns.base.Money;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-19
 */
public class Gateway {
    private final Connection conn;

    public Gateway(Connection conn) {
        this.conn = conn;
    }

    private static final String FIND_CONSTRACT_SQL =
            "SELECT c.revenue, c.signed_date, p.type " +
            "   FROM contracts c JOIN products p ON c.product_id = p.id" +
            "   WHERE c.id = ?";

    private static final String FIND_REVENUE_RECOGNITIONS_SQL =
            "SELECT amount" +
            "   FROM revenue_recognitions" +
            "   WHERE contract_id = ? AND recognized_date <= ?";

    private static final String ADD_REVENUE_RECOGNITION_SQL =
            "INSERT INTO revenue_recognitions(contract_id, amount, recognized_date)" +
            "   VALUES(?, ?, ?)";

    /**
     * 查找合同
     *
     * @param contractId 合同ID
     * @return
     */
    public ResultSet findContract(int contractId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(FIND_CONSTRACT_SQL);
        stmt.setInt(1, contractId);

        return stmt.executeQuery();
    }

    /**
     * 查找合同在给定确认日期之前(包含)的所有收入确认记录
     *
     * @param contractId
     * @param recognizedDate
     * @return
     * @throws SQLException
     */
    public ResultSet findContractRecognitions(int contractId, LocalDate recognizedDate) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(FIND_REVENUE_RECOGNITIONS_SQL);
        stmt.setInt(1, contractId);
        stmt.setDate(2, Date.valueOf(recognizedDate));

        return stmt.executeQuery();
    }

    /**
     * 新增合同收入确认记录
     *
     * @param contractId      合同ID
     * @param money           收入
     * @param recognizedDate  确认日期
     * @throws SQLException
     */
    public void addRevenueRecognition(int contractId, Money money, LocalDate recognizedDate) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(ADD_REVENUE_RECOGNITION_SQL);
        stmt.setInt(1, contractId);
        stmt.setBigDecimal(2, money.money());
        stmt.setDate(3, Date.valueOf(recognizedDate));

        stmt.executeUpdate();
    }
}
