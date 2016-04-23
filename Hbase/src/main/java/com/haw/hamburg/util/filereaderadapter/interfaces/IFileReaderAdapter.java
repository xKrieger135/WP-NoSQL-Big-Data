package com.haw.hamburg.util.filereaderadapter.interfaces;

import java.io.IOException;
import java.util.List;

/**
 * Created by nosql on 4/23/16.
 */
public interface IFileReaderAdapter {

    List<String> readFile(String file) throws IOException;

}
