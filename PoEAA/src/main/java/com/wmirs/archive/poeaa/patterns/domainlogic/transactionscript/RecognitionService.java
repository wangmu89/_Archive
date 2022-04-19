package com.wmirs.archive.poeaa.patterns.domainlogic.transactionscript;

import com.wmirs.archive.poeaa.patterns.base.Money;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-19
 */
public class RecognitionService {
    private final Gateway gateway;

    public RecognitionService(Gateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 查看合同在给定日期之前(包含)的收入总额(CNY)
     *
     * @param contractId
     * @param recognizedDate
     * @return
     */
    public Money recognizedRevenue(int contractId, LocalDate recognizedDate) {
        Money resMoney = Money.cny(0);
        try {
            ResultSet rs = gateway.findContractRecognitions(contractId, recognizedDate);
            while (rs.next()) {
                resMoney = resMoney.add(Money.cny(rs.getBigDecimal("amount")));
            }
            return resMoney;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算合同的收入确认记录并写入到数据库中
     *
     * @param contractId 合同ID
     */
    public void calculateRevenueRecognitions(int contractId) {
        try {
            // 合同
            ResultSet contractRs = gateway.findContract(contractId);
            if(!contractRs.next()) {
                throw new RuntimeException(String.format("合同[%d]不存在", contractId));
            }
            // 合同金额
            Money contractRevenue = Money.cny(contractRs.getBigDecimal("revenue"));
            // 合同日期
            LocalDate signedDate = contractRs.getDate("signed_date").toLocalDate();
            // 产品类型
            switch (contractRs.getString("type")) {
                case "WP":
                    // 文字处理软件: [0]
                    allocateAndAddRecognition(contractId, contractRevenue, signedDate, new int[]{0});
                    break;
                case "DB":
                    // 数据库: [0, 30, 60]
                    allocateAndAddRecognition(contractId, contractRevenue, signedDate, new int[]{0, 30, 60});
                    break;
                case "XLS":
                    // 电子表格: [0, 60, 90]
                    allocateAndAddRecognition(contractId, contractRevenue, signedDate, new int[]{0, 60, 90});
                    break;
                default:
                    throw new RuntimeException(String.format("未知的产品类型: %s", contractRs.getString("type")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void allocateAndAddRecognition(int contractId, Money contractRevenue, LocalDate signedDate,
                                           int[] recognitionDays) throws SQLException {
        Money[] allocation = contractRevenue.allocate(recognitionDays.length);
        for(int idx = 0; idx < recognitionDays.length; idx++) {
            // 合同金额和签订日期
            gateway.addRevenueRecognition(contractId, allocation[idx], signedDate.plusDays(recognitionDays[idx]));
        }
    }
}
