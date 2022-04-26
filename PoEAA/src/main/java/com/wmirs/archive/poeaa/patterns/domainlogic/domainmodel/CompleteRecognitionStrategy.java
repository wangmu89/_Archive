package com.wmirs.archive.poeaa.patterns.domainlogic.domainmodel;

import com.wmirs.archive.poeaa.patterns.base.Money;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-24
 */
public class CompleteRecognitionStrategy extends RecognitionStrategy {

    @Override
    public List<RevenueRecognition> calculateRevenueRecognitions(Money revenue, LocalDate signedDate) {

        return Collections.singletonList(new RevenueRecognition(revenue, signedDate));
    }
}
