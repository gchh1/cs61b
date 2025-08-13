package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.plainFilenamesIn;

/** Provide all of the command of gitlet
 * @author yhc
 */
public class Command {

    /** init command */
    public static void init() {
        // Step1: Creates a new Gitlet version-control system in the current directory.
        Repository.initDir();

        // Step2: Generate a init commit
        Commit initCommit = new Commit("initial commit", null);

        // Step3: Create a branch called master
        Repository.updateHead(initCommit.getID());

        // Step4: Save the Commit
        Repository.saveObject(initCommit);
    }

    /** add command */
    public static void add(String filename) {
        // Get the blob by the filename
        Blob blob = new Blob(filename);

        // Add the file into objects folder
        Repository.saveObject(blob);

        Stage stage = new Stage();

        // Judge whether the content of the file named filename is modified,
        // do not stage it, meaning that remove the file from both addition and removal
        Commit HEADCommit = Repository.getHEAD();
        if (blob.getID().equals(HEADCommit.getBlobID(filename))) {
             stage.removeFile(filename);
        } else {
            // Add the file into the stage area
            stage.addFile(filename, blob);
        }

        // Save the stage
        Repository.writeStage(stage);
    }

    /** commit command */
    public static void commit(String message) {
        // Get the HEAD commit -- the parent
        Commit parent = Repository.getHEAD();

        // Read the stage area
        Stage stage = Repository.readStage();

        // Declare a Commit object
        Commit commit = new Commit(message, parent.getID());

        // Add and remove file from the parent Commit
        commit.modifyTracedFile(parent, stage);

        // Clear the stage area
        stage.clear();


        // Save the Commit and the stage area
        Repository.writeStage(stage);
        Repository.saveObject(commit);

        // Update the HEAD pointer
        Repository.updateHead(commit.getID());
    }

    /** rm command */
    public static void rm(String filename) {
        Stage stage = Repository.readStage();
        // Throw error if the file is not tracked or staged
        if (!stage.getAddition().containsKey(filename) && Repository.getHEAD().getBlobID(filename) == null) {
            System.out.println(Failure.RM_ERROR_FAILURE);
            System.exit(0);
        }

        // Add the file named filename to the removal
        stage.removeFile(filename);

        // Save the stage area
        Repository.writeStage(stage);
    }

    /** log command */
    public static void log() {
        // Iterate the commits
        Commit it = Repository.getHEAD();

        /* TODO: Handle merge case
            ===
            commit 3e8bf1d794ca2e9ef8a4007275acf3751c7170ff
            Merge: 4975af1 2c1ead1
            Date: Sat Nov 11 12:30:00 2017 -0800
            Merged development into master.
         */


        // Traverse the commits from HEAD commit through the parent id
        while (it != null) {
            System.out.println(it.toString());
            it = it.getParent();
        }
    }

    /** global-log command
     * iterate all the commit
     */
    public static void globalLog() {
        // Get all the commits
        List<Commit> commits = Repository.getAllCommits();
        if (commits.isEmpty()) {
            System.exit(0);
        }

        // Print commits
        for (Commit commit : commits) {
            System.out.println(commit);
        }
    }

