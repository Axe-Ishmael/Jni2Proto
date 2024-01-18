package CodeParserUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilePathFindUtil {

    public static List<String> findImportPathOfMessage(String searchDirectoryPath,String messageName){
        String shellScript = "" +
                "#!/bin/bash\n" +
                "\n" +
                "# 最顶层文件夹路径\n" +
                "top_level_folder=\""+searchDirectoryPath+"\"\n" +
                "# 要搜索的特定内容\n" +
                "search_content=\""+"message "+messageName+"\"\n" +
                "\n" +
                "# 使用find命令遍历文件夹下的所有.proto文件\n" +
                "find \"$top_level_folder\" -type f -name \"*.proto\" | while read -r file; do\n" +
                "    # 使用grep检查文件内容\n" +
                "    if grep -q \"$search_content\" \"$file\"; then\n" +
                "        # 输出文件的路径\n" +
                "        echo \"$file\"\n" +
                "    fi\n" +
                "done";


        // 创建临时文件来保存脚本
        File tempScript = null;

        StringBuilder output = new StringBuilder();

        StringBuilder err = new StringBuilder();
        try {
            tempScript = File.createTempFile("script", ".sh");

            // 写入脚本内容
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempScript))) {
                writer.write(shellScript);
            }

            // 设置执行权限
            boolean executable = tempScript.setExecutable(true);
            if (!executable) {
                throw new IOException("Failed to set the script executable.");
            }

            // 使用ProcessBuilder执行脚本
            ProcessBuilder pb = new ProcessBuilder(tempScript.getAbsolutePath());
            Process process = pb.start();

            // 读取标准输出
            InputStream stdout = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String outLine;
            while ((outLine = reader.readLine()) != null) {
                output.append(outLine).append(System.lineSeparator());
                System.out.println("Stdout: " + output);
            }

            // 读取标准错误
            InputStream stderr = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(stderr));
            String errline;
            while ((errline = errorReader.readLine()) != null) {
                err.append(errline).append(System.lineSeparator());
                System.err.println("Stderr: " + errline);
            }

            // 等待脚本执行完成
            int exitCode = process.waitFor();
            System.out.println("Script executed with exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 清理临时文件
            if (tempScript != null) {
                tempScript.delete();
            }
        }

        List<String> ret = new ArrayList<>();

        ret.add(output.toString().trim());
        ret.add(String.valueOf(err));



        return ret;
    }
}
