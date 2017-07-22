package tech.shann.entity.enums;

/**
 * Created by shann on 17/7/3.
 */
public enum Sex implements IntEnum{
    MALE(1, "男"),
    FEMALE(2, "女"),
    UNKNOWN(0, "未知");
    private int value;
    private String desc;
    Sex(int value, String desc){
        this.value = value;
        this.desc = desc;
    }
    public int getValue() {
        return value;
    }
    public String getDesc() {
        return desc;
    }
    public static Sex valueOf(int value) {
        Sex sex = UNKNOWN;
        switch (value) {
            case 1: sex = MALE;break;
            case 2: sex = FEMALE;break;
            default: sex = UNKNOWN;
        }
        return sex;
    }
}

