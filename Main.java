import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.Buffer;


public class Main {
    JFrame frame;
    JPanel mainPanel;
    JLabel mainLabel;
    JButton createExcelSheets, createIncomeExpenseCSVs, createMapping;

    Process process;


    public Main() {






        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100,100,100,100));

        frame = new JFrame();

        mainLabel = new JLabel("One Stop Shop for Excel Shenanigans");

        createExcelSheets = new JButton("Create New Excel Sheets");
        createIncomeExpenseCSVs = new JButton("Create Income and Expense CSVs");
        createMapping = new JButton("Create Mapping");

        mainPanel.add(mainLabel);
        mainPanel.add(createExcelSheets);
        mainPanel.add(createIncomeExpenseCSVs);
        mainPanel.add(createMapping);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Bank Account Organisation");
        frame.pack();
        frame.setVisible(true);

        createExcelSheets.addActionListener(e1 -> {
            try {
                givenPythonScript_whenPythonProcessInvoked_thenSuccess();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public static void main(String[] args) {
        new Main();
    }


    public void givenPythonScript_whenPythonProcessInvoked_thenSuccess() throws IOException {

        ProcessBuilder pb = new ProcessBuilder("python","Business Audit\\create_excel_files_purely_from_code_with_libraries.py", "1", "4");
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader readers = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String lines = null;
        while ((lines=reader.readLine())!=null) {
            System.out.println("lines " + lines);
        }

        while ((lines=readers.readLine())!=null) {
            System.out.println("Error lines " + lines);
        }
    }


    private String resolvePythonScriptPath(String path){
        File file = new File(path);
        return file.getAbsolutePath();
    }

}
