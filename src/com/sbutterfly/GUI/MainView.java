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
        panel.add(initialStateView, getConstraint(0,0,1,1));

        traceView = new AddTraceView();
        traceView.addSubmitListener(e -> OnAdd(e));
        traceView.setBackground(Color.CYAN);
        panel.add(traceView, getConstraint(0,1,1,1));

        traceListView = new TraceListView();
        traceListView.addRemoveListener(e -> chart.removeTrace(e));
        traceListView.addRemoveAllListener(e -> chart.removeAllTraces());
        traceListView.setBackground(Color.YELLOW);
        panel.add(traceListView, getConstraint(0,2,1,1));

        chart = new Chart2D();
        chart.setMinimumSize(new Dimension(500, 500));
        panel.add(chart, getConstraint(1,0,2,3));

        menuView = new MenuView();
        menuView.addSettingsActionListener(e -> NavigationController.Open(new SettingsView()));
        menuView.setBackground(Color.YELLOW);

        //additionalLineView = new AdditionalLineView();

        rootPanel.add(menuView);
        rootPanel.add(panel);
        //rootPanel.add(additionalLineView.getComponent());
    }

    private GridBagConstraints getConstraint(int gridX, int gridY, int gridwidth, int gridheight){
        GridBagConstraints c = new GridBagConstraints();
        //c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = gridX;
        c.gridy = gridY;
        //c.anchor = GridBagConstraints.EAST;
        c.gridheight = gridheight;
        c.gridwidth = gridwidth;
        c.insets = new Insets(5, 5, 5, 5);
        return c;
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
