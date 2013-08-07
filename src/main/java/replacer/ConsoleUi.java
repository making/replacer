package replacer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUi {

    public static void main(String[] args) throws IOException {
        File projectRoot = new File(args.length > 0 ? args[0] : ".");

        String projectNameFrom = null;
        String projectNameTo = null;
        String packageNameFrom = null;
        String packageNameTo = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("置換元のプロジェクト名を入力してください: ");
            projectNameFrom = reader.readLine();
            System.out.print("置換先のプロジェクト名を入力してください: ");
            projectNameTo = reader.readLine();
            System.out.print("置換元のパッケージ名を入力してください: ");
            packageNameFrom = reader.readLine();
            System.out.print("置換先のパッケージ名を入力してください: ");
            packageNameTo = reader.readLine();

            System.out.println("対象フォルダ:\t" + projectRoot.getAbsolutePath());
            System.out.printf("プロジェクト名:\t%s -> %s%n", projectNameFrom,
                    projectNameTo);
            System.out.printf("パッケージ名:\t%s -> %s%n", packageNameFrom,
                    packageNameTo);
            System.out.print("以上の内容で置換してよいでしょうか？(Y/N): ");

            String yOrN = reader.readLine();
            if ("Y".equalsIgnoreCase(yOrN)) {
                System.out.println("置換します。");
            } else {
                System.out.println("中止します。");
                System.exit(0);
            }
        }
        ProjectName projectName = new ProjectName(projectNameFrom, projectNameTo);
        PackageName packageName = new PackageName(packageNameFrom, packageNameTo);

        Replacer replacer = new Replacer(projectName, packageName);

        try {
            replacer.scanRecursively(projectRoot);
        } catch (IOException e) {
            System.out.println("Stopped!");
            e.printStackTrace();
        }

    }

}
