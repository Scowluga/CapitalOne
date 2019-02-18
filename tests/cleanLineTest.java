//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class cleanLineTest {
//
//    @Test
//    public void singleQuotes() throws Exception {
//        String s = "char c = \'c\'; //";
//        assertEquals(Main.cleanLine(s), "char c = ; //");
//    }
//
//    @Test
//    public void doubleQuotes() throws Exception {
//        String s = "String s = \"TODO\"; //";
//        assertEquals(Main.cleanLine(s), "String s = ; //");
//    }
//
//    @Test
//    public void multipleStrings() throws Exception {
//        String s = "System.out.println(\"A\" + \'B\' + \"C\");";
//        assertEquals(Main.cleanLine(s), "System.out.println( +  + );");
//    }
//
//    @Test
//    public void unchanged() throws Exception {
//        String s = "for (int i = 0; i < nums.length; i++) { //";
//        assertSame(Main.cleanLine(s), s);
//    }
//}