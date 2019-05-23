package com.shykad.yunke.sdk.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * created by wuzejian on 2018/11/19
 */
public class CopyWhiteName {

    private FileReader fr;
    private BufferedReader br;
    private FileWriter fw;
    private BufferedWriter bw;

    /**
     * 输出下载SDK资源白名单到whiteName.txt
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        CopyWhiteName copyWhiteName = new CopyWhiteName();
        copyWhiteName.readAndWriteWhiteNameToFile();
    }

    public void readAndWriteWhiteNameToFile() {
        String flag = "\"";
        try {
            File rootDir = new File("");
            String rootPath = rootDir.getAbsolutePath();
            System.out.println("rootPath：" + rootPath);
            fr = new FileReader(rootPath + "/openadsdk/build/intermediates/bundles/release/R.txt");
            br = new BufferedReader(fr);
            File file = new File(rootPath + "/pack_shell/whiteName.txt");
            System.out.println("whiteName path :" + file.getAbsolutePath());
            file.delete();
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);

            String line = "";
            String[] arrs = null;
            while ((line = br.readLine()) != null) {
                arrs = line.split(" ");
                String res = flag;
                if (!arrs[2].trim().startsWith("tt_")) {
                    System.out.println(arrs[1] + " : " + arrs[2]);
                    if (arrs[1] != null && "string".equals(arrs[1])) {
                        res = res + "R.string." + arrs[2] + flag;
                    } else if (arrs[1] != null && "drawable".equals(arrs[1])) {
                        res = res + "R.drawable." + arrs[2] + flag;
                    } else if (arrs[1] != null && "color".equals(arrs[1])) {
                        res = res + "R.color." + arrs[2] + flag;
                    } else if (arrs[1] != null && "id".equals(arrs[1])) {
                        res = res + "R.id." + arrs[2] + flag;
                    } else if (arrs[1] != null && "layout".equals(arrs[1])) {
                        res = res + "R.layout." + arrs[2] + flag;
                    } else if (arrs[1] != null && "style".equals(arrs[1])) {
                        res = res + "R.style." + arrs[2] + flag;
                    } else if (arrs[1] != null && "dimen".equals(arrs[1])) {
                        res = res + "R.dimen." + arrs[2] + flag;
                    } else if (arrs[1] != null && "integer".equals(arrs[1])) {
                        res = res + "R.integer." + arrs[2] + flag;
                    } else if (arrs[1] != null && "attr".equals(arrs[1])) {
                        res = res + "R.attr." + arrs[2] + flag;
                    } else if (arrs[1] != null && "anim".equals(arrs[1])) {
                        res = res + "R.anim." + arrs[2] + flag;
                    }
                    if (!flag.equals(res)) {
                        bw.write(res + "\t\n");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
