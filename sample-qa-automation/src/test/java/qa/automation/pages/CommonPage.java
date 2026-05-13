package qa.automation.pages;

import com.azure.data.tables.models.TableEntity;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import okhttp3.Response;
import qa.automation.api.ApiClient;
import qa.automation.api.Endpoints;
import qa.automation.api.PayloadPaths;
import qa.automation.config.Config;
import qa.automation.helpers.comparors.Comparor;
import qa.automation.helpers.comparors.ModelComparator;
import qa.automation.redis.RedisClient;
import qa.automation.reporting.Reporter;
import qa.automation.reporting.context.TestContext;
import qa.automation.storage.AzureTableClient;
import qa.automation.utils.FileUtils;
import qa.automation.utils.JsonUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonPage extends BasePage {
    protected final Map<String, String> selectors = new HashMap<>();


    public CommonPage(Page page) {
        super(page);
        // Global/common selectors
        selectors.put("header-userMenu", "[data-testid='user-menu']");
        selectors.put("nav-search", "[data-testid='search']");
        selectors.put("btn-save", "[data-testid='btn-save']");
        selectors.put("category-hr", "[id = 'actions-category-hr']");
        selectors.put("category-dg", "[id = 'actions-category-dg']");
        selectors.put("refreshBtn", "#btn-refresh");
    }

    public Locator getLocByTypeAndText(String locType, String locText) {
        if (locType.equals("card")) return page.locator(String.format("//a[div//h5[text()='%s']]", locText));
        else return page.locator(String.format("//%s[normalize-space()='%s']", locType, locText));

    }

    @Override
    public String resolve(String key) {
        if (!selectors.containsKey(key)) throw new IllegalArgumentException("Unknown selector: " + key);
        return selectors.get(key);
    }

    public void clickRefreshBtn() {
        click(page.locator(selectors.get("refreshBtn")));
    }

    public void activateTrack(String supplier, String epDisplay, String epDistribution, String bpDisplay, String bpDistribution) {
        String templatePath = PayloadPaths.mainPayloadPath + PayloadPaths.TRACK_UPLOAD_JSON;
        String rawJson = FileUtils.read(templatePath);


        ApiClient api = new ApiClient();
        String finalPayload = JsonUtils.toJson("templateObj");
        Response res = api.post(Endpoints.UPLOAD_PATH, finalPayload);

        if (res.code() != 400 && res.code() != 500) {
            api.put(Endpoints.CONFIG_PATH, finalPayload);
        }
    }

    public void verifyLatestAzureTableEntry(String arg0, String arg1) {
        AzureTableClient table = new AzureTableClient(Config.table());
        List<TableEntity> returnedAzureTable = table.query(String.format("PartitionKey eq '%s'", "TestContext.get().eventId)"));

        //ordering descending by version
        Comparator<TableEntity> safeComparator = Comparator.comparing((TableEntity item) -> {
            Object rawValue = item.getProperty("version");

            if (rawValue == null) {
                return 0;
            }
            return (Integer) rawValue;
        });
        List<TableEntity> sortedDescAzureTables = returnedAzureTable.stream().sorted(safeComparator.reversed()).toList();

        if (arg0.contains(".")) {
            String[] list = arg0.split("\\.");
            String azureProperty = list[0];
            String valueInProperty = list[1];

            Object rawProperty = sortedDescAzureTables.getFirst().getProperty(azureProperty);
            Comparor.notNull(rawProperty, "The property '" + azureProperty + "' was null in the Azure Table");
            String valueToCheck = rawProperty.toString();
            Reporter.info("Azure Table Value (Nested): " + valueToCheck);
            String expectedSubstring = String.format("\"%s\":\"%s\"", valueInProperty, arg1);
            Reporter.info(expectedSubstring);
            Comparor.containsString(valueToCheck, expectedSubstring);
        } else {
            Object rawProperty = sortedDescAzureTables.getFirst().getProperty(arg0);
            Comparor.notNull(rawProperty, "The property '" + arg0 + "' was null in the Azure Table");
            String valueToCheck = rawProperty.toString();
            Reporter.info("Azure Table Value (Flat): " + arg0 + ":" + valueToCheck);
            Comparor.match(arg1, valueToCheck, "");
        }
    }

    public void verifyRpgEventStatusCache() {
        Reporter.info(JsonUtils.toJson("actualResult"));
        ModelComparator.compare("expectedStatus", "actualResult");
    }

    public void deleteEventData() {
        ApiClient apiClient = new ApiClient();
        apiClient.delete(String.format(Endpoints.DELETE_UPDATES, "TestContext.get().eventId"));
    }

}