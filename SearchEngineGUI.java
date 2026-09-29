import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SearchEngineGUI extends JFrame {

    // =========================
    // COLORS
    // =========================

    private static final Color BACKGROUND = new Color(12, 12, 16);
    private static final Color CARD = new Color(20, 20, 27);
    private static final Color CARD_HOVER = new Color(25, 25, 34);
    private static final Color BORDER = new Color(42, 42, 52);

    private static final Color TEXT = new Color(245, 245, 248);
    private static final Color SECONDARY = new Color(160, 160, 172);

    private static final Color ACCENT = new Color(145, 110, 255);
    private static final Color ACCENT_DARK = new Color(104, 76, 190);

    private static final Color SUCCESS = new Color(100, 220, 155);

    // =========================
    // COMPONENTS
    // =========================

    private MiniSearchEngine engine;

    private JTextField searchField;
    private JPanel resultsPanel;

    private JLabel resultTitle;
    private JLabel resultCount;

    // =========================
    // CONSTRUCTOR
    // =========================

    public SearchEngineGUI(MiniSearchEngine engine) {

        this.engine = engine;

        setTitle("Mini Search Engine");

        setSize(1100, 760);

        setMinimumSize(new Dimension(900, 650));

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(BACKGROUND);

        buildUI();
    }

    // =========================
    // MAIN UI
    // =========================

    private void buildUI() {

        JPanel main = new JPanel(new BorderLayout());

        main.setBackground(BACKGROUND);

        main.setBorder(new EmptyBorder(28, 45, 35, 45));

        // -------------------------
        // TOP BAR
        // -------------------------

        JPanel topBar = new JPanel(new BorderLayout());

        topBar.setOpaque(false);

        JLabel logo = new JLabel("✦  MINI SEARCH");

        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        logo.setForeground(TEXT);

        topBar.add(logo, BorderLayout.WEST);

        JPanel onlinePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));

        onlinePanel.setOpaque(false);

        JLabel dot = new JLabel("●");

        dot.setForeground(SUCCESS);

        dot.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        JLabel online = new JLabel("ONLINE");

        online.setForeground(SECONDARY);

        online.setFont(new Font("Segoe UI", Font.BOLD, 11));

        onlinePanel.add(dot);
        onlinePanel.add(online);

        topBar.add(onlinePanel, BorderLayout.EAST);

        main.add(topBar, BorderLayout.NORTH);

        // -------------------------
        // CENTER CONTENT
        // -------------------------

        JPanel center = new JPanel();

        center.setOpaque(false);

        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // Hero spacing
        center.add(Box.createVerticalStrut(60));

        JLabel heading = new JLabel("Search your documents.");

        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        heading.setForeground(TEXT);

        heading.setFont(new Font("Segoe UI", Font.BOLD, 38));

        center.add(heading);

        center.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel(
                "Find what you need, without the noise."
        );

        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitle.setForeground(SECONDARY);

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        center.add(subtitle);

        center.add(Box.createVerticalStrut(35));

        // -------------------------
        // SEARCH BAR
        // -------------------------

        JPanel searchContainer = new RoundedPanel(
                18,
                CARD
        );

        searchContainer.setLayout(new BorderLayout(12, 0));

        searchContainer.setBorder(
                new EmptyBorder(7, 18, 7, 7)
        );

        searchContainer.setMaximumSize(
                new Dimension(760, 62)
        );

        searchContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel searchIcon = new JLabel("⌕");

        searchIcon.setForeground(SECONDARY);

        searchIcon.setFont(
                new Font("Segoe UI", Font.PLAIN, 27)
        );

        searchContainer.add(
                searchIcon,
                BorderLayout.WEST
        );

        searchField = new JTextField();

        searchField.setBackground(CARD);

        searchField.setForeground(TEXT);

        searchField.setCaretColor(TEXT);

        searchField.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        searchField.setBorder(null);

        searchField.setToolTipText(
                "Search for a word"
        );

        searchContainer.add(
                searchField,
                BorderLayout.CENTER
        );

        RoundedButton searchButton =
                new RoundedButton(
                        "SEARCH  →",
                        ACCENT,
                        ACCENT_DARK
                );

        searchButton.setPreferredSize(
                new Dimension(125, 48)
        );

        searchContainer.add(
                searchButton,
                BorderLayout.EAST
        );

        center.add(searchContainer);

        // Search events
        searchButton.addActionListener(e -> search());

        searchField.addActionListener(e -> search());

        center.add(Box.createVerticalStrut(28));

        // -------------------------
        // STATS
        // -------------------------

        JPanel stats = new JPanel(
                new GridLayout(1, 3, 14, 0)
        );

        stats.setOpaque(false);

        stats.setMaximumSize(
                new Dimension(760, 82)
        );

        stats.setAlignmentX(Component.CENTER_ALIGNMENT);

        stats.add(
                createStatCard(
                        String.format(
                                "%02d",
                                engine.getDocumentCount()
                        ),
                        "DOCUMENTS"
                )
        );

        stats.add(
                createStatCard(
                        String.valueOf(
                                engine.getIndexedWordCount()
                        ),
                        "INDEXED WORDS"
                )
        );

        stats.add(
                createStatCard(
                        "FAST",
                        "SEARCH"
                )
        );

        center.add(stats);

        center.add(Box.createVerticalStrut(35));

        // -------------------------
        // RESULTS AREA
        // -------------------------

        RoundedPanel resultContainer =
                new RoundedPanel(
                        20,
                        CARD
                );

        resultContainer.setLayout(
                new BorderLayout()
        );

        resultContainer.setBorder(
                new EmptyBorder(
                        22,
                        25,
                        22,
                        25
                )
        );

        resultContainer.setMaximumSize(
                new Dimension(760, 270)
        );

        resultContainer.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        // Result header
        JPanel resultHeader =
                new JPanel(new BorderLayout());

        resultHeader.setOpaque(false);

        resultTitle =
                new JLabel("Ready to search");

        resultTitle.setForeground(TEXT);

        resultTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        resultCount =
                new JLabel("");

        resultCount.setForeground(SECONDARY);

        resultCount.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        resultHeader.add(
                resultTitle,
                BorderLayout.WEST
        );

        resultHeader.add(
                resultCount,
                BorderLayout.EAST
        );

        resultContainer.add(
                resultHeader,
                BorderLayout.NORTH
        );

        // Results panel
        resultsPanel = new JPanel();

        resultsPanel.setOpaque(false);

        resultsPanel.setLayout(
                new BoxLayout(
                        resultsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        showEmptyState();

        resultContainer.add(
                resultsPanel,
                BorderLayout.CENTER
        );

        center.add(resultContainer);

        center.add(Box.createVerticalStrut(25));

        // -------------------------
        // VIEW DOCUMENTS BUTTON
        // -------------------------

        RoundedButton documentsButton =
                new RoundedButton(
                        "▣   VIEW ALL DOCUMENTS",
                        CARD,
                        CARD_HOVER
                );

        documentsButton.setForeground(TEXT);

        documentsButton.setBorderColor(BORDER);

        documentsButton.setPreferredSize(
                new Dimension(240, 45)
        );

        documentsButton.setMaximumSize(
                new Dimension(240, 45)
        );

        documentsButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        documentsButton.addActionListener(
                e -> showDocumentsWindow()
        );

        center.add(documentsButton);

        main.add(
                center,
                BorderLayout.CENTER
        );

        add(main);
    }

    // =========================
    // STAT CARD
    // =========================

    private JPanel createStatCard(
            String value,
            String label
    ) {

        RoundedPanel card =
                new RoundedPanel(
                        16,
                        CARD
                );

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                new EmptyBorder(
                        13,
                        18,
                        13,
                        18
                )
        );

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setForeground(TEXT);

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        JLabel labelLabel =
                new JLabel(label);

        labelLabel.setForeground(SECONDARY);

        labelLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        card.add(
                valueLabel,
                BorderLayout.NORTH
        );

        card.add(
                labelLabel,
                BorderLayout.SOUTH
        );

        return card;
    }

    // =========================
    // SEARCH
    // =========================

    private void search() {

        String query =
                searchField.getText().trim();

        if (query.isEmpty()) {

            showEmptyState();

            resultTitle.setText(
                    "Ready to search"
            );

            resultCount.setText("");

            return;
        }

        ArrayList<Integer> results =
                engine.getSearchResults(query);

        resultsPanel.removeAll();

        resultTitle.setText(
                "Search results"
        );

        if (results.isEmpty()) {

            resultCount.setText(
                    "0 matches"
            );

            showNoResults(query);

        } else {

            resultCount.setText(
                    results.size() +
                            (results.size() == 1
                                    ? " match"
                                    : " matches")
            );

            for (int id : results) {

                resultsPanel.add(
                        createResultCard(
                                id,
                                query
                        )
                );

                resultsPanel.add(
                        Box.createVerticalStrut(8)
                );
            }
        }

        resultsPanel.revalidate();

        resultsPanel.repaint();
    }

    // =========================
    // EMPTY STATE
    // =========================

    private void showEmptyState() {

        resultsPanel.removeAll();

        JPanel empty =
                new JPanel();

        empty.setOpaque(false);

        empty.setLayout(
                new BoxLayout(
                        empty,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel icon =
                new JLabel("⌕");

        icon.setForeground(ACCENT);

        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        32
                )
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        "Search across " +
                                engine.getDocumentCount() +
                                " documents"
                );

        title.setForeground(TEXT);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel hint =
                new JLabel(
                        "Try: data, java, machine"
                );

        hint.setForeground(SECONDARY);

        hint.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        hint.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        empty.add(
                Box.createVerticalStrut(22)
        );

        empty.add(icon);

        empty.add(
                Box.createVerticalStrut(7)
        );

        empty.add(title);

        empty.add(
                Box.createVerticalStrut(5)
        );

        empty.add(hint);

        resultsPanel.add(empty);

        resultsPanel.revalidate();

        resultsPanel.repaint();
    }

    // =========================
    // NO RESULTS
    // =========================

    private void showNoResults(String query) {

        JPanel empty =
                new JPanel();

        empty.setOpaque(false);

        empty.setLayout(
                new BoxLayout(
                        empty,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel icon =
                new JLabel("○");

        icon.setForeground(SECONDARY);

        icon.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        30
                )
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        "No documents found"
                );

        title.setForeground(TEXT);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel hint =
                new JLabel(
                        "No match for \"" +
                                query +
                                "\""
                );

        hint.setForeground(SECONDARY);

        hint.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        hint.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        empty.add(
                Box.createVerticalStrut(20)
        );

        empty.add(icon);

        empty.add(
                Box.createVerticalStrut(5)
        );

        empty.add(title);

        empty.add(
                Box.createVerticalStrut(5)
        );

        empty.add(hint);

        resultsPanel.add(empty);
    }

    // =========================
    // RESULT CARD
    // =========================

    private JPanel createResultCard(
            int id,
            String query
    ) {

        RoundedPanel card =
                new RoundedPanel(
                        12,
                        new Color(25, 25, 33)
                );

        card.setLayout(
                new BorderLayout(
                        14,
                        0
                )
        );

        card.setBorder(
                new EmptyBorder(
                        11,
                        14,
                        11,
                        14
                )
        );

        JLabel number =
                new JLabel(
                        String.format(
                               ("%02d"),
                                id + 1
                        )
                );

        number.setForeground(ACCENT);

        number.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        JLabel document =
                new JLabel(
                        engine.getDocument(id)
                );

        document.setForeground(TEXT);

        document.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        JLabel match =
                new JLabel(
                        "MATCH  •  " +
                                query.toUpperCase()
                );

        match.setForeground(SECONDARY);

        match.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        9
                )
        );

        JPanel middle =
                new JPanel(
                        new BorderLayout()
                );

        middle.setOpaque(false);

        middle.add(
                document,
                BorderLayout.CENTER
        );

        middle.add(
                match,
                BorderLayout.SOUTH
        );

        card.add(
                number,
                BorderLayout.WEST
        );

        card.add(
                middle,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================
    // DOCUMENT WINDOW
    // =========================

    private void showDocumentsWindow() {

        JDialog dialog =
                new JDialog(
                        this,
                        "Indexed Documents",
                        true
                );

        dialog.setSize(
                650,
                570
        );

        dialog.setLocationRelativeTo(this);

        dialog.getContentPane()
                .setBackground(BACKGROUND);

        JPanel main =
                new JPanel(
                        new BorderLayout()
                );

        main.setBackground(BACKGROUND);

        main.setBorder(
                new EmptyBorder(
                        25,
                        28,
                        25,
                        28
                )
        );

        // Header
        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Indexed Documents"
                );

        title.setForeground(TEXT);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        JLabel subtitle =
                new JLabel(
                        engine.getDocumentCount() +
                                " documents available"
                );

        subtitle.setForeground(SECONDARY);

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(4)
        );

        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        JLabel close =
                new JLabel("✕");

        close.setForeground(SECONDARY);

        close.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        18
                )
        );

        close.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        close.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {
                        dialog.dispose();
                    }
                }
        );

        header.add(
                close,
                BorderLayout.EAST
        );

        main.add(
                header,
                BorderLayout.NORTH
        );

        // Document list
        JPanel list =
                new JPanel();

        list.setOpaque(false);

        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );

        list.setBorder(
                new EmptyBorder(
                        22,
                        0,
                        0,
                        0
                )
        );

        ArrayList<String> documents =
                engine.getDocuments();

        for (int i = 0;
             i < documents.size();
             i++) {

            list.add(
                    createDocumentCard(
                            i,
                            documents.get(i)
                    )
            );

            list.add(
                    Box.createVerticalStrut(10)
            );
        }

        JScrollPane scroll =
                new JScrollPane(list);

        scroll.setBorder(null);

        scroll.setBackground(BACKGROUND);

        scroll.getViewport()
                .setBackground(BACKGROUND);

        scroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants
                        .HORIZONTAL_SCROLLBAR_NEVER
        );

        main.add(
                scroll,
                BorderLayout.CENTER
        );

        dialog.add(main);

        dialog.setVisible(true);
    }

    // =========================
    // DOCUMENT CARD
    // =========================

    private JPanel createDocumentCard(
            int number,
            String document
    ) {

        RoundedPanel card =
                new RoundedPanel(
                        14,
                        CARD
                );

        card.setLayout(
                new BorderLayout(
                        16,
                        0
                )
        );

        card.setBorder(
                new EmptyBorder(
                        16,
                        18,
                        16,
                        18
                )
        );

        JLabel numberLabel =
                new JLabel(
                        String.format(
                                "%02d",
                                number + 1
                        )
                );

        numberLabel.setForeground(
                ACCENT
        );

        numberLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        JLabel documentLabel =
                new JLabel(
                        document
                );

        documentLabel.setForeground(TEXT);

        documentLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        JLabel status =
                new JLabel("INDEXED");

        status.setForeground(SUCCESS);

        status.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        9
                )
        );

        JPanel center =
                new JPanel(
                        new BorderLayout()
                );

        center.setOpaque(false);

        center.add(
                documentLabel,
                BorderLayout.CENTER
        );

        center.add(
                status,
                BorderLayout.SOUTH
        );

        card.add(
                numberLabel,
                BorderLayout.WEST
        );

        card.add(
                center,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================
    // ROUNDED PANEL
    // =========================

    static class RoundedPanel
            extends JPanel {

        private int radius;
        private Color backgroundColor;

        public RoundedPanel(
                int radius,
                Color backgroundColor
        ) {

            this.radius = radius;

            this.backgroundColor =
                    backgroundColor;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    backgroundColor
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================
    // ROUNDED BUTTON
    // =========================

    static class RoundedButton
            extends JButton {

        private Color normalColor;
        private Color hoverColor;

        private Color borderColor =
                new Color(
                        0,
                        0,
                        0,
                        0
                );

        public RoundedButton(
                String text,
                Color normalColor,
                Color hoverColor
        ) {

            super(text);

            this.normalColor =
                    normalColor;

            this.hoverColor =
                    hoverColor;

            setForeground(TEXT);

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            setFocusPainted(false);

            setBorderPainted(false);

            setContentAreaFilled(false);

            setOpaque(false);

            setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );
        }

        public void setBorderColor(
                Color color
        ) {

            this.borderColor = color;
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color bg =
                    getModel().isRollover()
                            ? hoverColor
                            : normalColor;

            g2.setColor(bg);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    14,
                    14
            );

            if (borderColor.getAlpha() > 0) {

                g2.setColor(
                        borderColor
                );

                g2.drawRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        14,
                        14
                );
            }

            g2.dispose();

            super.paintComponent(g);
        }
    }
}