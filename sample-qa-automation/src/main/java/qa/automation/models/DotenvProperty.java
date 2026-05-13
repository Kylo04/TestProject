package qa.automation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import qa.automation.enums.Property;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DotenvProperty {
    private Property property;
    private String value;
}

