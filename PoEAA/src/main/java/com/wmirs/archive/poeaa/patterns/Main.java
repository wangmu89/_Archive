package com.wmirs.archive.poeaa.patterns;

import com.wmirs.archive.poeaa.patterns.base.Money;
import java.util.Arrays;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-16
 */
public class Main {

    public static void main(String[] args) {
        Money money0 = Money.jpy(9);
        Money money1 = Money.jpy(10);
        Money money2 = Money.usd(0.05);

        System.out.println(Arrays.toString(money0.allocate(new long[]{3, 3, 3})));
        System.out.println(Arrays.toString(money1.allocate(new long[]{3, 3, 3})));
        System.out.println(Arrays.toString(money2.allocate(new long[]{3, 7})));

    }
}
