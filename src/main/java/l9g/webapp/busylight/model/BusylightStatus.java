/*
 * Copyright 2024 Thorsten Ludewig (t.ludewig@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package l9g.webapp.busylight.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Schema(description = "Represents the status of the Busylight device.")
public class BusylightStatus
{
  @Schema(description = "Unix timestamp (milliseconds) of the last status update.", example = "1678886400000") // Beispiel hinzugefügt
  private long timestamp;

  @Schema(description = "Indicates if the Busylight device is currently connected.", example = "true") // Beispiel hinzugefügt
  @Setter
  private boolean connected;

  @Schema(description = "Error message if any issues occurred (e.g., device not found, communication error).", example = "OK") // Beispiel hinzugefügt
  private String errorMessage;

  @Schema(description = "Status code. 0 indicates success, negative values represent errors.", example = "0") // Beispiel hinzugefügt
  @Setter
  private int status;

  @Schema(description = "The last Busylight command sent to the device.") // Beschreibung angepasst
  @Setter
  private BusylightCommand lastCommand;

  public void setErrorMessage(String errorMessage)
  {
    timestamp = System.currentTimeMillis();
    this.errorMessage = errorMessage;
  }

}
