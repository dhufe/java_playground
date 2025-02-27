package org.example.model;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Vector;

public class BagitParamModel implements TableModel {
    private Vector bagitParameters = new Vector();
    private Vector listeners = new Vector();

    public void addParameter(BagitParam parameter) {
        int index = this.bagitParameters.size();
        bagitParameters.add(parameter);

        // Event erzeugen
        TableModelEvent e = new TableModelEvent(this, index, index,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);

        // Nun das Event verschicken
        for (int i = 0, n = listeners.size(); i < n; i++) {
            ((TableModelListener) listeners.get(i)).tableChanged(e);
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
        switch (columnIndex) {
            case 0:
                return "Parameter";
            case 1:
                return "Wert";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BagitParam bp = (BagitParam) bagitParameters.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return bp.getParameterName();
            case 1:
                return bp.getParameterValue();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        BagitParam bp = (BagitParam) bagitParameters.get(rowIndex);

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
