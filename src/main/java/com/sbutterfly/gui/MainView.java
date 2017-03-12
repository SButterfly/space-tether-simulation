package com.sbutterfly.gui;

import com.sbutterfly.core.SystemSerializer;
import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelSet;
import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JBoxLayout;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.services.ModelSetFactory;
import com.sbutterfly.utils.FileUtils;
import com.sbutterfly.utils.Log;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Sergei on 31.01.2015.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class MainView implements Frameable {
    private JPanel rootPanel;
    private JGridBagPanel viewsPanel;
    private JFrame frame;

    private MenuView menuView;
    private InitialStateView initialStateView;
    private ModelsListView modelsListView;
    private TraceSelectionView traceSelectionView;
    private AdditionalLineView additionalLineView;
    private ChartView chartView;
    private SettingsView settingsView;

    private ModelSet modelSet = ModelSetFactory.createNewModelSet();
    private ColorIterator colorIterator = new ColorIterator();
    private IntegerIterator integerIterator = new IntegerIterator(1);

    public MainView() {
        createGUI();
        createNewModel(modelSet.createModel());
        traceSelectionChanged(modelSet.getModelTraces().get(0));
    }

    private void createGUI() {
        rootPanel = new JBoxLayout(BoxLayout.Y_AXIS);
        viewsPanel = new JGridBagPanel();

        initialStateView = new InitialStateView();
        initialStateView.addSubmitListener(e -> {
            switch (e.getState()) {
                case CREATE:
                    createNewModel(modelSet.createModel());
                    onModelCreated(e.getModel());
                    break;
                case EDIT:
                    createNewModel(modelSet.createModel());
                    // HACK quick way to update model
                    onModelDeleted(e.getModel());
                    onModelAdded(e.getModel());
                    break;
                case CANCEL:
                    createNewModel(modelSet.createModel());
                    modelsListView.desselect();
                    break;
                default:
                    throw new RuntimeException("Don't know state: " + e.getState());
            }
        });

        viewsPanel.add(initialStateView, getConstraint(0, 0, 1, 1));
        viewsPanel.updateUI();
        rootPanel.updateUI();

        modelsListView = new ModelsListView();
        modelsListView.addEventListener(e -> {
            switch (e.getStatus()) {
                case ADDED:
                    onModelAdded(e.getModel());
                    break;
                case SHOWED:
                    onModelAppeared(e.getModel());
                    break;
                case DELETED:
                    onModelDeleted(e.getModel());
                    break;
                case HID:
                    onModelDisappeared(e.getModel());
                    break;
                case SELECTED:
                    onModelSelected(e.getModel());
                    break;
                default:
                    throw new RuntimeException("Don't know status: " + e.getStatus());
            }
        });
        viewsPanel.add(modelsListView, getConstraint(0, 1, 1, 1).weightY(1));

        traceSelectionView = new TraceSelectionView();
        traceSelectionView.setTraceDescriptions(modelSet.getModelTraces());
        traceSelectionView.addEventListener(td -> traceSelectionChanged(td.getTraceDescription()));
        viewsPanel.add(traceSelectionView, getConstraint(0, 2, 1, 1));

        chartView = new ChartView();
        chartView.setPreferredSize(new Dimension(700, 500));
        chartView.setMinimumSize(chartView.getPreferredSize());

        viewsPanel.add(chartView, getConstraint(1, 0, 1, 3).weightX(1).weightY(1));

        additionalLineView = new AdditionalLineView();
        viewsPanel.add(additionalLineView, getConstraint(0, 3, 3, 1));

        settingsView = new SettingsView();
        settingsView.setModelSet(modelSet);

        menuView = new MenuView();
        menuView.addSettingsActionListener(e -> NavigationController.open(settingsView));
        menuView.addNewActionListener(e -> onNewModelSet(e));
        menuView.addOpenActionListener(e -> oLoadModel(e));
        menuView.addSaveActionListener(e -> onSaveModel(e));
        rootPanel.add(viewsPanel);
    }

    private void onModelAdded(Model model) {
        modelSet.add(model);
        chartView.addModel(model);
    }

    private void onModelDeleted(Model model) {
        modelSet.remove(model);
        chartView.removeModel(model);
    }

    private void onModelAppeared(Model model) {
        chartView.appearModel(model);
    }

    private void onModelDisappeared(Model model) {
        chartView.hideModel(model);
    }

    private void onModelSelected(Model model) {
        initialStateView.setModel(model, true);
    }

    private void traceSelectionChanged(TraceDescription traceDescription) {
        chartView.showTraceDescription(traceDescription);
    }

    public void setModelSet(ModelSet modelSet) {
        modelsListView.clear();
        createNewModel(modelSet.createModel());
        for (Model model : modelSet) {
            modelsListView.add(model);
        }
        // TODO HACK
        // присваивание происходит в конце, так как иначе в modelSet будут дублирующиеся значения
        this.modelSet = modelSet;
    }

    public void createNewModel(Model model) {
        initialStateView.setModel(model, false);

        viewsPanel.add(initialStateView, getConstraint(0, 0, 1, 1));
        viewsPanel.updateUI();
        rootPanel.updateUI();
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

    public void onModelCreated(Model model) {
        String name = "Система " + integerIterator.next();
        Color color = colorIterator.next();
        model.setName(name);
        model.setColor(color);
        modelsListView.add(model);
    }

    private void onNewModelSet(ActionEvent e) {
        Log.debug(this, "on new clicked");

        modelSet.clear();
        modelsListView.clear();

        ModelSet newModelSet = ModelSetFactory.createNewModelSet();
        setModelSet(newModelSet);
    }

    private void onSaveModel(ActionEvent e) {
        Log.debug(this, "on save clicked");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(FileUtils.ODE_FILTER);
        fileChooser.addChoosableFileFilter(FileUtils.ODE_FILTER);

        int result = fileChooser.showDialog(rootPanel, "Сохранить систему");
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String ext = FileUtils.getExtension(file);
            if (ext.equals("")) {
                ext = FileUtils.ODEX;
                if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter) {
                    FileNameExtensionFilter fileNameExtensionFilter =
                            (FileNameExtensionFilter) fileChooser.getFileFilter();
                    ext = fileNameExtensionFilter.getExtensions()[0];
                }
                file = FileUtils.setExtension(file, ext);
            }
            Log.debug(this, "Selected: " + file.getName());

            try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file))) {
                SystemSerializer.serialize(modelSet, dataOutputStream);
            } catch (IOException ex) {
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

            try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file))) {
                ModelSet set = SystemSerializer.deserialize(dataInputStream);
                // start differing models for speed up
                for (Model model : set) {
                    model.refresh();
                }
                setModelSet(set);
            } catch (IOException ex) {
                Log.error(this, ex);
                JOptionPane.showMessageDialog(null, "Файл поврежден или недопустимого формата");
            }
        } else {
            Log.debug(this, "Cancelled");
        }
    }

    private Constraint getConstraint(int gridX, int gridY, int gridWidth, int gridHeight) {
        return Constraint.create(gridX, gridY, gridWidth, gridHeight)
                .fill(GridBagConstraints.BOTH)
                .insets(5);
    }

    private class ColorIterator implements Iterator<Color> {
        private final Color[] colors = new Color[]{Color.BLUE, Color.DARK_GRAY, Color.MAGENTA, Color.YELLOW,
                Color.DARK_GRAY};

        private int index;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Color next() {
            return colors[index++ % colors.length];
        }
    }

    private class IntegerIterator implements Iterator<Integer> {
        private int index;

        IntegerIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Integer next() {
            return index++;
        }
    }
}
