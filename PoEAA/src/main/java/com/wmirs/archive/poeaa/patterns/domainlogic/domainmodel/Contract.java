package com.wmirs.archive.poeaa.patterns.domainlogic.domainmodel;

import com.wmirs.archive.poeaa.patterns.base.Money;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * @Desc 合同信息
 * @Author WangMu
 * @Create 2022-04-23
 */
@Getter
@ToString
public class Contract {
    /**
     * 合同商品
     */
    private final Product product;
    /**
     * 合同金额
     */
    private final Money revenue;
    /**
     * 合同签订日期
     */
    private final LocalDate signedDate;

    /**
     * 所有的收入确认(金额&确认日期)
     */
    private final List<RevenueRecognition> revenueRecognitionList;

    public Contract(Product product, Money revenue, LocalDate signedDate) {
        this.product = product;
        this.revenue = revenue;
        this.signedDate = signedDate;
        this.revenueRecognitionList = product.getStrategy().calculateRevenueRecognitions(revenue, signedDate);
    }

    /**
     * 给定日期之前(包含)已确认的收入
     *
     * @param asOf
     * @return
     */
    public Money recognizedRevenue(LocalDate asOf) {
        Money resMoney = Money.cny(0.0);
        for(RevenueRecognition revenueRecognition : revenueRecognitionList) {
            if(revenueRecognition.isReconizableBy(asOf)) {
                resMoney = resMoney.add(revenueRecognition.getAmount());
            }
        }
        return resMoney;
    }
}
