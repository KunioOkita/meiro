package jp.co.heartsoft.meiro.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
    private CommonUtil() {
        // コンストラクタ
    }

    /**
     * 迷路ファイル読み込み
     * @param file
     * @return
     * @throws IOException
     */
    public static char[][] loadMeriroFile(File file) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }

        char[][] meiro = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            meiro[i] = lines.get(i).toCharArray();
        }

        return meiro;
    }

    /**
     * 結果書き込み
     * @param outFile
     * @param result
     * @throws IOException 
     */
    public static void writeResult(File outFile, char[][] result) throws IOException {
        FileWriter writer = new FileWriter(outFile);
        for (int i = 0; i < result.length; i++) {
            writer.write(new String(result[i]) + '\n');
        }
        writer.close();
    }

    /**
     * 拡張子を除いた basename
     * @param file
     * @return
     */
    public static String getFileBaseName(File file) {
        String basename = file.getName();
        String woext = basename.substring(0, basename.lastIndexOf('.'));
        return woext;
    }

}
