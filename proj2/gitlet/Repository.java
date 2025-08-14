package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author yhc
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The HEAD pointer file */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    /** The heads directory */
    public static final File heads = join(GITLET_DIR, "refs", "heads");
    /** The master branch */
    public static final File master = join(heads, "master");
    /** The objects directory */
    public static final File objects = join(GITLET_DIR, "objects");
    /** The stage file */
    public static final File stage = join(GITLET_DIR, "stage");


    /** Creates a new Gitlet version-control system in the current directory. */
    public static void initDir() {
        // Failure case
        if (GITLET_DIR.exists()) {
            System.out.println(Failure.INIT_FAILURE);
            System.exit(0);
        }
        else {
            // Initialize new folders and files
            GITLET_DIR.mkdir();
            heads.mkdirs();
            objects.mkdir();
            try {
                master.createNewFile();
                HEAD.createNewFile();
                stage.createNewFile();
                Utils.writeObject(stage, new Stage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Initialize HEAD pointer
            Utils.writeContents(HEAD, "ref: refs/heads/master");
        }
    }

    /** Get the target file */
    public static File getFile(String filename) {
        File res = Utils.join(CWD, filename);
        if (!res.exists()) {
            System.out.println(Failure.FILE_NOT_EXIST_FAILURE);
            System.exit(0);
        }
        return res;
    }

    /** Update the HEAD pointer */
    public static void updateHead(String id) {
        File currBranch = Utils.join(heads, getCurrentBranch());
        Utils.writeContents(currBranch, id);
    }

    /** Switch branch */
    public static void switchBranch(String branch) {
        Utils.writeContents(HEAD, "ref: refs/heads/", branch);
    }

    /** Save a Serializable objects into the objects directory */
    public static void saveObject(GitletObject o) {
        File file = Utils.join(objects, o.getID());
        Utils.writeObject(file, o);
    }

    /** Write the stage file */
    public static void writeStage(Stage stage) {
        Utils.writeObject(Repository.stage, stage);
    }

    /** Read the stage file */
    public static Stage readStage() {
        return Utils.readObject(Repository.stage, Stage.class);
    }

    /** Get the Commit that HEAD points to */
    public static Commit getHEAD() {
        File file = Utils.join(heads, Repository.getCurrentBranch());
        String commitID = Utils.readContentsAsString(file);

        File commitFile = Utils.join(objects, commitID);
        return Utils.readObject(commitFile, Commit.class);
    }

    /** Get the Commit through sha1 id */
    public static Commit getCommit(String id) {
        File commitFile = Utils.join(objects, id);
        return Utils.readObject(commitFile, Commit.class);
    }

    /** Get all the commit files from a directory */
    public static List<Commit> getAllCommits() {
        // Get all the files in a directory
        List<String> files = Utils.plainFilenamesIn(objects);
        List<Commit> commits = new ArrayList<>();

        // Choose the commit files
        if (files == null) {
            return commits;
        }
        for (String element : files) {
            File file = Utils.join(objects, element);
            Object o = Utils.readObject(file, GitletObject.class);
            if (o instanceof Commit) {
               commits.add((Commit) o);
            }
        }
        return commits;
    }

    /** Get current branch */
    public static String getCurrentBranch() {
        return Utils.readContentsAsString(HEAD).substring(16);
    }

    /** Get all the branches, HEAD branch places the first */
    public static List<String> getBranches() {
        List<String> branches = new ArrayList<>();

        List<String> b = Utils.plainFilenamesIn(heads);
        if (!(b == null)) {
            branches.addAll(b);
        }

        return branches;

    }

    /** Get the Blob through sha1 id */
    public static Blob getBlob(String id) {
        return Utils.readObject(Utils.join(objects, id), Blob.class);
    }

    /** Rewrite a file with the target version */
    public static void checkoutFile(String filename, Commit commit) {
        File target = Utils.join(CWD, filename);
        Utils.writeContents(target, Repository.getBlob(commit.getBlobID(filename)).getContent());
    }

    /** Get the HEAD commit of the target branch */
    public static Commit getBranchHead(String branch) {
        String branchID = Utils.readContentsAsString(Utils.join(heads, branch));
        return Utils.readObject(Utils.join(objects, branchID), Commit.class);
    }

    /** Create a new branch */
    public static void createNewBranch(String branchName) {
        Utils.writeContents(Utils.join(heads, branchName), Repository.getHEAD().getID());
    }

    /** Delete the branch with that name */
    public static void deleteBranch(String branchName) {
        Utils.restrictedDelete(Utils.join(heads, branchName));
    }

    /** Check untracked files */
    public static void checkoutCommit(Commit tarCommit) {

        Commit currCommit = Repository.getHEAD();
        Stage stage = Repository.readStage();

        List<String> workingFiles = Utils.plainFilenamesIn(Repository.CWD);
        if (workingFiles == null) {
            workingFiles = new ArrayList<>();
        }

        for (String file : workingFiles) {
            boolean untrackedExist = (!currCommit.getTrackedFiles().containsKey(file) &&
                    !stage.getAddition().containsKey(file));
            boolean willOverride = tarCommit.getTrackedFiles().containsKey(file);

            if (untrackedExist && willOverride) {
                System.out.println(Failure.UNTRACED_FILE_EXIST);
                System.exit(0);
            }
        }

        // Delete the file that exist in the current commit but not be tracked by tarCommit
        for (String file : currCommit.getTrackedFiles().keySet()) {
            if (!tarCommit.getTrackedFiles().containsKey(file)) {
                Utils.restrictedDelete(file);
            }
        }

        // Override or add files
        for (Map.Entry<String, Blob> entry : tarCommit.getTrackedFiles().entrySet()) {
            if (!currCommit.getTrackedFiles().containsKey(entry.getKey()) ||
                    !entry.getValue().getID().equals(currCommit.getBlobID(entry.getKey()))) {
                Utils.writeContents(Utils.join(Repository.CWD, entry.getKey()), entry.getValue().getContent());
            }
        }

        // Clean the stage area
        stage.clear();
        Repository.writeStage(stage);
    }

    /** Get the split commit */
    public static Commit getSplit(String branchName) {
        Set<String> ancestors = new HashSet<>();
        Queue<Commit> queue = new ArrayDeque<>();

        // Get all the ancestors of HEAD
        queue.add(Repository.getHEAD());
        while (!queue.isEmpty()) {
            Commit c = queue.poll();
            if (c == null || ancestors.contains(c.getID())) {
                continue;
            }
            ancestors.add(c.getID());
            if (!(c.getParent() == null)) {
                queue.add(c.getParent());
            }
            if (!(c.getParent2() == null)) {
                queue.add(c.getParent2());
            }
        }

        queue.add(Repository.getBranchHead(branchName));
        while (!queue.isEmpty()) {
            Commit c = queue.poll();
            if (c == null) {
                continue;
            }
            if (ancestors.contains(c.getID())) {
                return c;
            }
            if (!(c.getParent() == null)) {
                queue.add(c.getParent());
            }
            if (!(c.getParent2() == null)) {
                queue.add(c.getParent2());
            }
        }
        return null;
    }

    /** Handle merge conflict */
    public static void handleConflict(String filename, String headID, String givenID) {
        File file = Utils.join(CWD, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Utils.writeContents(file, "<<<<<<< HEAD\n", getBlob(headID).getContent(),
                "=======\n", getBlob(givenID).getContent(), ">>>>>>>");
    }
}
