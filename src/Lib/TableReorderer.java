package src.Lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.*;

public class TableReorderer extends TransferHandler {
    @Override
    public int getSourceActions(JComponent c) {
        return DnDConstants.ACTION_COPY_OR_MOVE;
    }

    @Override
    public Transferable createTransferable(JComponent comp) {
        JTable table = (JTable) comp;
        int row = table.getSelectedRow();
        StringSelection transferable = new StringSelection(String.valueOf(row));

        return transferable;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info){
        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)){
            return false;
        }
        return true;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }

        if (!canImport(support)) {
            return false;
        }

        JTable table = (JTable) support.getComponent();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        JTable.DropLocation dl = (JTable.DropLocation)support.getDropLocation();

        TableColumnModel copiedColumns = table.getColumnModel();
        DefaultTableModel copiedModel = new DefaultTableModel() {
            @Override
            public int getRowCount() {
                return tableModel.getRowCount();
            }
            @Override
            public int getColumnCount() {
                return tableModel.getColumnCount();
            }
        };

        for (int i = 0; i < copiedColumns.getColumnCount(); i++) {
            copiedModel.addColumn(copiedColumns.getColumn(i).getHeaderValue());
        }

        int initialRow;
        int droppedRow;

        try {
            String tmp = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            initialRow =  Integer.valueOf(tmp);
        } catch (UnsupportedFlavorException | IOException e) {
            return false;
        }

        droppedRow = dl.getRow();

        boolean droppedGreaterThanInitial = (droppedRow > initialRow);
        String initialString = (String) tableModel.getValueAt(initialRow, 0);
        int initialColumnID = (int) tableModel.getValueAt(initialRow, 1);

        if (droppedGreaterThanInitial) {
            droppedRow--;
        } else {
            initialRow++;
        }


        int offset = 0;

//        logic to handle the shuffling
        try {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (i == droppedRow) {
                    offset--;
                }
                if (i == initialRow) {
                    offset++;
                }
                for (int j = 0; j < 2; j++) {
                    if ((i != droppedRow)) {
                        copiedModel.setValueAt(tableModel.getValueAt(i + offset, j), i, j);
                    }
                }
                copiedModel.setValueAt(i, i, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        copiedModel.setValueAt(initialString, droppedRow, 0);
        copiedModel.setValueAt(initialColumnID, droppedRow, 1);


        table.setModel(copiedModel);

        return true;
    }
}
