package com.gas.common.ui.util;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.misc.FilenameExtFilter;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

public class FileHelper {

    public static StringBuffer toStringBuffer(File file) {
        StringBuffer ret = new StringBuffer();

        FileInputStream stream;
        try {
            stream = new FileInputStream(file);
            ret = toStringBuffer(stream);
            try {
                stream.close();
            } catch (IOException e) {
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ret;
        }
        return ret;
    }

    public static StringList toStringList(InputStream stream) {
        StringList ret = new StringList();
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        try {
            while ((line = r.readLine()) != null) {
                ret.add(line);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public static String getExt(File file) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException(String.format("%s is a directory", file.getAbsolutePath()));
        }
        String path = file.getAbsolutePath();
        int index = path.lastIndexOf(".");
        if (index + 1 < path.length()) {
            return path.substring(index + 1);
        } else {
            return "";
        }
    }

    public static String getNewFileName(File dir, final String proposedName, final String pExt) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(String.format("File '%s' must be a directory", dir.getName()));
        }

        File[] files = dir.listFiles(new FilenameExtFilter(pExt));
        StringList namesExisting = new StringList();
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            String fileName = file.getName();
            final int indexPeriod = fileName.indexOf(".");
            String nameNoExt = fileName.substring(0, indexPeriod).toUpperCase(Locale.ENGLISH);
            namesExisting.add(nameNoExt);
        }
        return StrUtil.getNewName(proposedName, namesExisting);
    }

    public static byte[] toBytesQuitely(InputStream inputStream) {
        byte[] ret = null;
        try {
            ret = IOUtils.toByteArray(inputStream);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public static byte[] toBytes(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new IOException(String.format("File '%s'is too large", file.getName()));
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static boolean copy(File source, File dest){
        boolean ret = false;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            long bytes = outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            ret = bytes > 0;
        } catch (Exception e) {            
        } finally {
            closeQuitely(inputChannel);
            closeQuitely(outputChannel);
            return ret;
        }
    }
    
    private static void closeQuitely(FileChannel c){
        if(c != null){
            try{
                c.close();
            }catch(IOException e){}
        }
    }

    private static File getTempFolder() {
        String str = System.getProperty("java.io.tmpdir");
        return new File(str);
    }

    public static File getUniqueFile(String prefix, String suffix) {
        return getUniqueFile(prefix, suffix, true);
    }

    public static File getUniqueFile(boolean deleteOnExit) {
        return getUniqueFile("abc", null, deleteOnExit);
    }

    public static File getUniqueFile(String prefix, String suffix, boolean deleteOnExit) {
        if (prefix == null || prefix.length() < 3) {
            throw new IllegalArgumentException("Prefix must be at least 3 chars long");
        }
        File ret = null;
        try {
            ret = File.createTempFile(prefix, suffix);
            ret.setWritable(true);
            if (deleteOnExit) {
                ret.deleteOnExit();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    public static File getUniqueDir(String prefix) {
        File tempFolder = getTempFolder();

        File dir = new File(tempFolder, Long.toHexString(System.nanoTime()));
        boolean success = dir.mkdir();
        return dir;
    }

    public static boolean toFile(File file, byte[] bytes) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(bytes);
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean toFile(File file, InputStream inputStream) {
        StringBuffer buffer = toStringBuffer(inputStream);
        return toFile(file, buffer);
    }

    public static File toFile(InputStream inputStream, Charset charset) {
        StringBuffer buffer = toStringBuffer(inputStream);
        File file = FileHelper.getUniqueFile("abc", ".txt");
        toFile(file, buffer, charset);
        return file;
    }

    public static File toFile(InputStream inputStream) {
        return toFile(inputStream, Charset.forName("UTF-8"));
    }

    public static File toFile(String str) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        return toFile(inputStream);
    }

    public static boolean toFile(File file, CharSequence contents) {
        return toFile(file, contents, Charset.forName("UTF-8"));
    }

    public static boolean toFile(File file, CharSequence contents, Charset charset) {
        byte[] bytes = contents.toString().getBytes(charset);
        return toFile(file, bytes);
    }

    public static StringBuffer toStringBuffer(Class _class, String resource) {
        InputStream stream = _class.getResourceAsStream(resource);
        StringBuffer ret = toStringBuffer(stream);
        try {
            stream.close();
        } catch (IOException e) {
        }
        return ret;
    }

    public static StringBuffer toStringBuffer(InputStream stream) {
        StringBuffer ret = new StringBuffer();
        int data;
        try {
            //BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            //data = reader.read();
            data = stream.read();
            while (data > -1) {
                char dataChar = (char) data;
                ret.append(dataChar);
                data = stream.read();
            }
            //reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ret;
        }
        return ret;
    }

    public static List<String> unzip(File zipFile) {
        return unzip(zipFile, getTempFolder());
    }

    public static List<String> unzip(File zipFile, File outputDirectory) {
        List<String> ret = new ArrayList<String>();
        final int BUFFER = 2048;

        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(fis));
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[BUFFER];

                String fileName = outputDirectory.getAbsolutePath() + File.separatorChar + entry.getName();
                FileOutputStream fos = new FileOutputStream(fileName);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
                ret.add(fileName);
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void zip(File outputFile, File... inputFiles) {
        final int BUFFER = 2048;
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(outputFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            // out.setMethod(ZipOutputStream.DEFLATED);
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < inputFiles.length; i++) {
                FileInputStream fi = new FileInputStream(inputFiles[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(inputFiles[i].getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zip(File outputFile, List<File> inputFiles) {
        zip(outputFile, inputFiles.toArray(new File[inputFiles.size()]));
    }
}