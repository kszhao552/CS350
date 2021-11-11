import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class Dispatcher {
    public static void main(String[] args){
        String fileName = args[0];
        File file = new File(fileName);
        UnHash crack = new UnHash();


        try (Stream<String> linesStream = Files.lines(file.toPath())) {
            linesStream.forEach(line -> {
                System.out.println(crack.unhash(line));
            });
        }
        catch (Exception ignore){

        }
    }
}
