package Model;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LoggerGUI extends JPanel
{
    private Logger loggerRef;
    private int width = 800;
    private int height = 800;
    private JList<String> logList;
    private JScrollPane pane;
    private JPanel pnlList;
    private JPanel pnlDateInput;
    private JPanel pnlDateInputWest;
    private JTextField tfLower;
    private JTextField tfUpper;
    private JButton btnRefresh;

    public LoggerGUI(Logger loggerRef) {
        this.loggerRef = loggerRef;
        setLayout(new BorderLayout());
        pnlList = new JPanel();
        pnlList.setLayout(new BorderLayout());
        int heightFactor = (int) (height * 0.1);
        CreateListPanel(width, 4*heightFactor);

        pnlDateInput = new JPanel( );
        pnlDateInputWest = new JPanel( );
        tfLower = new JTextField( loggerRef.GetFileContentDateFirst().toString() );
        tfUpper = new JTextField( loggerRef.GetFileContentDateLast().toString() );
        btnRefresh = new JButton("Refresh List");

        tfLower.setColumns( 12 );
        tfUpper.setColumns( 12 );

        pnlDateInputWest.setLayout( new BoxLayout( pnlDateInputWest, BoxLayout.PAGE_AXIS ) );
        pnlDateInputWest.add( tfLower );
        pnlDateInputWest.add( tfUpper );
        pnlDateInputWest.add( btnRefresh );
        pnlDateInput.add( pnlDateInputWest, BorderLayout.CENTER );
        add( pnlDateInput, BorderLayout.SOUTH );

        btnRefresh.addActionListener( e -> UpdateList() );
        UpdateList();
    }

    private void CreateListPanel( int width, int height) {
        pnlList.setPreferredSize(new Dimension(width, height));
        pnlList.setBorder(new TitledBorder("File content"));
        logList = new JList<>();
        logList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        pane = new JScrollPane( logList );
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setPreferredSize(new Dimension(width, height));
        pnlList.add(pane);
        add( pnlList, BorderLayout.NORTH);
    }

    public void CreateFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(width, height));
        frame.setTitle("Logger");
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void UpdateList()
    {
        logList.setListData( loggerRef.GetFileContent(tfLower.getText(), tfUpper.getText()).toArray( new String[0] ) );
    }
}
