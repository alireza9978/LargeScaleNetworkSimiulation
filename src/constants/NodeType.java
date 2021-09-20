package constants;

import java.util.Random;

public enum NodeType {

    health,
    traffic;

    private static Random random = new Random();

    public static int getCount() {
        return 2;
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
        }
        return null;
    }

    public static NodeType getInstanceRandom() {
        int tempId = Math.round(random.nextFloat() * 10);
        if (tempId >= 4) {
            tempId = 1;
        } else {
            tempId = 0;
        }
        switch (tempId) {
            case 0: {
                return health;
            }
            case 1: {
                return traffic;
            }
        }
        return null;
    }


}
