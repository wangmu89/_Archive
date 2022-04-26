package com.wmirs.archive.poeaa.patterns.domainlogic.domainmodel;

import lombok.Getter;
import lombok.ToString;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-24
 */
@Getter
@ToString
public class Product {
    /**
     * 产品名称
     */
    private final String name;
    /**
     * 确认策略
     */
    private final RecognitionStrategy strategy;

    public Product(String name, RecognitionStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    /**
     * 文字处理软件: [0]
     *
     * @param name
     * @return
     */
    public static Product newWordProcessor(String name) {

        return new Product(name, new CompleteRecognitionStrategy());
    }

    /**
     * 数据库: [0, 30, 60]
     *
     * @param name
     * @return
     */
    public static Product newDatabase(String name) {

        return new Product(name, new EqualDivisionRecognitionStrategy(new int[]{0, 30, 60}));
    }

    /**
     * 电子表格: [0, 60, 90]
     *
     * @param name
     * @return
     */
    public static Product newXls(String name) {

        return new Product(name, new EqualDivisionRecognitionStrategy(new int[]{0, 60, 90}));
    }
}
