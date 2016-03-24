package com.zooplus.openexchange.service.training;

import org.junit.Assert;
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
        SquareMatrix m = SquareMatrix.init(3);
        logger.info(m.toString());
        SquareMatrix m1 = m.rotate2steps();
        logger.info(m1.toString());
        logger.info(String.format("%-10s", "-"));
        m = SquareMatrix.init(4);
        logger.info(m.toString());
        m1 = m.rotate2steps();
        logger.info(m1.toString());
    }

    @Test
    public void testMatrixRotationOptimized() throws Exception {
        Matrix m = Matrix.from(new int[][]{
                {1, 2, 3},
                {4, 0, 5},
                {6, 7, 0},
                {8, 0, 9}
        });
        logger.info(m.toString());
        logger.info(String.format("%-10s", "-"));
        m = m.mark();
        logger.info(m.toString());
        Assert.assertTrue(m.equalsTo(new int[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        }));

        m = Matrix.from(new int[][]{
                {1, 1},
                {1, 1}
        });
        Assert.assertTrue(m.equalsTo((new int[][]{
                {1, 1},
                {1, 1}
        })));

        Matrix m1 = Matrix.from(new int[][]{
                {0, 1, 2},
                {1, 4, 5},
                {1, 6, 7}
        });
        logger.info(m1.toString());
        logger.info(m1.mark().toString());
        Assert.assertTrue(m1.equalsTo((new int[][]{
                {0, 0, 0},
                {0, 4, 5},
                {0, 6, 7}
        })));
    }
}
