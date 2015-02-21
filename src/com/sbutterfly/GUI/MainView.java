package com.sbutterfly.GUI;

import com.sbutterfly.GUI.Panels.Constraint;
import com.sbutterfly.GUI.Panels.JBoxLayout;
import com.sbutterfly.GUI.Panels.JGridBagPanel;
import com.sbutterfly.differential.ODEBaseModel;
import com.sbutterfly.pendulum.PendulumModel;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sergei on 31.01.2015.
 */
public class MainView implements Frameable {

    private JPanel rootPanel;
    private JFrame frame;
    private Chart2D chart;

    private MenuView menuView;
    private InitialStateView initialStateView;
    private AddTraceView traceView;
    private TraceListView traceListView;
    private ODEBaseModel model;

    public MainView() {
        createGUI();
    }

    private void createGUI() {
        rootPanel = new JBoxLayout(BoxLayout.Y_AXIS);

        JGridBagPanel panel = new JGridBagPanel();
        //panel.setBackground(Color.GREEN);

        initialStateView = new InitialStateView(new PendulumModel());
        initialStateView.addSubmitListener(e -> OnSubmit(e));
        //initialStateView.setBackground(Color.BLUE);
        panel.add(initialStateView, getConstraint(0, 0, 1, 1));

        traceView = new AddTraceView();
        traceView.addSubmitListener(e -> OnAdd(e));
        //traceView.setBackground(Color.CYAN);
        panel.add(traceView, getConstraint(0, 1, 1, 1));

        traceListView = new TraceListView();
        traceListView.addRemoveListener(e -> chart.removeTrace(e));
        traceListView.addRemoveAllListener(e -> chart.removeAllTraces());
        //traceListView.setBackground(Color.YELLOW);
        panel.add(traceListView, getConstraint(0, 2, 1, 1));

        chart = new Chart2D();
        chart.setPreferredSize(new Dimension(700, 500));
        chart.setMinimumSize(chart.getPreferredSize());
        chart.setPaintLabels(false);

        panel.add(chart, getConstraint(1, 0, 1, 3).weightX(1).weightY(1));

        menuView = new MenuView();
        menuView.addSettingsActionListener(e -> NavigationController.Open(new SettingsView()));
        menuView.setBackground(Color.YELLOW);
        rootPanel.add(panel);
    }

    private Constraint getConstraint(int gridX, int gridY, int gridWidth, int gridHeight) {
        return Constraint.create(gridX, gridY, gridWidth, gridHeight)
                .fill(GridBagConstraints.BOTH)
                .insets(5);
    }

    @Override
    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame("My Program Change TITLE!!!");
            frame.setJMenuBar(menuView);
            frame.getContentPane().add(rootPanel);
            frame.pack();
            frame.setSize(1000, 700);
        }
        return frame;
    }

    public void OnSubmit(ODEBaseModel model) {
        traceView.Init(model);
        this.model = model;
    }

    public void OnAdd(AddTraceView.Traceable traceable){

        ITrace2D trace1 = new Trace2DSimple();
        chart.addTrace(trace1);

        model.setToTrace(traceable.yIndex, traceable.xIndex, trace1);
        trace1.setName(traceable.name);

        traceListView.Add(trace1);
    }
}
