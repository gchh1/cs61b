package gitlet;

import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author yhc
 */
public class Commit implements GitletObject {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private final String message;
    /** The timestamp of this Commit. */
    private String timestamp;
    /** The parent(also a Commit) of this Commit. */
    private final String parent;
    /** The second parent. Only exist in merge commit*/
    private final String parent2;
    /** The file that the Commit tracked, represented by blob */
    private Map<String, Blob> trackedFiles;
    /** Format for the timestamp */
    private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss yyyy Z";


    /** Constructor for a Commit object */
    public Commit(String message, String parent, String parent2) {
        // init Commit
        if (parent == null) {
            this.timestamp = getFormatTimestamp(0L);
        }
        Date date = new Date();
        this.timestamp = getFormatTimestamp(date.getTime());
        this.message = message;
        this.parent = parent;
        this.parent2 = parent2;
        this.trackedFiles = new TreeMap<>();
    }

    /** Get a string object of the commit time
     * @param time : use for creating a date object
     * @return : format time
     */
    private String getFormatTimestamp(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date);
    }

    /** Get the id of a Commit, through sha1 */
    @Override
    public String getID() {
        return Utils.sha1(Utils.serialize(this));
    }

    /** Get the blob id of the filename, null if filename is not tracked */
    public String getBlobID(String filename) {
        if (trackedFiles == null || !trackedFiles.containsKey(filename)) {
            return null;
        }
        return trackedFiles.get(filename).getID();
    }

    /** Add and remove file from stage area */
    public void modifyTracedFile(Commit parentCommit, Stage stage) {
        this.trackedFiles = parentCommit.trackedFiles;

        // Add file from stage area
        Map<String, Blob> addition = stage.getAddition();
        trackedFiles.putAll(addition);

        // Remove file from stage area
        Set<String> removal = stage.getRemoval();
        for (String key : removal) {
            trackedFiles.remove(key);
        }

    }

    /** Return the parent Commit through parent id */
    public Commit getParent() {
        if (parent == null) {
            return null;
        }

        // Get the parent commit by sha1 id
        return Repository.getCommit(parent);
    }

    /** Return the second parent Commit through parent2 id */
    public Commit getParent2() {
        if (parent2 == null) {
            return null;
        }

        // Get the parent commit by sha1 id
        return Repository.getCommit(parent2);
    }

    /** Return the traced files in the Commit */
    public Map<String, Blob> getTrackedFiles() {
        return trackedFiles;
    }

    /** Return the message of the Commit */
    public String getMessage() {
        return message;
    }

    /** Override the toString method for log */
    @Override
    public String toString() {

        return "===" +
                // Append commit information
                // like: commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
                "\ncommit " + getID() +

                // Append merge info
                // like: Merge: 4975af1 2c1ead1
                (parent2 == null ? "" : "\nMerge: " + parent.substring(0, 7) + " " + parent2.substring(0, 7)) +

                // Append timestamp
                // like: Date: Thu Nov 9 20:00:05 2017 -0800
                "\nDate: " + timestamp +



                // Append message
                // like: A commit message.
                "\n" + message + "\n";
    }
}
