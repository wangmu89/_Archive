package com.wmirs.archive.poeaa.patterns.domainlogic.domainmodel;

import com.wmirs.archive.poeaa.patterns.base.Money;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-24
 */
public class EqualDivisionRecognitionStrategy extends RecognitionStrategy {
    /**
     * 平均等分确认收入的日期距离合同签订日期的偏移量
     */
    private final int[] afterSignedOffsets;

    public EqualDivisionRecognitionStrategy(int[] afterSignedOffsets) {
        this.afterSignedOffsets = afterSignedOffsets;
    }

    @Override
    public List<RevenueRecognition> calculateRevenueRecognitions(Money revenue, LocalDate signedDate) {
        Money[] revenues = revenue.allocate(this.afterSignedOffsets.length);

        return IntStream.range(0, afterSignedOffsets.length).boxed()
                .map(i -> new RevenueRecognition(revenues[i], signedDate.plusDays(afterSignedOffsets[i])))
                .collect(Collectors.toList());
    }
}
