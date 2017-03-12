package com.sbutterfly.gui;

import com.sbutterfly.utils.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Sergei on 31.01.2015.
 */
public class MenuView extends JMenuBar {

    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;

    private JMenuItem settingsItem;

    private JMenuItem helpItem;
    private JMenuItem creditsItem;

    public MenuView() {
        createGUI();
    }

    private void createGUI() {
        Font font = new Font("Verdana", Font.PLAIN, 11);

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        JMenu fileMenu = new JMenu("Файл");
        fileMenu.setFont(font);

        newItem = new JMenuItem("Новый");
        newItem.setFont(font);
        fileMenu.add(newItem);

        openItem = new JMenuItem("Открыть");
        openItem.setFont(font);
        fileMenu.add(openItem);

        saveItem = new JMenuItem("Сохранить");
        saveItem.setFont(font);
        fileMenu.add(saveItem);

        add(fileMenu);

        JMenu settingsMenu = new JMenu("Настройки");
        settingsMenu.setFont(font);

        settingsItem = new JMenuItem("Настройки интегрирования");
        settingsItem.setFont(font);
        settingsMenu.add(settingsItem);

        add(settingsMenu);

        JMenu aboutMenu = new JMenu("О программе");
        aboutMenu.setFont(font);

        helpItem = new JMenuItem("Руководство пользователя");
        helpItem.setFont(font);
        aboutMenu.add(helpItem);

        creditsItem = new JMenuItem("О разработчиках");
        creditsItem.setFont(font);
        aboutMenu.add(creditsItem);

        add(aboutMenu);

        Log.debug(this, "GUI was created");
    }

    public void addNewActionListener(ActionListener listener) {
        newItem.addActionListener(listener);
    }

    public void addSaveActionListener(ActionListener listener) {
        saveItem.addActionListener(listener);
    }

    public void addOpenActionListener(ActionListener listener) {
        openItem.addActionListener(listener);
    }


    public void addSettingsActionListener(ActionListener listener) {
        settingsItem.addActionListener(listener);
    }


    public void addHelpActionListener(ActionListener listener) {
        helpItem.addActionListener(listener);
    }

    public void addCreditsActionListener(ActionListener listener) {
        creditsItem.addActionListener(listener);
    }
}
