package net.codjo.util.string;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void test_removeAllCharOccurrence() {
        assertEquals(StringUtil.removeAllCharOccurrence(" a la con  ", ' '), "alacon");
        assertEquals(StringUtil.removeAllCharOccurrence("20/12/2001", '/'), "20122001");
    }


    @Test
    public void test_sqlToJavaName() throws Exception {
        assertEquals("columnName", StringUtil.sqlToJavaName("COLUMN_NAME"));
        assertEquals("columnNameA", StringUtil.sqlToJavaName("COLUMN_NAME_A"));
        assertEquals("columnname", StringUtil.sqlToJavaName("COLUMNNAME"));
        assertEquals("1ColumnName", StringUtil.sqlToJavaName("1_COLUMN_NAME"));
    }


    @Test
    public void test_javaToSqlName() throws Exception {
        assertEquals("PIMS", StringUtil.javaToSqlName("pims"));
        assertEquals("PIMS_CODE", StringUtil.javaToSqlName("pimsCode"));
        assertEquals("EXT_VAL_VL", StringUtil.javaToSqlName("extValVl"));
    }


    @Test
    public void test_javaToSqlName_tooLong() throws Exception {
        assertEquals("PIMS_CODE0123456789012345678", StringUtil.javaToSqlName("pimsCode0123456789012345678"));

        try {
            StringUtil.javaToSqlName("pimsCode01234567890123456780");
            Assert.fail("Exception attendue");
        }
        catch (Exception ex) {
            assertEquals("La colonne pour le champ pimsCode01234567890123456780 dépasse 28 caractères !",
                         ex.getMessage());
        }
    }


    @Test
    public void test_javaToSqlName_allreadyInSQLSynntax() throws Exception {
        assertEquals("ID_CHARACTERISTIC", StringUtil.javaToSqlName("ID_CHARACTERISTIC"));
        assertEquals("ID_1_CHARACTERISTIC", StringUtil.javaToSqlName("ID_1_CHARACTERISTIC"));
    }


    @Test
    public void test_tomultipleKeySqlName() throws Exception {
        assertEquals("PIMS,RED", StringUtil.javaToSqlName("pims,red"));
        assertEquals("PIMS_CODE", StringUtil.javaToSqlName("pimsCode"));
        assertEquals("EXT_VAL_VL,EXT_VAL_ERIE", StringUtil.javaToSqlName("extValVl,extValErie"));
    }


    @Test
    public void test_isNotNull() {
        assertFalse(StringUtil.isNotNull(null));
        assertFalse(StringUtil.isNotNull("null"));
        assertTrue(StringUtil.isNotNull("Une chaine pas nulle"));
    }


    @Test
    public void test_isNull() {
        assertTrue(StringUtil.isNull(null));
        assertTrue(StringUtil.isNull("null"));
        assertFalse(StringUtil.isNull("Une chaine pas nulle"));
    }


    @Test
    public void test_isNullOrEmpty() {
        assertTrue(StringUtil.isNullOrEmpty(null));
        assertTrue(StringUtil.isNullOrEmpty(""));
        assertFalse(StringUtil.isNullOrEmpty("null"));
        assertFalse(StringUtil.isNullOrEmpty("Une chaine pas nulle"));
    }


    @Test
    public void test_isEmpty() {
        assertTrue(StringUtil.isEmpty(""));
        assertTrue(StringUtil.isEmpty("null"));
        assertFalse(StringUtil.isEmpty("Une chaine pas nulle"));
    }


    @Test
    public void test_findAll() {
        List<String> allOccurences = StringUtil.findAll("name=(.*?) -",
                                                        "[<name=branchCode - value=05R_C_01011990_EUR>, <name=beginDate - value=1998-01-01>]");
        Assert.assertEquals(2, allOccurences.size());
        Assert.assertEquals("branchCode", allOccurences.get(0));
        Assert.assertEquals("beginDate", allOccurences.get(1));
    }
}
