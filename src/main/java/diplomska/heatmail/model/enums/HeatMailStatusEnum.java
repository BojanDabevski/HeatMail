package diplomska.heatmail.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HeatMailStatusEnum {

    IMPORTED("IMPORTED"),
    MAIL_IN_PROCESS("MAIL_IN_PROCESS"),
    FINISHED("FINISHED"),
    FAILED("FAILED");


    private String value;
    HeatMailStatusEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static HeatMailStatusEnum fromValue(String text) {
        for (HeatMailStatusEnum b : HeatMailStatusEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
