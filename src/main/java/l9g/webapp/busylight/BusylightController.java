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
package l9g.webapp.busylight;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import l9g.webapp.busylight.model.BusylightCommand;
import l9g.webapp.busylight.model.BusylightStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@OpenAPIDefinition(info =
  @Info(
    title = "Busylight API Documentation",
    version = "1.0.0",
    description = "An API for educational purposes, dealing with a Luxafor Flag. "
    + "To minimize data transfer, only non-default attributes need to be sent in requests. "
    + "In the same way, responses will only include non-default attributes. "
    + "That means attributes with values of 0 or null will be omitted during data transfer.",
    license =
    @License(name = "Apache License Version 2.0 ",
             url = "https://www.apache.org/licenses/LICENSE-2.0.txt"),
    contact =
    @Contact(url = "https://github.com/thorsten-l/l9g-webapp-busylight",
             name = "Thorsten Ludewig",
             email = "t.ludewig@gmail.com")
  )
)
@RestController
@RequestMapping(path = "/api/v1/busylight",
                produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Busylight", description = "Controll Luxafor Flag")
public class BusylightController
{
  private final BusylightService busylightService;

  @Operation(summary = "Get Busylight status",
             description = "Retrieves the current status of the Busylight device, including connection status, error messages, and the last command sent.")
  @ApiResponses(value =
  {
    @ApiResponse(responseCode = "200",
                 description = "Successfully retrieved Busylight status",
                 content =
                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                          schema =
                          @Schema(implementation = BusylightStatus.class)))
  })
  @GetMapping("/status")
  public BusylightStatus status()
  {
    log.debug("status");
    return busylightService.getBusylightStatus();
  }

  @Operation(summary = "Set Busylight color",
             description = "Sets the Busylight to a solid color. Supported colors are: red, green, blue, cyan, magenta, yellow, white, and off.")
  @ApiResponses(value =
  {
    @ApiResponse(responseCode = "200",
                 description = "Successfully set the Busylight color",
                 content =
                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                          schema =
                          @Schema(implementation = BusylightStatus.class))),
    @ApiResponse(responseCode = "400", description = "Invalid color provided")
  })
  @GetMapping("/color/{color}")
  public BusylightStatus color(
    @Parameter(description = "Available colors are: red, green, blue, cyan, magenta, yellow, white and off",
               required = true)
    @PathVariable(required = true, name = "color") String color)
  {
    log.debug("color: {}", color);
    return busylightService.solidColor(color);
  }

  @Operation(summary = "Send custom command to Busylight",
             description = "Sends a custom command to the Busylight device. This allows for more advanced control, such as flashing patterns or specific lighting effects. See the Busylight documentation for available commands.")
  @ApiResponses(value =
  {
    @ApiResponse(responseCode = "200",
                 description = "Successfully sent the command to the Busylight",
                 content =
                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                          schema =
                          @Schema(implementation = BusylightStatus.class))),
    @ApiResponse(responseCode = "400",
                 description = "Invalid command provided")
  })
  @PostMapping("/command")
  public BusylightStatus command(
    @Parameter(description = "busylightCommand", required = true)
    @RequestBody BusylightCommand busylightCommand)
  {
    log.debug("command: {}", busylightCommand);
    return busylightService.sendCommand(busylightCommand);
  }

}
