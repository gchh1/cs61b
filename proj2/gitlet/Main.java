package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author yhc
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // Break when args are empty
        if (args.length == 0) {
            System.out.println(Failure.NO_INPUT_FAILURE);
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Command.init();
                break;
            case "add":
                Failure.checkInit();
                Failure.checkOPNum(args, 2);
                Command.add(args[1]);
                break;
            case "commit":
                Failure.checkInit();
                Failure.checkOPNum(args, 2);
                Command.commit(args[1], null, false);
                break;
            case "rm":
                Failure.checkInit();
                Failure.checkOPNum(args, 2);
                Command.rm(args[1]);
                break;
            case "log":
                Failure.checkInit();
                Failure.checkOPNum(args, 1);
                Command.log();
                break;
            case "status":
                Failure.checkInit();
                Failure.checkOPNum(args, 1);
                Command.status();
                break;
            case "global-log":
                Failure.checkInit();
                Failure.checkOPNum(args, 1);
                Command.globalLog();
                break;
            case "find":
                Failure.checkInit();
                Failure.checkOPNum(args, 2);
                Command.find(args[1]);
                break;
            case "checkout":
                Failure.checkInit();
                Command.checkout(args);
                break;
            case "branch":
                Failure.checkInit();
                Failure.checkOPNum(args, 2);
                Command.branch(args[1]);
                break;
            case "rm-branch":
                Failure.checkInit();
                Failure.checkOPNum(args, 2);
                Command.rmBranch(args[1]);
                break;
            case "reset":
                Failure.checkInit();
                Failure.checkOPNum(args, 2);
                Command.reset(args[1]);
                break;
            case "merge":
                Failure.checkInit();
                Failure.checkOPNum(args, 2);
                Command.merge(args[1]);
                break;

            default:
                System.out.println(Failure.INPUT_NOT_EXIST_FAILURE);
                System.exit(0);
        }
    }
}
