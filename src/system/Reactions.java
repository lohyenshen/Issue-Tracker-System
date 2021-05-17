package system;

public class Reactions {
    private int likeCount;
    private int loveCount;
    private int hahaCount;
    private int wowCount;
    private int sadCount;
    private int angryCount;

    /**
     * construct a reaction object
     * 1) split a string "reactions" into array
     * 2) for each individual component in the array, cast it into int and store in int array
     */
    public Reactions(String reactions) {
        String[] reactionCountsString = reactions.split("\\s+");
        int[]    reactionCountsInt    = new int[6];
        for (int i = 0; i < reactionCountsString.length; i++)
            reactionCountsInt[i] = Integer.parseInt(reactionCountsString[i]);

        likeCount  = reactionCountsInt[0];
        loveCount  = reactionCountsInt[1];
        hahaCount  = reactionCountsInt[2];
        wowCount   = reactionCountsInt[3];
        sadCount   = reactionCountsInt[4];
        angryCount = reactionCountsInt[5];
    }

    /**
     * - convert a reaction object into String
     * - only reaction with >= 1 frequency will be displayed
     * - reaction with 0 frequency will be ignored
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] reactions = {"like",    "love",    "haha",    "wow",    "sad",    "angry"};
        int[] counts =       {likeCount, loveCount, hahaCount, wowCount, sadCount, angryCount};

        for (int i = 0; i < counts.length; i++)
            if (counts[i] > 0)
                sb.append( String.format("   %s(%d)", reactions[i], counts[i]) );
        sb.append("\n");
        return sb.toString();
    }


    /**
     * update the counts of a reaction object based on inputReaction
     */
    public void updateCounts( String inputReaction){
        switch (inputReaction){
            case "like"  -> likeCount++;
            case "love"  -> loveCount++;
            case "haha"  -> hahaCount++;
            case "wow"   -> wowCount++;
            case "sad"   -> sadCount++;
            case "angry" -> angryCount++;
        }
    }

    /**
     * return a String in the format (0 0 0 0 0 0)
     * where each integer represents the frequency of a reaction
     */
    public String asDatabaseString() {
        StringBuilder sb = new StringBuilder();
        sb.append(likeCount).append(" ");
        sb.append(loveCount).append(" ");
        sb.append(hahaCount).append(" ");
        sb.append(wowCount).append(" ");
        sb.append(sadCount).append(" ");
        sb.append(angryCount);
        return sb.toString();
    }
}
