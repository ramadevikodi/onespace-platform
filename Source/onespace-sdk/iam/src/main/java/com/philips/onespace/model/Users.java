package com.philips.onespace.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@SuppressFBWarnings(value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class Users {

    @JsonProperty("Resources")
    private List<UserResource> resources;
}
