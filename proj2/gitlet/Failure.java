package gitlet;


/** A class that handles all the failure case messages
 * @author yhc
 */

public class Failure {

    protected static final String NO_INPUT_FAILURE = "Please enter a command.";
    protected static final String INPUT_NOT_EXIST_FAILURE = "No command with that name exist.";
    protected static final String INCORRECT_OPERANDS_FAILURE = "Incorrect operands.";
    protected static final String NO_INIT_FAILURE = "Not in an initialized Gitlet directory.";
    protected static final String INIT_FAILURE = "A Gitlet version-control system already exits " +
                                                "in the current directory";
    protected static final String FILE_NOT_EXIST_FAILURE = "File does not exits.";
    protected static final String RM_ERROR_FAILURE = "No reason to remove the file.";
    protected static final String FILE_NOT_EXIST_COMMIT_FAILURE = "File does not exist in that                                                                     commit.";
    protected static final String COMMIT_NOT_EXIST = "No commit with that id exists.";
    protected static final String BRANCH_NOT_EXIST = "No such branch exists.";
    protected static final String NO_NEED_CHECKOUT_BRANCH = "No need to checkout the current branch.";
    protected static final String UNTRACED_FILE_EXIST = "There is an untracked file in the way; delete it, " +
                                                "or add and commit it first.";
    protected static final String BRANCH_NAME_EXIST = "A branch with that name already exists.";
    protected static final String BRANCH_NAME_NOT_EXIST = "A branch with that name does not exist.";
    protected static final String CANNOT_RM_CURRENT_BRANCH = "Cannot remove the current branch.";
    protected static final String SPLIT_SAME_BRANCH = "Given branch is an ancestor of the current branch.";
    protected static final String SPLIT_SAME_HEAD = "Current branch fast-forwarded.";
    protected static final String MERGE_CONFLICT = "Encountered a merge conflict.";


    // Check if the directory has been initialized
    public static void checkInit() {
        if (!Repository.GITLET_DIR.exists()) {
            System.out.println(NO_INIT_FAILURE);
            System.exit(0);
        }

    }

    // Check if the operand number is correct
    public static void checkOPNum(String[] args, int opNum) {
        if (args.length != opNum) {
            System.out.println(INCORRECT_OPERANDS_FAILURE);
            System.exit(0);
        }
    }

    // Check if the file with the name exits in the commit
    public static void checkFileExitInCommit(String filename, Commit commit) {
        if (commit.getBlobID(filename) == null) {
            System.out.println(Failure.FILE_NOT_EXIST_COMMIT_FAILURE);
            System.exit(0);
        }
    }

}
