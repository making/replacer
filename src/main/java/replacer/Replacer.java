package replacer;

import java.io.File;
import java.io.IOException;

public class Replacer {
    private ProjectName projectName;

    private PackageName packageName;

    public Replacer(ProjectName projectName, PackageName packageName) {
        this.projectName = projectName;
        this.packageName = packageName;
    }

    public void scanRecursively(File file) throws IOException {
        File replaced = attemptToReplace(file);
        if (replaced.isDirectory()) {
            for (File child : replaced.listFiles()) {
                scanRecursively(child);
            }
        }
    }

    private File attemptToReplace(File file) throws IOException {
        File replaced = file;
        replaced = projectName.attemptToReplcae(replaced);
        replaced = packageName.attemptToReplcae(replaced);
        return replaced;
    }

}
