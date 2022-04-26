package com.wmirs.archive.poeaa.patterns.domainlogic.domainmodel;

import com.wmirs.archive.poeaa.patterns.base.Money;
import java.time.LocalDate;
import lombok.Getter;
import lombok.ToString;

/**
 * @Desc 收入确认
 * @Author WangMu
 * @Create 2022-04-23
 */
@Getter
@ToString
public class RevenueRecognition {
    /**
     * 确认金额
     */
    private final Money amount;
    /**
     * 确认日期
     */
    private final LocalDate date;

    public RevenueRecognition(Money amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
    }

    /**
     * 在给定日期之前金额是否可以被确认
     *
     * @param asOf
     * @return
     */
    public boolean isReconizableBy(LocalDate asOf) {

        return asOf.isEqual(date) || asOf.isAfter(date);
    }
}
