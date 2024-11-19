package com.philips.onespace.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OneSpaceLogger {

    private String operation;
    private String status;
    private int statusCode;
    private String method;
    private String url;
    private Map<String, String> headers;
    private Map<String, String> requestParameters;
    private Object body;
    private String totalTimeTaken;

    public enum StatusLogEnum {
        INITIATED("initiated"),
        ERROR("error"),
        SUCCEEDED("succeeded");

        private String value;

        StatusLogEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static StatusLogEnum fromValue(String text) {
            for (StatusLogEnum b : StatusLogEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

}
