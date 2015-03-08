package com.sbutterfly.GUI;

import com.sbutterfly.GUI.Panels.Constraint;
import com.sbutterfly.GUI.Panels.JBoxLayout;
import com.sbutterfly.GUI.Panels.JGridBagPanel;
import com.sbutterfly.core.ODEBaseModel;
import com.sbutterfly.core.ODEModelSerializer;
import com.sbutterfly.core.rope.RopeModel;
import com.sbutterfly.utils.FileAccessor;
import com.sbutterfly.utils.FileUtils;
import com.sbutterfly.utils.Log;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by Sergei on 31.01.2015.
 */
public class MainView implements Frameable, SubmitListener<ODEBaseModel> {

    private JPanel rootPanel;
    private JGridBagPanel viewsPanel;
    private JFrame frame;

    private MenuView menuView;
    private InitialStateView initialStateView;
    private AddTraceView addTraceView;
    private TraceListView traceListView;
    private Chart2D chart;

    private ODEBaseModel model;

    public MainView() {
        createGUI();
        onNew_click(null);
    }

    private void createGUI() {
        rootPanel = new JBoxLayout(BoxLayout.Y_AXIS);
        viewsPanel = new JGridBagPanel();

        addTraceView = new AddTraceView();
        addTraceView.addSubmitListener(e -> onAdd(e));
        viewsPanel.add(addTraceView, getConstraint(0, 1, 1, 1));

        traceListView = new TraceListView();
        traceListView.addRemoveListener(e -> chart.removeTrace(e));
        traceListView.addRemoveAllListener(e -> chart.removeAllTraces());
        viewsPanel.add(traceListView, getConstraint(0, 2, 1, 1));

        chart = new Chart2D();
        chart.setPreferredSize(new Dimension(700, 500));
        chart.setMinimumSize(chart.getPreferredSize());
        chart.setPaintLabels(false);
        chart.getAxisX().setAxisTitle(new IAxis.AxisTitle(""));
        chart.getAxisY().setAxisTitle(new IAxis.AxisTitle(""));

        viewsPanel.add(chart, getConstraint(1, 0, 1, 3).weightX(1).weightY(1));

        menuView = new MenuView();
        menuView.addSettingsActionListener(e -> NavigationController.open(new SettingsView()));
        menuView.addNewActionListener(e -> onNew_click(e));
        menuView.addOpenActionListener(e -> onOpen_click(e));
        menuView.addSaveActionListener(e -> onSave_click(e));
        rootPanel.add(viewsPanel);
    }

    public void setModel(ODEBaseModel model) {
        addTraceView.setEnabled(false);
        traceListView.clear();
        chart.removeAllTraces();
        if (initialStateView != null) {
            viewsPanel.remove(initialStateView);
        }

        this.model = model;
        if (initialStateView != null) {
            rootPanel.remove(initialStateView);
        }

        initialStateView = new InitialStateView(model);
        initialStateView.addSubmitListener(e -> onSubmit(e));
        viewsPanel.add(initialStateView, getConstraint(0, 0, 1, 1));
        viewsPanel.updateUI();
        rootPanel.updateUI();
    }

    private Constraint getConstraint(int gridX, int gridY, int gridWidth, int gridHeight) {
        return Constraint.create(gridX, gridY, gridWidth, gridHeight)
                .fill(GridBagConstraints.BOTH)
                .insets(5);
    }

    @Override
    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame("Ермаков Сергей. 6413 Б 300. ВКР");
            frame.setJMenuBar(menuView);
            frame.getContentPane().add(rootPanel);
            frame.pack();
            frame.setSize(1000, 700);
        }
        return frame;
    }

    public void onSubmit(ODEBaseModel model) {
        addTraceView.Init(model);
        this.model = model;
        model.values(false);
    }

    public void onAdd(AddTraceView.Traceable traceable) {

        ITrace2D trace = new Trace2DSimple(traceable.name);
        chart.addTrace(trace);
        model.setToTrace(traceable.yIndex, traceable.xIndex, trace);
        traceListView.Add(trace);

        IAxis.AxisTitle xAxisTitle = new IAxis.AxisTitle(model.getFullAxisName(traceable.xIndex));
        IAxis.AxisTitle yAxisTitle = new IAxis.AxisTitle(model.getFullAxisName(traceable.yIndex));

        xAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));
        yAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));

        chart.getAxisX().setAxisTitle(xAxisTitle);
        chart.getAxisY().setAxisTitle(yAxisTitle);
    }

    //region Menu handlers

    private void onNew_click(ActionEvent e) {
        Log.debug(this, "on new clicked");
        ODEBaseModel model = new RopeModel();

        setModel(model);
    }

    private void onSave_click(ActionEvent e) {
        Log.debug(this, "on save clicked");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(FileUtils.odeFilter);
        fileChooser.addChoosableFileFilter(FileUtils.odexFilter);

        int result = fileChooser.showDialog(rootPanel, "Сохранить модель");
        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            String ext = FileUtils.getExtension(file);
            if (ext.equals("")) {
                ext = FileUtils.odex;
                if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter) {
                    FileNameExtensionFilter fileNameExtensionFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();
                    ext = fileNameExtensionFilter.getExtensions()[0];
                }
                file = FileUtils.setExtension(file, ext);
            }
            Log.debug(this, "Selected: " + file.getName());

            try {
                String serialized = ODEModelSerializer.serialize(model, FileUtils.getSerializeType(file));
                FileAccessor.write(file, serialized);
            } catch (Exception ex) {
                Log.error(this, ex);
            }
        } else {
            Log.debug(this, "Cancelled");
        }
    }

    private void onOpen_click(ActionEvent e) {
        Log.debug(this, "on open clicked");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(FileUtils.bothFilter);

        int result = fileChooser.showDialog(rootPanel, "Открыть модель");
        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            String ext = FileUtils.getExtension(file);
            Log.debug(this, "Selected: " + file.getName());

            try {
                String text = FileAccessor.read(file);
                ODEBaseModel model = ODEModelSerializer.deserialize(text);
                setModel(model);
            } catch (Exception ex) {
                Log.error(this, ex);
            }
        } else {
            Log.debug(this, "Cancelled");
        }
    }

    //endregion
}
