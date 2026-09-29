import javax.swing.*;
import java.util.*;

class TrieNode {
    HashMap<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}

class MiniSearchEngine {

    private HashMap<String, ArrayList<Integer>> index = new HashMap<>();
    private TrieNode root = new TrieNode();
    private ArrayList<String> documents = new ArrayList<>();

    // Add document
    public void addDocument(String document) {

        int documentId = documents.size();

        documents.add(document);

        String[] words = document.toLowerCase()
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .split("\\s+");

        for (String word : words) {

            if (word.isEmpty())
                continue;

            index.putIfAbsent(word, new ArrayList<>());

            if (!index.get(word).contains(documentId)) {
                index.get(word).add(documentId);
            }

            insertIntoTrie(word);
        }
    }

    // Insert word into Trie
    private void insertIntoTrie(String word) {

        TrieNode current = root;

        for (char c : word.toCharArray()) {

            current.children.putIfAbsent(c, new TrieNode());

            current = current.children.get(c);
        }

        current.isEnd = true;
    }

    // Search word
    public ArrayList<Integer> getSearchResults(String word) {

        ArrayList<Integer> results = new ArrayList<>();

        if (word == null || word.trim().isEmpty()) {
            return results;
        }

        word = word.toLowerCase().trim();

        if (index.containsKey(word)) {
            results.addAll(index.get(word));
        }

        return results;
    }

    public String getDocument(int id) {

        if (id >= 0 && id < documents.size()) {
            return documents.get(id);
        }

        return "";
    }

    public ArrayList<String> getDocuments() {
        return documents;
    }

    public int getDocumentCount() {
        return documents.size();
    }

    public int getIndexedWordCount() {
        return index.size();
    }
}

class Main {

    public static void main(String[] args) {

        MiniSearchEngine engine = new MiniSearchEngine();

        // 5 Documents
        engine.addDocument("Data Structures and Algorithms");
        engine.addDocument("Learn Java Programming");
        engine.addDocument("Web Development using HTML CSS");
        engine.addDocument("Data Structures using C++");
        engine.addDocument("Introduction to Machine Learning");

        // 20 Documents
        engine.addDocument("Data Structures and Algorithms");
        engine.addDocument("Learn Java Programming");
        engine.addDocument("Web Development using HTML CSS");
        engine.addDocument("Data Structures using C++");
        engine.addDocument("Introduction to Machine Learning");

        engine.addDocument("Database Management System");
        engine.addDocument("Operating System Concepts");
        engine.addDocument("Computer Networks");
        engine.addDocument("Object Oriented Programming");
        engine.addDocument("Software Engineering");

        engine.addDocument("Artificial Intelligence Basics");
        engine.addDocument("Cloud Computing Fundamentals");
        engine.addDocument("Cyber Security and Ethical Hacking");
        engine.addDocument("Python Programming");
        engine.addDocument("JavaScript for Web Development");


        engine.addDocument("Computer Architecture");
        engine.addDocument("Theory of Computation");
        engine.addDocument("Compiler Design");
        engine.addDocument("Mobile Application Development");
        engine.addDocument("Full Stack Web Development");


        SwingUtilities.invokeLater(() -> {

            SearchEngineGUI gui = new SearchEngineGUI(engine);

            gui.setVisible(true);
        });
    }
}