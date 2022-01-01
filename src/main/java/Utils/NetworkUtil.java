package Utils;

import Models.Network;
import Models.Switch;
import constants.Pair;

import java.util.ArrayList;

public class NetworkUtil {

    public static float[][] getSwitchesConnectionCost(Network network) {
        int temp = network.getSwitchCount() + network.getServerCount();
        float[][] result = new float[temp][temp];
        temp = network.getSwitchCount();
        for (int i = 0; i < network.getSwitchCount(); i++) {
            Switch tempSwitch = network.getSwitch(i);
            ArrayList<Pair<Integer, Integer>> connections = tempSwitch.getSwitchesConnections();
            for (Pair<Integer, Integer> connection : connections) {
                result[i][connection.getKey()] = tempSwitch.getQueueSize(connection.getValue());
            }
            connections = tempSwitch.getServersConnections();
            for (Pair<Integer, Integer> connection : connections) {
                result[i][temp + connection.getKey()] = tempSwitch.getQueueSize(connection.getValue());
            }
        }
        return result;
    }

    public static int[][] getSwitchesConnectionPort(Network network) {
        int temp = network.getSwitchCount() + network.getServerCount();
        int[][] result = new int[temp][temp];
        temp = network.getSwitchCount();
        for (int i = 0; i < network.getSwitchCount(); i++) {
            Switch tempSwitch = network.getSwitch(i);
            ArrayList<Pair<Integer, Integer>> connections = tempSwitch.getSwitchesConnections();
            for (Pair<Integer, Integer> connection : connections) {
                result[i][connection.getKey()] = connection.getValue();
            }
            connections = tempSwitch.getServersConnections();
            for (Pair<Integer, Integer> connection : connections) {
                result[i][temp + connection.getKey()] = connection.getValue();
            }
        }
        return result;
    }

    private static int minDistance(Float[] distance, Boolean[] relaxed) {
        // Initialize min value
        float min = Float.MAX_VALUE;
        int min_index = -1;

        for (int i = 0; i < relaxed.length; i++)
            if (!relaxed[i] && distance[i] <= min) {
                min = distance[i];
                min_index = i;
            }

        return min_index;
    }

    public static ArrayList<Integer> dijkstra(float[][] network, int start, int end) {
        int switchCount = network.length;

        Float[] distance = new Float[switchCount];
        Integer[] from = new Integer[switchCount];
        Boolean[] relaxed = new Boolean[switchCount];
        for (int i = 0; i < switchCount; i++) {
            distance[i] = Float.MAX_VALUE;
            relaxed[i] = false;
            from[i] = -1;
        }

        distance[start] = 0f;
        from[start] = start;

        // Find the shortest path for all vertices
        for (int count = 0; count < switchCount; count++) {
            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = minDistance(distance, relaxed);
            if (u == end) {
                break;
            }
            // Mark the picked vertex as processed
            relaxed[u] = true;

            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < switchCount; v++)

                // Update dist[v] only if is not in sptSet, there is an
                // edge from u to v, and total weight of path from src to
                // v through u is smaller than current value of dist[v]
                if (!relaxed[v] &&
                        network[u][v] != 0 &&
                        distance[u] != Float.MAX_VALUE &&
                        distance[u] + network[u][v] < distance[v]) {
                    distance[v] = distance[u] + network[u][v];
                    from[v] = u;
                }
        }
        ArrayList<Integer> path = new ArrayList<>();
        int pathStart = end;
        path.add(end);
        while (pathStart != start) {
            int temp = from[pathStart];
            path.add(temp);
            pathStart = temp;
        }

        return path;
    }


}
