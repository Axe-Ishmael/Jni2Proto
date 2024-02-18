package CodeParserUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 找到 message ABC 对应的文件的路径
 */
public class FilePathFindUtil {

    public static List<String> findImportPathOfMessage(String searchDirectoryPath, String messageName) {
        String shellScript = "#!/bin/bash\n" +
                "\n" +
                "# 最顶层文件夹路径\n" +
                "top_level_folder=\"" + searchDirectoryPath + "\"\n" +
//                "top_level_folder=\"" + "/Users/axeishmael/StudioProjects/api_proto/src/mobile_framework" + "\"\n" +
                "# 要搜索的消息名称\n" +
                "message_name=\"" + messageName + "\"\n" +
//                "message_name=\"" + "OprCorpShowHideReq" + "\"\n" +
                "\n" +
                "# 要搜索的特定内容，这里使用正则表达式进行精确匹配\n" +
                "# ^ 表示行的开始，[[:space:]]+ 表示一个或多个空白字符\n" +
                "search_content=\"^message[[:space:]]+\\b${message_name}\\b\"\n" +
                "\n" +
                "# 使用find命令遍历文件夹下的所有.proto文件\n" +
                "find \"$top_level_folder\" -type f -name \"*.proto\" | while read -r file; do\n" +
                "    # 使用grep检查文件内容\n" +
                "    if grep -q -E \"$search_content\" \"$file\"; then\n" +
                "        # 输出文件的路径\n" +
                "        echo \"$file\"\n" +
                "    fi\n" +
                "done";

        // 创建临时文件来保存脚本
        File tempScript = null;
        List<String> ret = new ArrayList<>();

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null){
                ret.add(line);
            }else {
                ret.add("");
            }


            // 等待脚本执行完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                ret.add("Script executed with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            ret.add("An error occurred: " + e.getMessage());
        } finally {
            // 清理临时文件
            if (tempScript != null && !tempScript.delete()) {
                ret.add("Failed to delete the temporary script file.");
            }
        }

        return ret;
    }
}
