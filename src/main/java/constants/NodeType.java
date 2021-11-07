package constants;

import java.util.Random;

public enum NodeType {

    health,
    traffic,
    securityCamera,
    powerConsumption;

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

    public static NodeType getInstanceRandom() {
        int tempId = Math.round(random.nextFloat() * 100);
//        0~10 powerConsumption
//        10~30 securityCamera
//        30~60 traffic
//        60~100 health
        if (tempId < 10) {
            return powerConsumption;
        } else if (tempId < 30) {
            return securityCamera;
        } else if (tempId < 60) {
            return traffic;
        } else if (tempId < 100) {
            return health;
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

    public static int getStartDelay(){
        return random.nextInt((int) Constants.CLOCK_IN_SECOND);
    }

}
