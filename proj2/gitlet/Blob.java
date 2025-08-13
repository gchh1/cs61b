package gitlet;


import java.io.File;
import java.io.Serializable;

/** A Blob object stores the contents of a specific file
 * @author yhc
 */

public class Blob implements GitletObject {
    /** Filename that will be construct to a blob */
    private final String filename;
    /** Specific id for a Blob object */
    private final String id;
    /** Content of a Blob object */
    private final byte[] content;

    /** Constructor */
    public Blob(String filename) {
        this.filename = filename;

        // Get the file through the filename
        File file = Repository.getFile(filename);

        // Read the content and generate the sha1 id
        content = Utils.readContents(file);
        id = Utils.sha1(filename, content);

    }

    /** Get the sha1 id of the Blob */
    public String getID() {
        return id;
    }

    /** Get the sha1 id through the filename */
    public static String getID(String filename) {
        File file = Utils.join(Repository.CWD, filename);
        if (!file.exists()) {
            System.exit(0);
        }
        byte[] contents = Utils.readContents(file);
        return Utils.sha1(filename, contents);
    }

    /** Get the contents of the Blob */
    public byte[] getContent() {
        return content;
    }
}
