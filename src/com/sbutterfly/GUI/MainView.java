package com.sbutterfly.GUI;

import com.sbutterfly.differential.*;
import com.sbutterfly.pendulum.PendulumModel;
import info.monitorenter.gui.chart.*;
import info.monitorenter.gui.chart.traces.*;

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
    private AdditionalLineView additionalLineView;

    public MainView() {
        createGUI();
    }

    private void createGUI() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.GREEN);

        initialStateView = new InitialStateView(new PendulumModel());
        initialStateView.addSubmitListener(e -> OnSubmit(e));
        initialStateView.setBackground(Color.BLUE);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 5, 5, 5);
        panel.add(initialStateView, c);

        traceView = new AddTraceView();
        traceView.addSubmitListener(e -> OnAdd(e));
        traceView.setBackground(Color.CYAN);

        c.gridy = 1;
        panel.add(traceView, c);

        traceListView = new TraceListView();
        traceListView.addRemoveListener(e -> chart.removeTrace(e));
        traceListView.addRemoveAllListener(e -> chart.removeAllTraces());
        traceListView.setBackground(Color.YELLOW);

        c.gridy = 2;
        panel.add(traceListView, c);

        chart = new Chart2D();
        chart.setMinimumSize(new Dimension(500, 500));
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 3;
        c.gridwidth = 2;
        panel.add(chart, c);

        menuView = new MenuView();
        menuView.addSettingsActionListener(e -> NavigationController.Open(new SettingsView()));
        menuView.setBackground(Color.YELLOW);

        //additionalLineView = new AdditionalLineView();

        rootPanel.add(menuView);
        rootPanel.add(panel);
        //rootPanel.add(additionalLineView.getComponent());
    }

    @Override
    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame("My Program Change TITLE!!!");
            frame.getContentPane().add(rootPanel);
            frame.pack();
            frame.setSize(500, 500);
        }
        return frame;
    }

    private ODEBaseModel model;
    public void OnSubmit(ODEBaseModel model) {
        chart.removeAllTraces();
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
