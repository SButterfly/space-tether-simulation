package com.sbutterfly.gui;

import com.sbutterfly.gui.hardcoded.RopeInitialStateView;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JBoxLayout;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.core.BaseSystem;
import com.sbutterfly.core.SystemSerializer;
import com.sbutterfly.core.rope.RopeSystem;
import com.sbutterfly.utils.FileAccessor;
import com.sbutterfly.utils.FileUtils;
import com.sbutterfly.utils.Log;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by Sergei on 31.01.2015.
 */
public class MainView implements Frameable, SubmitListener<BaseSystem> {
    private JPanel rootPanel;
    private JGridBagPanel viewsPanel;
    private JFrame frame;

    private MenuView menuView;
    private InitialStateView initialStateView;
    private AddTraceView addTraceView;
    private TraceListView traceListView;
    private Chart2D chart;
    private AdditionalLineView additionalLineView;

    private BaseSystem currentModel;

    public MainView() {
        createGUI();
        onNewModel(null);
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

        additionalLineView = new AdditionalLineView();
        viewsPanel.add(additionalLineView, getConstraint(0, 3, 3, 1));

        menuView = new MenuView();
        menuView.addSettingsActionListener(e -> NavigationController.open(new SettingsView(currentModel)));
        menuView.addNewActionListener(e -> onNewModel(e));
        menuView.addOpenActionListener(e -> oLoadModel(e));
        menuView.addSaveActionListener(e -> onSaveModel(e));
        rootPanel.add(viewsPanel);
    }

    public void setModel(BaseSystem model) {
        addTraceView.setEnabled(false);
        traceListView.clear();
        chart.removeAllTraces();
        if (initialStateView != null) {
            viewsPanel.remove(initialStateView);
        }

        this.currentModel = model;

        initialStateView = new RopeInitialStateView(model);
        initialStateView.addSubmitListener(e -> onSubmit(e));
        if (model.hasValues()) {
            onSubmit(model);
        }
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
            frame = new JFrame("Программа развертывания космической тросовой системы");
            frame.setJMenuBar(menuView);
            frame.getContentPane().add(rootPanel);
            frame.pack();
            frame.setSize(1700, 1000);
        }
        return frame;
    }

    public void onSubmit(BaseSystem model) {
        this.currentModel = model;
        addTraceView.setEnabled(false);

        // TODO change to thread pool
        new Thread(() -> {
            try {
                model.values(false);
                AdditionalLineView.Processable p = model.getProcessable();
                if (p != null && !p.hasCanceled()) {
                    SwingUtilities.invokeLater(() -> addTraceView.init(model));
                    additionalLineView.setText("Расчет закончен");
                } else {
                    additionalLineView.setText("Расчет отменен");
                }
            } catch (Exception e) {
                AdditionalLineView.Processable p = model.getProcessable();
                if (p != null) {
                    p.cancel();
                }
                SwingUtilities.invokeLater(() -> {
                    additionalLineView.setText("Произошла ошибка");
                    JOptionPane.showMessageDialog(null, e.getMessage());
                });
            }
        }).start();

        AdditionalLineView.Processable processable = null;
        while (processable == null || processable.hasEnded()) {
            processable = model.getProcessable();
        }
        additionalLineView.setText("Выполняется расчет");
        additionalLineView.setProcessable(processable);
    }

    public void onAdd(AddTraceView.Traceable traceable) {
        ITrace2D trace = new Trace2DSimple(traceable.name);
        chart.addTrace(trace);
        currentModel.setToTrace(traceable.yIndex, traceable.xIndex, trace);
        traceListView.add(trace);

        IAxis.AxisTitle xAxisTitle = new IAxis.AxisTitle(currentModel.getFullAxisName(traceable.xIndex));
        IAxis.AxisTitle yAxisTitle = new IAxis.AxisTitle(currentModel.getFullAxisName(traceable.yIndex));

        xAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));
        yAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));

        chart.getAxisX().setAxisTitle(xAxisTitle);
        chart.getAxisY().setAxisTitle(yAxisTitle);
    }

    //region Menu handlers

    private void onNewModel(ActionEvent e) {
        Log.debug(this, "on new clicked");
        BaseSystem model = new RopeSystem();

        setModel(model);
    }

    private void onSaveModel(ActionEvent e) {
        Log.debug(this, "on save clicked");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(FileUtils.ODE_FILTER);
        fileChooser.addChoosableFileFilter(FileUtils.ODE_FILTER);

        int result = fileChooser.showDialog(rootPanel, "Сохранить модель");
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String ext = FileUtils.getExtension(file);
            if (ext.equals("")) {
                ext = FileUtils.ODEX;
                if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter) {
                    FileNameExtensionFilter fileNameExtensionFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();
                    ext = fileNameExtensionFilter.getExtensions()[0];
                }
                file = FileUtils.setExtension(file, ext);
            }
            Log.debug(this, "Selected: " + file.getName());

            final File ffile = file;
            try {
                String serialized = SystemSerializer.serialize(currentModel);
                FileAccessor.write(ffile, serialized);
            } catch (Exception ex) {
                Log.error(this, ex);
                JOptionPane.showMessageDialog(null, "Произошла ошибка при сохраенении файла");
            }
        } else {
            Log.debug(this, "Cancelled");
        }
    }

    private void oLoadModel(ActionEvent e) {
        Log.debug(this, "on open clicked");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(FileUtils.ODE_FILTER);

        int result = fileChooser.showDialog(rootPanel, "Открыть модель");
        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            Log.debug(this, "Selected: " + file.getName());
            try {
                String text = FileAccessor.read(file);
                BaseSystem model = SystemSerializer.deserialize(text);
                setModel(model);
            } catch (Exception ex) {
                Log.error(this, ex);
                JOptionPane.showMessageDialog(null, "Файл поврежден или недопустимого формата");
            }
        } else {
            Log.debug(this, "Cancelled");
        }
    }

    //endregion
}
