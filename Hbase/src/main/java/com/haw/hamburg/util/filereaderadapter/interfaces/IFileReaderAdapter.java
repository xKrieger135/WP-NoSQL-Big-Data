package com.haw.hamburg.util.filereaderadapter.interfaces;

import java.io.IOException;
import java.util.List;

/**
 * Created by nosql on 4/23/16.
 */
public interface IFileReaderAdapter {

    /**
     * This method will read a file and then the file will be stored into a list of strings.
     * @param file            This is the file, which should be read.
     * @return                Returns a list of strings, which represent the file as string line by line.
     * @throws IOException    This Exception will be thrown, when something went wrong while reading the file into the
     *                        list of strings.
     */
    List<String> readFile(String file) throws IOException;

}
