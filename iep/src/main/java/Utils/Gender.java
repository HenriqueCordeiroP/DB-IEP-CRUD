package Utils;

public enum Gender {
    MASCULINO("M"),
    FEMININO("F");

    private final String code;

    Gender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Gender fromCode(String code) {
        for (Gender gender : Gender.values()) {
            if (gender.code.equals(code)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender code: " + code);
    }
}