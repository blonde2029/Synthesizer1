import javax.sound.midi.*;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static final byte A = 69;
    private static final byte B = 70;
    private static final byte C = 60;
    private static final byte D = 62;
    private static final byte E = 64;
    private static final byte F = 65;
    private static final byte G = 67;

    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        MidiChannel[] midiChannels = synthesizer.getChannels();
        midiChannels[0].programChange(41);
        Receiver receiver = synthesizer.getReceiver();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, world!");
        System.out.println("Would you like to play from file? y/n");
        String answer = scanner.nextLine().trim().toLowerCase();
        if (answer.equals("y")) {
            //String filePath = "C:\\Games\\dotes.txt";
            System.out.println("Enter path to file with notes: ");
            String filePath = scanner.nextLine().trim();
            String content = "";
            try {
                content = Files.lines(Paths.get(filePath)).collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(content);
            String[] notes = content.toUpperCase().split(" ");
            playNotes(receiver, notes);
        } else if (answer.equals("n")) {
            System.out.println("Would you like to play manually? y/n");
            answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("y")) {
                System.out.println("Please enter notes (or \"exit\" to quit):");
                String text = scanner.nextLine().trim().toUpperCase();
                while (!text.equalsIgnoreCase("exit")) {
                    String[] notes = text.toUpperCase().split(" ");
                    //System.out.println(notes[0]);
                    playNotes(receiver, notes);
                    text = scanner.nextLine().trim().toUpperCase();
                }
            }
            System.out.println("Goodbye!");
            return;
        }
        System.out.println("Goodbye!");
        synthesizer.close();
        scanner.close();
        //return;
    }

    private static void playNotes(Receiver receiver, String[] notes) throws InvalidMidiDataException, InterruptedException {
        for (String note : notes) {
            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.NOTE_ON, convertToId(note), 100);
            receiver.send(msg, -1);
            Thread.sleep(500);
            msg.setMessage(ShortMessage.NOTE_OFF, convertToId(note), 100);
            receiver.send(msg, -1);
        }
    }

    private static int convertToId(String text) {
        switch (text) {
            case "A":
                return A;
            case "B":
                return B;
            case "C":
                return C;
            case "D":
                return D;
            case "E":
                return E;
            case "F":
                return F;
            case "G":
                return G;
            default:
                System.out.println("Unknown note: " + text);
                return C;

        }
    }
}