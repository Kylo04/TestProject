package qa.automation.storage;

import com.azure.data.tables.*;
import com.azure.data.tables.models.*;
import qa.automation.config.Config;

import java.util.ArrayList;
import java.util.List;

public class AzureTableClient {
  private final TableClient client;

  public AzureTableClient(String tableName) {
    TableServiceClient svc = new TableServiceClientBuilder()
        .connectionString(Config.tablesConn())
        .buildClient();
    client = svc.getTableClient(tableName);
  }

  public TableEntity get(String partitionKey, String rowKey) {
    return client.getEntity(partitionKey, rowKey);
  }

  public List<TableEntity> query(String filter) {
    List<TableEntity> list = new ArrayList<>();
    client.listEntities(new ListEntitiesOptions().setFilter(filter), null, null)
        .forEach(list::add);
    return list;
  }

  public List<TableEntity> all() {
    List<TableEntity> list = new ArrayList<>();
    client.listEntities().forEach(list::add);
    return list;
  }
}