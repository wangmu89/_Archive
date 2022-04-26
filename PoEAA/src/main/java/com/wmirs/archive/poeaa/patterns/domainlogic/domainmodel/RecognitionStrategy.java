package com.wmirs.archive.poeaa.patterns.domainlogic.domainmodel;

import com.wmirs.archive.poeaa.patterns.base.Money;
import java.time.LocalDate;
import java.util.List;

/**
 * @Desc 确认策略
 * @Author WangMu
 * @Create 2022-04-24
 */
public abstract class RecognitionStrategy {
    /**
     * 计算合同的收入确认策略
     *
     * @param revenue 合同金额
     * @param signedDate 签订日期
     * @return
     */
    public abstract List<RevenueRecognition> calculateRevenueRecognitions(Money revenue, LocalDate signedDate);
}
