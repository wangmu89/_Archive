package com.wmirs.archive.poeaa.patterns.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Currency;
import java.util.Objects;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-18
 */
public final class Money implements Comparable<Money> {
    /**
     * 货币最小面额对应的金额
     */
    private final BigInteger amount;
    /**
     * 货币
     */
    private final Currency currency;

    public Money(long amount, Currency currency) {
        // 以CNY为例，最小面额是'分'，所以金额100元需要转换为10000分(100 * 10^2)，10的幂来源于Currency的defaultFractionDigits
        this(BigInteger.valueOf(amount).multiply(BigInteger.valueOf(10).pow(currency.getDefaultFractionDigits())),
                currency);
    }

    public Money(double amount, Currency currency) {
        // 以CNY为例，最小面额是'分'，可以传入100.0901元，转换为10009.01分，再四舍五入变成10009分
        // 100.0901 -> 10009, 100.0950 -> 10010
        this(BigDecimal.valueOf(amount), currency);
    }

    public Money(BigDecimal amount, Currency currency) {
        // 以CNY为例，最小面额是'分'，可以传入100.0901元，转换为10009.01分，再四舍五入变成10009分
        // 100.0901 -> 10009, 100.0950 -> 10010
        this(amount.multiply(BigDecimal.valueOf(Math.pow(10, currency.getDefaultFractionDigits())))
                        .setScale(0, RoundingMode.HALF_EVEN).toBigInteger(),
                currency);
    }

