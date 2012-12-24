package Modele;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ModeleBrowserTable implements TableModel 
{
	private String[] columnNames = {"Title", "Artist", "BPM", "Length"};

	private Object[][] data = {
	{"Kathy", "Smith",
	"Snowboarding", new Integer(5), new Boolean(false)},
	{"John", "Doe",
	"Rowing", new Integer(3), new Boolean(true)},
	{"Sue", "Black",
	"Knitting", new Integer(2), new Boolean(false)},
	{"Jane", "White",
	"Speed reading", new Integer(20), new Boolean(true)},
	{"Joe", "Brown",
	"Pool", new Integer(10), new Boolean(false)}
	};

	public ModeleBrowserTable()
	{
		
	}

	public int getColumnCount()
	{
		return 4;
	}

	public Class<?> getColumnClass(int columnIndex)
	{
		return String.class;
	}

	public String getColumnName(int col)
	{
        return columnNames[col];
    }
	
	public int getRowCount()
	{
		return data.length;
	}

	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
 
	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
	}
	
	public void addTableModelListener(TableModelListener l)
	{
	}

	public void removeTableModelListener(TableModelListener l) 
	{
	}
}
