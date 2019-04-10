package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class GridBagHelp
{
	public static GridBagLayout makeLayout(int[] columnWidths, int[] rowHeights,
			double[] columnWeights, double[] rowWeights)
	{
		GridBagLayout temp_layout = new GridBagLayout();
		temp_layout.columnWidths = columnWidths;
		temp_layout.rowHeights = rowHeights;
		temp_layout.columnWeights = columnWeights;
		temp_layout.rowWeights = rowWeights;
		
		return temp_layout;
	}
	
	public static GridBagConstraints makeConstraints(int gridx, int gridy)
	{
		GridBagConstraints temp_constraints = new GridBagConstraints();
		temp_constraints.gridx = gridx;
		temp_constraints.gridy = gridy;
		
		return temp_constraints;
	}
	
	public static GridBagConstraints makeConstraints(int gridx, int gridy,
			int fill)
	{
		GridBagConstraints temp_constraints = makeConstraints(gridx, gridy);
		temp_constraints.fill = fill;
		
		return temp_constraints;
	}
	
	public static GridBagConstraints makeConstraints(int gridx, int gridy,
			int fill, Insets insets)
	{
		GridBagConstraints temp_constraints = makeConstraints(gridx, gridy, fill);
		temp_constraints.insets = insets;
		
		return temp_constraints;
	}
}
