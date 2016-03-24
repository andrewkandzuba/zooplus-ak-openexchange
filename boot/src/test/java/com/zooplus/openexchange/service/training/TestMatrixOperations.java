package com.zooplus.openexchange.service.training;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMatrixOperations {
    private static final Logger logger = LoggerFactory.getLogger(TestMatrixOperations.class);

    @Test
    public void testArrayShift() throws Exception {
        RingInt.from(new int[]{1, 2, 3}).shift(1).equalsTo(new int[]{3, 2, 1});
        RingInt.from(new int[]{1, 2, 3}).shift(12).equalsTo(new int[]{1, 2, 3});
        RingInt.from(new int[]{1, 2, 3}).shift(-2).equalsTo(new int[]{3, 2, 1});
        RingInt.from(new int[]{1, 2, 3, 4, 5}).shift(-2).equalsTo(new int[]{3, 4, 5, 1, 2});
        RingInt.from(new int[]{1, 2, 3}).shift(-4).equalsTo(new int[]{1, 2, 3});
    }

    @Test
    public void testMatrixRotation() throws Exception {
        Matrix m = Matrix.init(3);
        logger.info(m.toString());
        Matrix m1 = m.rotate2steps();
        logger.info(m1.toString());
        logger.info(String.format("%-10s","-"));
        m = Matrix.init(4);
        logger.info(m.toString());
        m1 = m.rotate2steps();
        logger.info(m1.toString());
    }

    @Test
    public void testMatrixRotationOptimized() throws Exception {
        Matrix m = Matrix.init(3);
        logger.info(m.toString());
        Matrix m1 = m.rotate1step();
        logger.info(m1.toString());
        logger.info(String.format("%-10s","-"));
        m = Matrix.init(4);
        logger.info(m.toString());
        m1 = m.rotate1step();
        logger.info(m1.toString());
    }
}
