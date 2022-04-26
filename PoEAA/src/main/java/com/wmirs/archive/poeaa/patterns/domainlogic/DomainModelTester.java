package com.wmirs.archive.poeaa.patterns.domainlogic;

import com.wmirs.archive.poeaa.patterns.domainlogic.domainmodel.Product;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-24
 */
public class DomainModelTester {

    public static void main(String[] args) {
        Product wp = Product.newWordProcessor("Thinking WP");
        Product xls = Product.newXls("Thinking XLS");
        Product db = Product.newDatabase("Thinking DB");
    }
}
