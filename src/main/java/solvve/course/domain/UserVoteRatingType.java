package solvve.course.domain;

public enum UserVoteRatingType {
    R1(1), R2(2), R3(3), R4(4), R5(5), R6(6), R7(7), R8(8), R9(9), R10(10);

    private int value;

    UserVoteRatingType(int value) { this.value = value; }

    public int getValue() { return value; }

    public static UserVoteRatingType parse(int id) {
        UserVoteRatingType userVoteRatingType = null; // Default
        for (UserVoteRatingType item : UserVoteRatingType.values()) {
            if (item.getValue()==id) {
                userVoteRatingType = item;
                break;
            }
        }
        return userVoteRatingType;
    }
}
