package byow.Core;

import byow.Core.map.World;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


/** Utils methods */

public class Utils {
    /** The current working directory */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The world saving */
    public static final File GAME = join(CWD, "game");

    /* OTHER FILE UTILITIES */

    /** Return the concatentation of FIRST and OTHERS into a File designator,
     *  analogous to the {@link java.nio.file.Paths.#get(String, String[])}
     *  method. */
    static File join(String first, String... others) {
        return Paths.get(first, others).toFile();
    }

    /** Return the concatentation of FIRST and OTHERS into a File designator,
     *  analogous to the {@link java.nio.file.Paths.#get(String, String[])}
     *  method. */
    static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }

    /**
     * Write the result of concatenating the bytes in CONTENTS to FILE,
     * creating or overwriting it as needed.  Each object in CONTENTS may be
     * either a String or a byte array.  Throws IllegalArgumentException
     * in case of problems.
     */
    static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /**
     * Return an object of type T read from FILE, casting it to EXPECTEDCLASS.
     * Throws IllegalArgumentException in case of problems.
     */
    static <T extends Serializable> T readObject(File file, Class<T> expectedClass) {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(file));
            T result = expectedClass.cast(in.readObject());
            in.close();
            return result;
        } catch (IOException | ClassCastException
                 | ClassNotFoundException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /* SERIALIZATION UTILITIES */

    /**
     * Returns a byte array containing the serialized contents of OBJ.
     */
    static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(obj);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException excp) {
            throw new RuntimeException("Internal error serializing");
        }
    }

    /**
     * Write OBJ to FILE.
     */
    static void writeObject(File file, Serializable obj) {
        writeContents(file, serialize(obj));
    }

    /* ENGINE UTILS */


    /** Save the World instance to the file */
    static void saveGame(GameState gs) {
        writeObject(GAME, gs);
    }

    /** Write the World instance from file */
    static GameState loadGame() {
        return readObject(GAME, GameState.class);
    }

    /** Split the input string to some useful information */
    static String splitInput(String input) {
        StringBuilder res = new StringBuilder();
        boolean seedFlag = false;
        boolean loadFlag = false;
        boolean worldFlag = false;

        // Iterate the input string
        for (int i = 0; i < input.length(); i++) {
            String c = String.valueOf(input.charAt(i));


            if (seedFlag && (c.equals("s") || c.equals("S"))) {
                seedFlag = false;
                res.append(c.toLowerCase());
                continue;
            }

            if (seedFlag) {
                res.append(c.toLowerCase());
            }

            if (!loadFlag && (c.equals("l") || c.equals("L"))) {
                loadFlag = true;
                worldFlag = true;
                res.append(c.toLowerCase());
            }

            if (!loadFlag && (c.equals("n") || c.equals("N"))) {
                seedFlag = true;
                worldFlag = true;
            }

            if (c.equals(":") && (input.charAt(i + 1) == 'q' || input.charAt(i + 1) == 'Q')) {
                res.append(c.toLowerCase());
                break;
            }

            if (worldFlag && inMove(c)) {
                res.append(c.toLowerCase());
            }

        }

        return res.toString();
    }

    /** splitInput helper method */
    private static boolean inMove(String c) {
        String map = "WwAaSsDd";
        return map.contains(c);
    }


}
