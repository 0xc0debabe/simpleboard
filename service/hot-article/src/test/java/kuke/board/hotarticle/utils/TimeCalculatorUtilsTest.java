package kuke.board.hotarticle.utils;

import org.junit.jupiter.api.Test;

import java.time.Duration;

class TimeCalculatorUtilsTest {

    @Test
    void test() {
        Duration duration = TimeCalculatorUtils.calculateDurationToMidnight();
        System.out.println("restTIME= " + duration.getSeconds() / 60);
    }

}