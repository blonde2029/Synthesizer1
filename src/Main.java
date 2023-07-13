import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException, IOException {
        // Подключаем всё необходимое
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        MidiChannel[] midiChannels = synthesizer.getChannels();
        midiChannels[0].programChange(41);
        Receiver receiver = synthesizer.getReceiver();
        Scanner scanner = new Scanner(System.in);

        // Выбираем способ проигрывания MIDI звуков
        System.out.println("How would you like to play MIDI sound? (Type \"exit\" to quit) \n1.From txt file \n2.Manually \n3.From MIDI file");
        String answer = scanner.nextLine().trim().toLowerCase();
        switch (answer) {
            case "1":
                System.out.println("Would you like to play from file? y/n");
                answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("y")) {
                    //String filePath = "C:\\Games\\dotes.txt";
                    playFromFile(scanner, receiver);
                }
                System.out.println("Goodbye!");
                return;
            case "2":
                System.out.println("Would you like to play manually? y/n");
                answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("y")) {
                    playManually(scanner, receiver);
                }
                System.out.println("Goodbye!");
                return;
            case "3":
                System.out.println("Would you like to play from MIDI file? y/n");
                answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("y")) {
                    playFromMIDI(scanner);
                }
                System.out.println("Goodbye!!");
                return;
            case "exit":
                System.out.println("Goodbye!");
                return;
            default:
                System.out.println("Try again ;)");
        }
        synthesizer.close();
        scanner.close();
    }

    private static void playFromMIDI(Scanner scanner) throws InvalidMidiDataException, IOException, MidiUnavailableException {
        System.out.println("Enter path to MIDI file: ");
        String filePath = scanner.nextLine().trim();
        Sequence sequence = MidiSystem.getSequence(new File(filePath));
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.start();
        while (sequencer.isRunning()) {
            System.out.println("If you wish to stop enter \"stop\"");
            String answer = scanner.nextLine().trim();
            if (answer.equals("stop")) {
                sequencer.stop();
            }
        }
    }


    private static void playFromFile(Scanner scanner, Receiver receiver) throws InvalidMidiDataException, InterruptedException {
        System.out.println("Enter path to file with notes (divided by space): ");
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
    }

    private static void playManually(Scanner scanner, Receiver receiver) throws InvalidMidiDataException, InterruptedException {
        System.out.println("Please enter notes:"); // (or \"exit\" to quit):");
        String text = scanner.nextLine().trim().toUpperCase();
        while (!text.equalsIgnoreCase("exit")) {
            String[] notes = text.toUpperCase().split(" ");
            //System.out.println(notes[0]);
            playNotes(receiver, notes);
            text = scanner.nextLine().trim().toUpperCase();
        }
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
        final byte A = 69;
        final byte B = 70;
        final byte C = 60;
        final byte D = 62;
        final byte E = 64;
        final byte F = 65;
        final byte G = 67;

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