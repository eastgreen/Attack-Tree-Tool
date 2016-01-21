public class MainFrame extends JFrame implements WindowListener {

    MainPanel mp = new MainPanel();
    JMenuBar menuBar = new JMenuBar();
    JMenuItem item;
    JMenu menu, submenu;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        getContentPane().add(mp);
        //create menuBar
        menu = new JMenu("File");
        item = new JMenuItem("New");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mp.tp.newCanvas(1);
            }

        });
        menu.add(item);

        item = new JMenuItem("Open");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mp.openFile();
            }

        });
        menu.add(item);

        item = new JMenuItem("Save");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mp.saveFile();
            }

        });
        menu.add(item);

        item = new JMenuItem("Exit");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                windowClosing(null);
            }

        });
        menu.add(item);
        setJMenuBar(menuBar);
        addWindowListener(this);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Windows".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
                    //another look and feel.
                }
                MainFrame mf = new MainFrame();
                mf.setVisible(true);
            }
        });
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int option = JOptionPane.showOptionDialog(
                this,
                "Are you sure you want to exit?",
                "Exit", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null, null,
                null);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
