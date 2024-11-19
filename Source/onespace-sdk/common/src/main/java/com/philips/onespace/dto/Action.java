package com.philips.onespace.dto;

import static com.philips.onespace.util.ErrorMessages.INVALID_ACTION_STATUS_ERR_CODE;
import static com.philips.onespace.util.ErrorMessages.INVALID_ACTION_TYPE_ERR_CODE;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.philips.onespace.validator.ValidDateFormat;
import com.philips.onespace.validator.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Validated
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Action {

    @JsonProperty("actionId")
    private UUID id;

    @JsonProperty("title")
    @NotBlank(message = "missing_action_title_param")
    @Size(min=3, max = 256, message= "size_limit")
    private String title;

    @JsonProperty("status")
    @ValidEnum(enumClass = ActionStatus.class, message = INVALID_ACTION_STATUS_ERR_CODE)
    private String status;

    @JsonProperty("initiator")
    @NotNull(message = "missing_action_initiator_param")
    private UUID initiator;

    @JsonProperty("potentialOwner")
    @NotNull(message = "missing_action_potential_owner_param")
    private List<UUID> potentialOwner;

    @JsonProperty("dateTime")
    @ValidDateFormat
    private String dateTime;

    @JsonProperty("type")
    @ValidEnum(enumClass = ActionType.class, message = INVALID_ACTION_TYPE_ERR_CODE)
    private String type;

    @JsonProperty("notify")
    private Boolean notify = Boolean.FALSE;

    @JsonProperty("message")
    private String message;

    @JsonProperty("source")
    @NotNull(message = "missing_action_source_param")
    private Source source;

    @JsonProperty("completedAtDateTime")
    @ValidDateFormat
    private String completedAtDateTime;


    @JsonProperty("dueDateTime")
    @ValidDateFormat
    private String dueDateTime;

    @JsonProperty("expiryDateTime")
    @ValidDateFormat
    private String expiryDateTime;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @JsonProperty("relatedObject")
    private String relatedObject;

}
