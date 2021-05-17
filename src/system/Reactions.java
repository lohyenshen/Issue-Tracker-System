package system;

public class Reactions {
    private int likeCount;
    private int loveCount;
    private int hahaCount;
    private int wowCount;
    private int sadCount;
    private int angryCount;

    public Reactions( String reactions) {
        for (String r : reactions.split(",")){
            switch (r) {
                case "like" -> likeCount++;
                case "love" -> loveCount++;
                case "haha" -> hahaCount++;
                case "wow"  -> wowCount++;
                case "sad"  -> sadCount++;
                case "angry"-> angryCount++;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int[] counts =       {likeCount, loveCount, hahaCount, wowCount, sadCount, angryCount};
        String[] reactions = {"like",    "love",    "haha",    "wow",    "sad",    "angry"};
        for (int i = 0; i < counts.length; i++)
            if (counts[i] != 0)
                sb.append( String.format("   %s(%d)", reactions[i], counts[i]) );
        sb.append("\n");
        return sb.toString();
    }

    /**
     * return a long string containing reactions to be inserted into database
     */
    public String asString(){
        StringBuilder sb = new StringBuilder();
        int[] counts =       {likeCount, loveCount, hahaCount, wowCount, sadCount, angryCount};
        String[] reactions = {"like",    "love",    "haha",    "wow",    "sad",    "angry"};
        for (int i = 0; i < counts.length; i++)
            for (int noOfTimes = 0; noOfTimes < counts[i] ; noOfTimes++)
                sb.append( reactions[i] + ",");
        return sb.toString();
    }
}
