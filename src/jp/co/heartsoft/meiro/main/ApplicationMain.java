package jp.co.heartsoft.meiro.main;

import java.io.File;
import java.io.IOException;

import jp.co.heartsoft.meiro.algorithm.Algorithm4;
import jp.co.heartsoft.meiro.util.CommonUtil;

public class ApplicationMain {
    public static final String MEIRO_DIR = "./test_data";
    public static final String MEIRO_OUT = "./out";

    public static void main(String[] args) throws IOException {
        File inDir = new File(MEIRO_DIR);
        String[] inputFiles = inDir.list();
        System.out.println(String.join(",", inputFiles));

        for (int i = 0; i < inputFiles.length; i++) {
            File inputFile = new File(String.format("%s/%s", MEIRO_DIR, inputFiles[i]));
            if (inputFile.isFile()) {
                char[][] meiroData = CommonUtil.loadMeriroFile(inputFile);
                Algorithm4 al4 = new Algorithm4(meiroData);
                char[][] meiroResult = al4.execute();
                File outFile = new File(
                        String.format("%s/%s_result.txt", MEIRO_OUT, CommonUtil.getFileBaseName(inputFile)));
                CommonUtil.writeResult(outFile, meiroResult);
            }
        }
    }

}
