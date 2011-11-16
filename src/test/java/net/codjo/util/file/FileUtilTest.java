/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.util.file;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class FileUtilTest {
    private File tempDir = new File(
          System.getProperty("java.io.tmpdir") + File.separator + "FileUtilTest" + File.separator);
    private static final String UTF16FILE_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\r\n"
                                                    + "<todo>\r\n"
                                                    + "    fêter la victoire de Paris.\r\n"
                                                    + "</todo>";


    @Test
    public void test_moveFileTo_withVersion() throws IOException {
        File dirDest = new File(tempDir, "test");
        File source = new File(tempDir, "toto.txt");
        source.delete();

        dirDest.mkdir();
        source.createNewFile();
        assertTrue("Creation rep", dirDest.exists());
        assertTrue("Creation file", source.exists());

        try {
            File sameFile;
            File versionFile;
            new File(dirDest, "toto.txt").delete();
            sameFile = FileUtil.moveFileTo(dirDest.getAbsolutePath(), source);
            assertEquals("Deplacement...", "toto.txt", sameFile.getName());

            source.createNewFile();
            versionFile = FileUtil.moveFileTo(dirDest.getAbsolutePath(), source);
            assertEquals("Deplacement...", "toto001.txt", versionFile.getName());

            versionFile.delete();
            sameFile.delete();
            dirDest.delete();
        }
        catch (IOException e) {
            fail("ça ne doit pas planter !");
        }
    }


    @Test
    public void test_moveFileTo_withVersion_withoutExtension() throws IOException {
        File dirDest = new File(tempDir, "test");
        File source = new File(tempDir, "toto");

        dirDest.mkdir();
        source.createNewFile();
        assertTrue("Creation rep", dirDest.exists());
        assertTrue("Creation file", source.exists());

        try {
            File sameFile;
            File versionFile;
            sameFile = FileUtil.moveFileTo(dirDest.getAbsolutePath(), source);
            assertTrue("Deplacement...", "toto".equals(sameFile.getName()));

            source.createNewFile();
            versionFile = FileUtil.moveFileTo(dirDest.getAbsolutePath(), source);
            assertTrue("Deplacement...", "toto001".equals(versionFile.getName()));

            versionFile.delete();
            sameFile.delete();
            dirDest.delete();
        }
        catch (IOException e) {
            fail("ça ne doit pas planter !");
        }
    }


    @Test
    public void test_moveFileTo_withoutVersion() throws Exception {
        String dirDest = System.getProperty("user.dir");

        File sourceFile = File.createTempFile("ORBIS_TU_", null);
        sourceFile.deleteOnExit();
        assertEquals("Fichier source existe", sourceFile.exists(), true);
        String sourceFilePath = sourceFile.getAbsolutePath();

        File dest = FileUtil.moveFileTo(dirDest, sourceFile, false);
        dest.deleteOnExit();
        assertEquals("Fichier source n'existe plus", sourceFile.exists(), false);
        assertEquals("Fichier dest existe", dest.exists(), true);

        long first = dest.lastModified();

        Thread.sleep(100);

        File newSource = new File(sourceFilePath);
        newSource.deleteOnExit();
        assertTrue("Creation d'un autre source (avec le meme nom)",
                   newSource.createNewFile());

        dest = FileUtil.moveFileTo(dirDest, newSource, false);
        dest.deleteOnExit();

        long second = dest.lastModified();

        assertTrue("Le nouveau a ecrasé l'ancien", first < second);
    }


    @Test
    public void test_moveFileTo_dirNotFound() throws IOException {
        File sourceFile = new File(tempDir + "toto.txt");

        try {
            FileUtil.moveFileTo(tempDir + "test", sourceFile);
            fail("ça doit planter ! le rep n'existe pas.");
        }
        catch (IOException e) {
            assertEquals("Le repertoire destination : " + tempDir + "test n'existe pas.", e.getMessage());
        }
    }


    @Test
    public void test_moveFileTo_fileNotFound() throws IOException {
        File destFile = new File(tempDir + "test");
        File sourceFile = new File(tempDir + "toto.txt");

        destFile.mkdir();
        assertTrue("Creation nouveau rep", destFile.exists());

        try {
            FileUtil.moveFileTo(tempDir + "test", sourceFile);
            fail("ça doit planter ! le sourceFile n'existe pas.");
        }
        catch (IOException e) {
            assertEquals("Le fichier : " + tempDir + "toto.txt n'existe pas.", e.getMessage());
        }
        destFile.delete();
    }


    @Test
    public void test_getExtensionIndex() throws IOException {
        File fa = new File(".\\test");
        assertEquals(-1, FileUtil.getExtensionIndex(fa));

        File fb = new File(tempDir + "test.txt");
        int idx = FileUtil.getExtensionIndex(fb);
        assertEquals(tempDir.getAbsolutePath().length() + 4, idx);
        assertEquals(".txt", fb.toString().substring(idx));
    }


    @Test
    public void test_loadContent() throws Exception {
        String expected = "sdkjfhsjkfd\nfvgdtgfrg";

        assertEquals(expected, FileUtil.loadContent(new StringReader(expected)));
    }


    @Test
    public void test_loadContent_fromFile() throws Exception {
        String expected = "file content";

        assertEquals(expected, FileUtil.loadContent(
              new File(getClass().getResource("FileUtilTest.properties").getFile())));
    }


    @Test
    public void test_loadContent_fromUrl() throws Exception {
        String expected = "file content";

        assertEquals(expected, FileUtil.loadContent(getClass().getResource("FileUtilTest.properties")));
    }


    @Test
    public void test_loadContent_fromUTF16() throws Exception {
        assertEquals(UTF16FILE_CONTENT,
                     FileUtil.loadContent(getClass().getResource("UTF16.xml"), "UTF16"));
    }


    @Test
    public void test_saveContent() throws Exception {
        String content = "file content";
        File file = new File(tempDir, "myFile");

        FileUtil.saveContent(file, content);

        assertEquals(content, FileUtil.loadContent(file));
    }


    @Test
    public void test_saveContent_withUTF16() throws Exception {
        File file = new File(tempDir, "myFile");

        FileUtil.saveContent(file, UTF16FILE_CONTENT, "UTF16");
        File expected = new File(getClass().getResource("UTF16.xml").getFile());
        BufferedReader actualReader = new BufferedReader(new FileReader(file));
        BufferedReader expectedReader = new BufferedReader(new FileReader(expected));

        StringBuilder actualContent = new StringBuilder();
        StringBuilder expectedContent = new StringBuilder();
        do {
            actualContent.append(actualReader.readLine());
            expectedContent.append(expectedReader.readLine());
        }
        while (actualReader.readLine() != null && expectedReader.readLine() != null);

        actualReader.close();
        expectedReader.close();

        assertEquals(expectedContent.toString(), actualContent.toString());
    }


    @Test
    public void test_deleteRecursively() throws Exception {
        File file = new File(tempDir, "a");
        file.mkdir();

        new File(file, "a.b").mkdir();

        new File(file, "a.file.txt").createNewFile();

        File dir = new File(file, "a.a");
        dir.mkdir();

        File dir2 = new File(dir, "a.a.a");
        dir2.mkdir();

        new File(dir2, "a.a.a.file.sh").createNewFile();

        assertTrue(file.list().length > 0);

        FileUtil.deleteRecursively(file);

        assertFalse(file.exists());
    }


    @Before
    public void setUp() throws Exception {
        tempDir.mkdir();
        assertTrue("Temp dir existe", tempDir.exists());
    }


    @After
    public void tearDown() {
        if (tempDir.exists()) {
            for (File file : tempDir.listFiles()) {
                assertTrue(file.delete());
            }
        }
        assertTrue(tempDir.delete());
    }
}
