package com.best.bruce.guessnum;

import java.util.Random;

/**
 * Created by Bruce on 2015/11/8.
 */
public class Guess {

    private Random random;

    public Guess() {
        random = new Random();
    }

    public int getRandomNum() {
        return random.nextInt(100) + 1;
    }

    public int judeResult(int inputNum, int randNum) {
//     0.猜对了
//     1.小于
//     2.大于
        if (inputNum == randNum) {
            return 0;
        } else if (inputNum < randNum) {
            return 1;
        } else {
            return 2;
        }


    }

    public String transResult(int num) {
        if (num == 0) {
            return "你终于猜对了，真厉害";
        } else if (num == 1) {
            return "你输入的数字小了，加油";
        } else

        {
            return "你输入的数字大了，加油";
        }


    }
}
