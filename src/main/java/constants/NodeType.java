package constants;

import java.util.Random;

import static constants.Constants.*;

public enum NodeType {

    health("health"),
    traffic("traffic"),
    securityCamera("security camera"),
    powerConsumption("power consumption");

    @Override
    public String toString() {
        return this.name;
    }

    NodeType(String name) {
        this.name = name;
    }

    public final String name;

    private static final Random random = new Random();

    public static int getCount() {
        return 4;
    }

    public static NodeType getInstance(String id) {
        int tempId = Integer.parseInt(id);
        switch (tempId) {
            case 0: {
                return health;
            }
            case 1: {
                return traffic;
            }
            case 2: {
                return securityCamera;
            }
            case 3: {
                return powerConsumption;
            }
        }
        return null;
    }

    public static NodeType getInstance(int id) {
        switch (id) {
            case 0: {
                return health;
            }
            case 1: {
                return traffic;
            }
            case 2: {
                return securityCamera;
            }
            case 3: {
                return powerConsumption;
            }
        }
        return null;
    }

    public static NodeType getInstanceRandom() {
        int tempId = Math.round(random.nextFloat() * 100);
        if (tempId < CLASS_ONE_PROBABILITY * 100) {
            return health;
        } else if (tempId < (CLASS_ONE_PROBABILITY + CLASS_TWO_PROBABILITY) * 100) {
            return traffic;
        } else if (tempId < (CLASS_ONE_PROBABILITY + CLASS_TWO_PROBABILITY +
                CLASS_THREE_PROBABILITY) * 100) {
            return securityCamera;
        } else if (tempId < (CLASS_ONE_PROBABILITY + CLASS_TWO_PROBABILITY +
                CLASS_THREE_PROBABILITY + CLASS_FOUR_PROBABILITY) * 100) {
            return powerConsumption;
        }
        return health;
    }

    public int getPeriod() {
        switch (this) {
            case health:
                return Constants.CLASS_ONE_CYCLE;
            case traffic:
                return Constants.CLASS_TWO_CYCLE;
            case securityCamera:
                return Constants.CLASS_THREE_CYCLE;
            case powerConsumption:
                return Constants.CLASS_FOUR_CYCLE;
        }
        return -1;
    }

    public int getSize() {
        switch (this) {
            case health:
                return Constants.CLASS_ONE_SIZE;
            case traffic:
                return Constants.CLASS_TWO_SIZE;
            case securityCamera:
                return Constants.CLASS_THREE_SIZE;
            case powerConsumption:
                return Constants.CLASS_FOUR_SIZE;
        }
        return -1;
    }

    public int toInt() {
        switch (this) {
            case health:
                return 0;
            case traffic:
                return 1;
            case securityCamera:
                return 2;
            case powerConsumption:
                return 3;
        }
        return -1;
    }

    public static int getStartDelay() {
        return random.nextInt((int) Constants.CLOCK_IN_SECOND);
    }

}
