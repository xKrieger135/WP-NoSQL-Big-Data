package com.haw.hamburg.util.filereaderadapter;

import com.haw.hamburg.util.filereaderadapter.interfaces.IFileReaderAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;


/**
 * Created by Krieger135 on 4/23/16.
 */
public class FileReaderAdapter implements IFileReaderAdapter {

    private List<String> fileInput;

    public FileReaderAdapter() {
        fileInput = new ArrayList<>();
    }

    public List<String> readFile(String file) throws IOException {
        Stream<String> fileInputStream = Files.lines(Paths.get(file));
        fileInputStream.forEach(elem -> fileInput.add(elem));
        return fileInput;
    }
}
