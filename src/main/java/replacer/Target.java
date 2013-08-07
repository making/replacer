package replacer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

abstract public class Target {
    private final String from;

    private final String to;

    public Target(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    protected boolean isExcluded(File file) {
        if (file.isFile() && file.getName().endsWith(".jar")) {
            return true;
        }
        return false;
    }

    protected boolean nameMatches(File file) {
        return file.getName().contains(from);
    }

    protected File replaceName(File file) throws IOException {
        File dest = new File(file.getParentFile(), file.getName().replace(from,
                to));
        try {
            Files.move(file.toPath(), dest.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(this + " error! -> " + file);
            throw e;
        }
        return dest;
    }

    protected boolean contentsMatches(File file) throws IOException {
        if (file.isDirectory()) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(file.toPath(),
                StandardCharsets.UTF_8)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(from)) {
                    return true;
                }
            }
        } catch (MalformedInputException e) {
           // System.err.println(this + " skipped to check " + file);
        } catch (IOException e) {
            System.err.println(this + " error! -> " + file);
            throw e;
        }
        return false;
    }

    protected void replaceContents(File file) throws IOException {
        // TODO more effective way
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file
                .toPath(), StandardCharsets.UTF_8, StandardOpenOption.WRITE))) {
            List<String> lines = Files.readAllLines(file.toPath(),
                    StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.contains(from)) {
                    writer.println(line.replace(from, to));
                } else {
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            System.err.println(this + " error ! -> " + file);
            throw e;
        }
    }

    public File attemptToReplcae(File file) throws IOException {
        if (from == null || from.isEmpty() || to == null || to.isEmpty()
                || isExcluded(file)) {
            return file;
        }

        if (contentsMatches(file)) {
            System.out.println(this + " contents match!\t" + file);
            replaceContents(file);
            System.out.println("\tcontents were replaced!\t->\t" + file);
        }

        if (nameMatches(file)) {
            System.out.println(this + " name match!\t" + file);
            File dest = replaceName(file);
            System.out.println("\trenamed! " + file + "\t->\t" + dest);
            return dest;
        }

        return file;
    }
}