    /** status command */
    public static void status() {
        // Step1: Print the branches, signing a "*" in front of the current branch
        System.out.println("=== Branches ===");
        List<String> branches = Repository.getBranches();
        // Print branches
        if (!branches.isEmpty()) {
            branches.sort(String::compareTo);
            for (String branch : branches) {
                if (branch.equals(Repository.getCurrentBranch())) {
                    System.out.print("*");
                }
                System.out.println(branch);
            }
        }
        // Print 2 line breaks
        System.out.print("\n\n");

        // Step2: Print the staged files
        System.out.println("=== Staged Files ===");
        Stage stage = Repository.readStage();
        for (String key : stage.getAddition().keySet()) {
            System.out.println(key);
        }
        if (stage.getAddition().isEmpty()) {
            System.out.print("\n");
        } else {
            System.out.print("\n\n");
        }

        // Step3: Print the removed files
        System.out.println("=== Removed Files ===");
        for (String key : stage.getRemoval()) {
            System.out.println(key);
        }

        if (stage.getRemoval().isEmpty()) {
            System.out.print("\n");
        } else {
            System.out.print("\n\n");
        }

        // Step4: Print modifications not staged for commit
        System.out.println("=== Modifications Not Staged For Commit ===");
        Commit currentCommit = Repository.getHEAD();

        Map<String, Blob> tracedFiles = currentCommit.getTrackedFiles();
        Map<String, String> modifiedFiles = new TreeMap<>();
        List<String> untrackedFiles = new ArrayList<>();

        List<String> workingFiles = plainFilenamesIn(Repository.CWD);
        if (workingFiles == null) {
            workingFiles = new ArrayList<>();
        }

        // A set that contains all the files
        Set<String> files = new TreeSet<>(tracedFiles.keySet());
        files.addAll(stage.getAddition().keySet());
        files.addAll(workingFiles);


        // case1: Tracked in the current commit, changed in the working directory, but not staged
        // case2: Staged for addition, but with different contents than in the working directory
        // case3: Staged for addition, but deleted in the working directory
        // case4: Not staged for removal, but tracked in the current commit and deleted from the working directory

        for (String file : files) {
            // case: untracked
            if (!tracedFiles.containsKey(file) && !stage.getAddition().containsKey(file)) {
                untrackedFiles.add(file);
                continue;
            }

            // case4: deleted
            if (tracedFiles.containsKey(file) && !workingFiles.contains(file) && !stage.getRemoval().contains(file)) {
                modifiedFiles.put(file, "(deleted)");
                continue;
            }

            // case1: tracked but modified
            if (tracedFiles.containsKey(file) && workingFiles.contains(file) && !stage.getAddition().containsKey(file)) {
                if (!Blob.getID(file).equals(tracedFiles.get(file).getID())) {
                    modifiedFiles.put(file, "(modified)");
                }
            }

            // case2: staged but modified
            if (stage.getAddition().containsKey(file) && workingFiles.contains(file)) {
                if (!Blob.getID(file).equals(stage.getAddition().get(file).getID())) {
                    modifiedFiles.put(file, "(modified)");
                }
            }
        }

        for (Map.Entry<String, String> entry : modifiedFiles.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        if (!modifiedFiles.isEmpty()) {
            System.out.print("\n\n");
        } else {
            System.out.print("\n");
        }

        // Step5: Print untracked files
        System.out.println("=== Untracked Files ===");


        // Print 2 line breaks
        for (String s : untrackedFiles) {
            System.out.println(s);
        }
        if (!untrackedFiles.isEmpty()) {
            System.out.print("\n\n");
        } else {
            System.out.print("\n");
        }
    }

    /** find command
     * function: find the commits with the given message and print out the id */

    public static void find(String message) {
        List<Commit> commits = Repository.getAllCommits();

        // Filter the commit
        for (Commit item : commits) {
            if (item.getMessage().equals(message)) {
                System.out.println(item.getID());
            }
        }

    }

    /** checkout command */
    public static void checkout(String[] args) {

        if (args.length == 3 && args[1].equals("--"))  {
            /* version1: java gitlet.Main checkout -- [file name]
            Takes the version of the file as it exists in the head commit and puts it in the working directory,
            overwriting the version of the file that’s already there if there is one.
            The new version of the file is not staged.
            */
            Commit curr = Repository.getHEAD();
            Failure.checkFileExitInCommit(args[2], curr);
            Repository.checkoutFile(args[2], curr);

        } else if (args.length == 4 && args[2].equals("--")) {
            /* version2: java gitlet.Main checkout [commit id] -- [file name]
            Takes the version of the file as it exists in the commit with the given id, and puts it in the
            working directory, overwriting the version of the file that’s already there if there is one.
            The new version of the file is not staged.
             */
            Commit curr = Repository.getCommit(args[1]);
            if (curr == null) {
                System.out.println(Failure.COMMIT_NOT_EXIST);
                System.exit(0);
            }
            Failure.checkFileExitInCommit(args[3], curr);
            Repository.checkoutFile(args[3], curr);

        } else if (args.length == 2) {
            /* version3: java gitlet.Main checkout [branch name]
            Takes all files in the commit at the head of the given branch, and puts them in the working directory,
             overwriting the versions of the files that are already there if they exist.
             Also, at the end of this command, the given branch will now be considered the current branch (HEAD).
             Any files that are tracked in the current branch but are not present in the checked-out branch are deleted.
              The staging area is cleared, unless the checked-out branch is the current branch.
             */

            // Failure case check
            if (!Repository.getBranches().contains(args[1])) {
                System.out.println(Failure.BRANCH_NOT_EXIST);
                System.exit(0);
            }
            if (Repository.getCurrentBranch().equals(args[1])) {
                System.out.println(Failure.NO_NEED_CHECKOUT_BRANCH);
                System.exit(0);
            }

            Repository.checkoutCommit(Repository.getBranchHead(args[1]));

            // Switch branch
            Repository.switchBranch(args[1]);

        } else {
            System.out.println(Failure.INCORRECT_OPERANDS_FAILURE);
            System.exit(0);
        }
    }

    /** branch command */
    public static void branch(String branchName) {
        if (Repository.getBranches().contains(branchName)) {
            System.out.println(Failure.BRANCH_NAME_EXIST);
            System.exit(0);
        }
        Repository.createNewBranch(branchName);

    }

    /** rm-branch command */
    public static void rmBranch(String branchName) {
        // Failure case
        if (Repository.getCurrentBranch().equals(branchName)) {
            System.out.println(Failure.CANNOT_RM_CURRENT_BRANCH);
            System.exit(0);
        }
        if (!Repository.getCurrentBranch().contains(branchName)) {
            System.out.println(Failure.BRANCH_NAME_NOT_EXIST);
            System.exit(0);
        }

        // Delete the branch
        Repository.deleteBranch(branchName);
    }

    /** reset command */
    public static void reset(String commitID) {
        if (Repository.getCommit(commitID) == null) {
            System.out.println(Failure.COMMIT_NOT_EXIST);
            System.exit(0);
        }

        Repository.checkoutCommit(Repository.getCommit(commitID));

        Repository.updateHead(commitID);
    }

    /** merge command */
    public static void merge(String branchName) {
        
    }
}
