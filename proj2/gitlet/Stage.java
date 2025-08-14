package gitlet;

import java.io.Serializable;
import java.util.*;

/** Stage area
 * @author yhc
 */

public class Stage implements Serializable {
    /** Files to be added to a commit */
    private Map<String, Blob> addition;
    /** Files to be removed */
    private Set<String> removal;


    /** Constructor to a Stage */
    public Stage() {
        addition = new TreeMap<>();
        removal = new TreeSet<>();
    }

    /** Add file to stage area */
    public void addFile(String filename, Blob blob) {
        // Remove the blob if the file is the same as the version of the Commit
        removal.remove(filename);
        addition.put(filename, blob);
    }

    /** Remove file */
    public void removeFile(String filename) {
        if (!addition.containsKey(filename)) {
            removal.add(filename);
            Utils.restrictedDelete(filename);
        }
        addition.remove(filename);
    }

    /** Unremoved file */
    public void unremoveFile(String filename) {
        removal.remove(filename);
    }

    /** Clear the stage area */
    public void clear() {
        addition.clear();
        removal.clear();
    }

    /** Get addition */
    public Map<String, Blob> getAddition() {
        return addition;
    }

    /** Get removal */
    public Set<String> getRemoval() {
        return removal;
    }
}
