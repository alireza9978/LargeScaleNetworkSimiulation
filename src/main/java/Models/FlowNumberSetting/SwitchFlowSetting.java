package Models.FlowNumberSetting;

import java.util.Hashtable;

public class SwitchFlowSetting {

    private int switchId;
    private Hashtable<Integer, Integer> setting;

    public SwitchFlowSetting(int switchId) {
        this.switchId = switchId;
        this.setting = new Hashtable<>();
    }

    public void addCondition(int flowNumber, int targetBuffer){
        setting.put(flowNumber, targetBuffer);
    }

    public int getSwitchId() {
        return switchId;
    }

    public void setSwitchId(int switchId) {
        this.switchId = switchId;
    }

    public Hashtable<Integer, Integer> getSetting() {
        return setting;
    }

    public void setSetting(Hashtable<Integer, Integer> setting) {
        this.setting = setting;
    }
}
