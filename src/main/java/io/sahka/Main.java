package io.sahka;

import io.sahka.files.FileHandler;
import io.sahka.files.LocalFileHandler;
import java.nio.file.Paths;

public class Main
{
    public static void main(String [] args)
    {
        // Create zip file
        FileHandler base = new LocalFileHandler(Paths.get(args[0]));
        FileHandler zipFile = base.child("utresult.zip");
        zipFile.delete();
        zipFile.zip(base.child("unittest_output"), "**/*", "*");
    }
}
