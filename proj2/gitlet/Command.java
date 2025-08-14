package gitlet;


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
        Commit initCommit = new Commit("initial commit", null, null);

        // Step3: Create a branch called master
        Repository.updateHead(initCommit.getID());

        // Step4: Save the Commit
        Repository.saveObject(initCommit);
    }

    /** add command */
    public static void add(String filename) {
        Blob blob = new Blob(filename);
        Repository.saveObject(blob);

        Stage stage = Repository.readStage();
        Commit HEADCommit = Repository.getHEAD();

        String headBlobID = HEADCommit.getBlobID(filename);
        boolean sameAsHead = blob.getID().equals(headBlobID);

        if (sameAsHead) {
            // 如果文件在 removed 集合中 -> unremove
            if (stage.getRemoval().contains(filename)) {
                stage.unremoveFile(filename);  // 只从 removed 集合中删除
            }
            // 如果文件不在 removed 集合中，且和 HEAD 一样 -> 什么也不做
        } else {
            // 普通 add
            stage.addFile(filename, blob);
        }

        Repository.writeStage(stage);
    }

    /** commit command */
    public static void commit(String message, String parent2, boolean isMerge) {
        // Handle empty message
        if (message.isEmpty()) {
            System.out.println(Failure.NO_COMMIT_MESSAGE);
            System.exit(0);
        }

        // Get the HEAD commit -- the parent
        Commit parent = Repository.getHEAD();

        // Read the stage area
        Stage stage = Repository.readStage();

        // Handle empty stage area
        if (!isMerge && stage.getAddition().isEmpty() && stage.getRemoval().isEmpty()) {
            System.out.println(Failure.STAGE_EMPTY);
            System.exit(0);
        }

        // Declare a Commit object
        Commit commit = new Commit(message, parent.getID(), parent2);

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
        System.out.print("\n");

        // Step2: Print the staged files
        System.out.println("=== Staged Files ===");
        Stage stage = Repository.readStage();
        for (String key : stage.getAddition().keySet()) {
            System.out.println(key);
        }
        System.out.print("\n");


        // Step3: Print the removed files
        System.out.println("=== Removed Files ===");
        for (String key : stage.getRemoval()) {
            System.out.println(key);
        }

        System.out.print("\n");


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


        System.out.print("\n");

        // Step5: Print untracked files
        System.out.println("=== Untracked Files ===");


        // Print 2 line breaks

        System.out.print("\n");
    }

    /** find command
     * function: find the commits with the given message and print out the id */

    public static void find(String message) {
        List<Commit> commits = Repository.getAllCommits();
        boolean isFound = false;

        // Filter the commit
        for (Commit item : commits) {
            if (item.getMessage().equals(message)) {
                System.out.println(item.getID());
                isFound = true;
            }
        }

        if (!isFound) {
            System.out.println(Failure.FIND_NO_COMMIT);
            System.exit(0);
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
        if (!Repository.getBranches().contains(branchName)) {
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
        // Step1: get the split point
        Commit splitCommit = Repository.getSplit(branchName);
        Commit currCommit = Repository.getHEAD();
        Commit branchCommit = Repository.getBranchHead(branchName);
        if (splitCommit != null) {
            if (splitCommit.getID().equals(branchCommit.getID())) {
                System.out.println(Failure.SPLIT_SAME_BRANCH);
                System.exit(0);
            }
            if (splitCommit.getID().equals(currCommit.getID())) {
                Repository.checkoutCommit(branchCommit);
                System.out.println(Failure.SPLIT_SAME_HEAD);
                System.exit(0);
            }

            // Set for all files
            Set<String> files = new HashSet<>();
            Set<String> splitFiles = splitCommit.getTrackedFiles().keySet();
            Set<String> currFiles = currCommit.getTrackedFiles().keySet();
            Set<String> branchFiles = branchCommit.getTrackedFiles().keySet();

            files.addAll(splitFiles);
            files.addAll(currFiles);
            files.addAll(branchFiles);


            boolean isConflict = false;
            Stage stage = Repository.readStage();

            // Begin merge
            for (String file : files) {
                // Modified in the given branch but not in the current branch, checkout them and stage
                // Modified in the current branch but not in the given branch, do nothing
                // Modified in the same way or be deleted, do nothing
                // Only in the current branch, do nothing
                // Only in the given branch, checkout them and stage
                // Unmodified in the current branch but deleted in the given, do nothing
                // Unmodified in the given branch but deleted in the current, delete

                boolean inSplit = splitFiles.contains(file);
                boolean inCurr = currFiles.contains(file);
                boolean inGiven = branchFiles.contains(file);

                String splitID = inSplit ? splitCommit.getBlobID(file) : null;
                String currID = inCurr ? currCommit.getBlobID(file) : null;
                String givenID = inGiven ? branchCommit.getBlobID(file) : null;

                // === Rule 1 === Modified in given, unmodified in current
                if (inSplit && inCurr && inGiven &&
                        splitID.equals(currID) && !splitID.equals(givenID)) {
                    Repository.checkoutFile(file, branchCommit);
                    stage.addFile(file, Repository.getBlob(givenID));
                    continue;
                }

                // === Rule 2 === Modified in current, unmodified in given -> do nothing
                if (inSplit && inCurr && inGiven &&
                        splitID.equals(givenID) && !splitID.equals(currID)) {
                    continue;
                }

                // === Rule 3 === Modified in same way or both deleted -> do nothing
                if ((inSplit && inCurr && inGiven &&
                        !splitID.equals(currID) && currID.equals(givenID)) ||
                        (!inCurr && !inGiven && inSplit)) {
                    continue;
                }

                // === Rule 4 === Only in current branch -> do nothing
                if (!inSplit && inCurr && !inGiven) {
                    continue;
                }

                // === Rule 5 === Only in given branch -> checkout and stage
                if ((!inSplit && !inCurr && inGiven) || (!inSplit && inCurr && inGiven && currID.equals(givenID)) ) {
                    Repository.checkoutFile(file, branchCommit);
                    stage.addFile(file, Repository.getBlob(givenID));
                    continue;
                }

                // === Rule 6 === Present at split, unmodified in current, absent in given -> remove
                if (inSplit && inCurr && !inGiven && splitID.equals(currID)) {
                    Utils.restrictedDelete(file);
                    stage.removeFile(file);
                    continue;
                }

                // === Rule 7 === Present at split, unmodified in given, absent in current -> remain absent
                if (inSplit && !inCurr && inGiven && splitID.equals(givenID)) {
                    continue;
                }


                // === Rule 8 === Conflict
                isConflict = true;
                Repository.handleConflict(file, currID, givenID);
            }

            Repository.writeStage(stage);

            // Merge commit
            if (isConflict) {
                System.out.println(Failure.MERGE_CONFLICT);
                System.exit(0);
            } else {
                commit("Merged " + branchName + " into " + Repository.getCurrentBranch() + ".", branchCommit.getID(), true);
            }
        }
    }
}
