public class ExpressionPanel extends JPanel {

    MainPanel mp;
    JTextArea area = new JTextArea();

    public ExpressionPanel(MainPanel mp) {
        this.mp = mp;
        initUI();
    }

    private void initUI() {
        JTextField panelName = new JTextField("Expression Panel");
        panelName.setEditable(false);
        area.setEditable(false);
        area.setTabSize(2);
        setLayout(new BorderLayout());
        add(panelName, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }
}
