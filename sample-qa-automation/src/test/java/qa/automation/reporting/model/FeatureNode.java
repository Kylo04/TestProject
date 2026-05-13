package qa.automation.reporting.model;

import java.util.ArrayList;
import java.util.List;

public class FeatureNode {
    public String name;
    public List<ScenarioNode> scenarios = new ArrayList<>();

    public Double durationSeconds() {
        return scenarios.stream()
                .mapToDouble(ScenarioNode::durationSeconds)
                .sum();
    }
}
