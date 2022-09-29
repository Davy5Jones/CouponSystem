import java.util.Arrays;

public class Program {
    public static void main(String[] args) {/*
        try {
            Test.testAll();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/
        set(new String[]{"id", "name", "age"}, new String[]{"5", "ido", "4"});

    }

    public static void set(String[] columns, String[] values) {
        StringBuilder builder = new StringBuilder();
        Arrays.setAll(columns, i -> columns[i].concat("=" + values[i]));
        Arrays.stream(columns).forEach(System.out::println);

    }
}