    private Money(BigInteger amount, Currency currency) {
        if(amount == null || currency == null) {
            throw new IllegalArgumentException("金额[amount]和货币类型[currency]都不能为空");
        }
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Chinese Yuan, CNY
     *
     * @param amount ?.xy元
     * @return
     */
    public static Money cny(double amount) {

        return new Money(amount, Currency.getInstance("CNY"));
    }

    /**
     * Chinese Yuan, CNY
     *
     * @param amount ?.xy元
     * @return
     */
    public static Money cny(BigDecimal amount) {

        return new Money(amount, Currency.getInstance("CNY"));
    }

    /**
     * US Dollar, USD
     *
     * @param amount ?.xy美元
     * @return
     */
    public static Money usd(double amount) {

        return new Money(amount, Currency.getInstance("USD"));
    }

    /**
     * US Dollar, USD
     *
     * @param amount ?.xy美元
     * @return
     */
    public static Money usd(BigDecimal amount) {

        return new Money(amount, Currency.getInstance("USD"));
    }

    /**
     * Japanese Yen, JPY
     *
     * @param amount
     * @return
     */
    public static Money jpy(double amount) {

        return new Money(amount, Currency.getInstance("JPY"));
    }

    /**
     * Japanese Yen, JPY
     *
     * @param amount
     * @return
     */
    public static Money jpy(BigDecimal amount) {

        return new Money(amount, Currency.getInstance("JPY"));
    }

    /**
     * 货币
     *
     * @return
     */
    public Currency getCurrency() {

        return this.currency;
    }

    /**
     * 精确到货币最小面额的金额
     *
     * @return
     */
    public BigInteger getAmount() {

        return this.amount;
    }

    /**
     * 具体的钱，精确到货币的最小面额，以CNY为例，返回的'?.xy元'，例如100.09元
     *
     * @return
     */
    public BigDecimal money() {

        return new BigDecimal(amount, currency.getDefaultFractionDigits());
    }

    /**
     * 带有标识具体的钱，例如：CNY100.09
     *
     * @return
     */
    public String moneyWithSymbol() {

        return currency.getSymbol() + money().toString();
    }

    /**
     * 加上给定的金额
     *
     * @param money
     * @return
     */
    public Money add(Money money) {
        checkIsSameCurrencyAs(money);

        BigInteger newAmount = this.amount.add(money.getAmount());
        return new Money(newAmount, this.currency);
    }

    /**
     * 减去给定的金额
     *
     * @param money
     * @return
     */
    public Money subtract(Money money) {
        checkIsSameCurrencyAs(money);

        BigInteger newAmount = this.amount.subtract(money.getAmount());
        return new Money(newAmount, this.currency);
    }

    /**
     * 金额乘以multiple倍，当出现比最小面额的小数时，采用四舍五入的方式取整
     * 例如：原始金额是10009，乘以3.7倍后是37033.3，最终是37033
     *
     * @param multiple
     * @return
     */
    public Money multiply(double multiple) {

        return multiply(multiple, RoundingMode.HALF_EVEN);
    }

    /**
     * 金额乘以multiple倍，当出现比最小面额的小数时，按照指定的方式进行取整
     *
     * @param multiple
     * @param roundingMode
     * @return
     */
    public Money multiply(double multiple, RoundingMode roundingMode) {
        BigInteger newAmount =
                new BigDecimal(this.amount).multiply(BigDecimal.valueOf(multiple))
                        .setScale(0, roundingMode).toBigInteger();
        return new Money(newAmount, this.currency);
    }

    /**
     * 将金额分成N份，无法被整除时，则将余数R分配个R份
     * 例如: 9.00分成3份则是[3.00, 3.00, 3.00]
     *      10.00分成3份则是[3.33, 3.33, 3.34]
     *      11.00分成3份则是[3.66, 3.67, 3.67]
     *
     * @param n
     * @return
     */
    public Money[] allocate(int n) {
        BigInteger divideAmountDiscardFraction = this.amount.divide(BigInteger.valueOf(n));
        Money lowResMoney = new Money(divideAmountDiscardFraction, this.currency);
        Money highResMoney = new Money(divideAmountDiscardFraction.add(BigInteger.valueOf(1)), this.currency);
        Money[] resMoneys = new Money[n];
        // 无法被整除的最小面额数量（取模）
        int remainder = this.amount.mod(BigInteger.valueOf(n)).intValue();
        for(int i = 0; i < remainder; i++) {
            resMoneys[i] = highResMoney;
        }
        for(int i = remainder; i < n; i++) {
            resMoneys[i] = lowResMoney;
        }
        return resMoneys;
    }

    /**
     * 按照任意比例进行分配
     *
     * @param ratios
     * @return
     */
    public Money[] allocate(long[] ratios) {
        Money[] resMoneys = new Money[ratios.length];
        BigInteger total = BigInteger.valueOf(Arrays.stream(ratios).sum());
        // 分配后剩余的金额
        BigInteger remainderBi = this.amount;
        for(int i = 0; i < ratios.length; i++) {
            BigInteger iAmount = this.amount.multiply(BigInteger.valueOf(ratios[i])).divide(total);
            remainderBi = remainderBi.subtract(iAmount);
            resMoneys[i] = new Money(iAmount, this.currency);
        }
        for(int i = 0; i < remainderBi.intValue(); i++) {
            resMoneys[i] = resMoneys[i].add(new Money(1.0 / Math.pow(10, this.currency.getDefaultFractionDigits()), this.currency));
        }
        return resMoneys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Money money = (Money) o;
        return  this.amount.compareTo(money.amount) == 0 &&
                Objects.equals(this.currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.amount, this.currency);
    }

    @Override
    public int compareTo(Money money) {
        checkIsSameCurrencyAs(money);

        return this.amount.compareTo(money.getAmount());
    }

    @Override
    public String toString() {
        return moneyWithSymbol();
    }

    /**
     * 是否大于给定的Money
     *
     * @param money
     * @return
     */
    public boolean isGreaterThen(Money money) {

        return compareTo(money) > 0;
    }

    /**
     * 是否小于给定的Money
     *
     * @param money
     * @return
     */
    public boolean isLessThen(Money money) {

        return compareTo(money) < 0;
    }

    /**
     * 校验给定Money的货币是否相同
     *
     * @param money
     */
    private void checkIsSameCurrencyAs(Money money) {

        if(!this.currency.equals(money.getCurrency())) {
            throw new IllegalArgumentException(
                    String.format("Money currency[%s != %s] mismatch", this.currency, money.getCurrency()));
        }
    }
}
