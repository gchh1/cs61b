package gitlet;

import java.io.Serializable;

/** Interface for a serializable gitlet object
 * @author yhc
 */

public interface GitletObject extends Serializable {
    String getID();
}
