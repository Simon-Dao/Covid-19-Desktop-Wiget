package gui.widget;

import config.ConfigModel;
import config.ConfigurationService;
import datafetch.CountryData;
import datafetch.CovidDataModel;
import datafetch.DataProviderService;
import datafetch.GlobalData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WidgetController implements Initializable {

    private ScheduledExecutorService executorService;
    private ConfigModel configModel;

    @FXML
    public AnchorPane rootPane;
    @FXML
    public Text textGlobalReport, textGlobalCode, textCountryReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initScheduler();
        initExitMenu();
    }

    private void initScheduler() {
        updateConfig();
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(this::loadData, 0, configModel.getRefreshRateInSeconds(), TimeUnit.SECONDS);
    }

    private void loadData() {
        updateConfig();
        DataProviderService dataProviderService = new DataProviderService();
        CovidDataModel covidDataModel = dataProviderService.getData(configModel.getCountryName());
        textGlobalCode.setText(configModel.getCountryCode());

        Platform.runLater(() -> {
            inflateData(covidDataModel);
        });
    }

    private void inflateData(CovidDataModel cdm) {

        GlobalData globalData = cdm.getGlobalData();
        CountryData countryData = cdm.getCountryData();
        textGlobalReport.setText(getFormattedData(globalData.getCases(), globalData.getRecovered(), globalData.getDeaths()));
        textCountryReport.setText(getFormattedData(countryData.cases, countryData.recovered, countryData.deaths));
        textGlobalCode.setText(configModel.getCountryCode());
    }

    private String getFormattedData(long totalCases, long recoveredCases, long totalDeaths) {
        return String.format("Cases: %-8s | Recovered: %-6s | Deaths: %-6s"
                ,addCommas(totalCases),addCommas(recoveredCases),addCommas(totalDeaths));
    }

    private String addCommas(long number) {
        String num = String.valueOf(number);
        StringBuilder sb = new StringBuilder();

        int c = 0;
        for(int i = num.length() - 1; i>=0; i--) {
            sb.insert(0,num.charAt(i));
            c++;
            if(c % 3 == 0 && c != 0 && c != num.length()) {
                sb.insert(0,",");
            }
        }

        return sb.toString();
    }

    private void updateConfig() {
        try {
            configModel = new ConfigurationService().getConfiguration();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void initExitMenu() {
        MenuItem exitItem = new MenuItem("EXIT");
        exitItem.setOnAction(event -> {
            System.exit(0);
        });

        MenuItem refreshItem = new MenuItem("REFRESH");
        refreshItem.setOnAction(event -> {
            System.out.println("refreshing");
            executorService.schedule(this::loadData, 0, TimeUnit.SECONDS);
        });

        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(exitItem, refreshItem);
        rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED, mouse -> {
            if(mouse.isSecondaryButtonDown()) {
                contextMenu.show(rootPane, mouse.getScreenX(),mouse.getScreenY());
            } else {
                if(contextMenu.isShowing())
                    contextMenu.hide();
            }
        });
    }
}
