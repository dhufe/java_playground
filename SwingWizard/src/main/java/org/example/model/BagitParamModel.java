package org.example.model;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Vector;

public class BagitParamModel implements TableModel {
    private final Vector<BagitParam> bagitParameters = new Vector<>();
    private final Vector<TableModelListener> listeners = new Vector<>();

    public void addParameter(BagitParam parameter) {
        int index = this.bagitParameters.size();
        bagitParameters.add(parameter);

        // Event erzeugen
        TableModelEvent e = new TableModelEvent(this, index, index,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);

        // Nun das Event verschicken
        for (TableModelListener listener : listeners) {
            listener.tableChanged(e);
        }
    }

    @Override
    public int getRowCount() {
        return bagitParameters.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> "Parameter";
            case 1 -> "Wert";
            default -> null;
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1 -> String.class;
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;

        /*
        return switch (columnIndex) {
            case 0 -> false;
            case 1 -> true;
            default -> false;
        };
        */

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BagitParam bp = bagitParameters.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> bp.getParameterName();
            case 1 -> bp.getParameterValue();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        BagitParam bp = bagitParameters.get(rowIndex);

        switch (columnIndex) {
            case 0:
                bp.setParameterName((String) aValue);
                break;
            case 1:
                bp.setParameterValue((String) aValue);
                break;
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }
}
