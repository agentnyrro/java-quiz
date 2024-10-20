package questions;

import report.*;

import jdk.javadoc.doclet.Reporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public abstract class Question {
    protected List<String> questions;
    protected List<String> correctAnswers;
    protected List<List<String>> answers;

    public Question() {
        questions = new ArrayList<>();
        correctAnswers = new ArrayList<>();
        answers = new ArrayList<>();
    }

    // Metoda do ładowania pytań z pliku
    protected void loadQuestions(String filePath) {
        QuestionLoader loader = new QuestionLoader();
        loader.load(filePath);
    }

    // Klasa wewnętrzna do ładowania pytań
    private class QuestionLoader {
        // Główna metoda do ładowania pytań
        public void load(String filePath) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    processLine(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Metoda do przetwarzania pojedynczej linii
        private void processLine(String line) {
            String[] parts = line.split("\\|");
            if (isLineValid(parts)) {
                addQuestion(parts);
            } else {
                Logger logger = ReportHandler.getLogger();
                logger.severe("Niepoprawny format pytania: " + line);
            }
        }

        // Metoda do walidacji linii
        private boolean isLineValid(String[] parts) {
            return parts.length >= 3; // Upewnij się, że długość tablicy jest odpowiednia
        }

        // Metoda do dodawania pytania i odpowiedzi do list
        private void addQuestion(String[] parts) {
            questions.add(parts[0]); // Pytanie
            correctAnswers.add(parts[1]); // Poprawna odpowiedź

            Logger logger = ReportHandler.getLogger();
            logger.info("Pobrano pytanie: " + parts[0]);
            logger.info("Poprawna odpowiedź: " + parts[1]);

            // Dodaj wszystkie odpowiedzi (parts[2], .. parts[5])
            List<String> currAnswers = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                currAnswers.add(parts[i]);
                logger.info("Odpowiedź " + (i-1) + ": " + parts[i]);
            }
            answers.add(currAnswers);
        }
    }
}
