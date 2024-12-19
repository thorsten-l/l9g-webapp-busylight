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

import l9g.webapp.busylight.model.BusylightCommand;
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
@RestController
@RequestMapping(path = "/api/v1/busylight",
                produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class BusylightController
{
  private final BusylightService busylightService;

  @GetMapping("/color/{color}")
  public String color(
    @PathVariable(required = true, name = "color") String color)
  {
    log.debug("color: {}", color);
    busylightService.solidColor(color);
    return "{ \"status\":\"OK\"}";
  }

  @PostMapping("/command")
  public String command(
    @RequestBody BusylightCommand busylightCommand)
  {
    log.debug("command: {}", busylightCommand);
    busylightService.sendCommand(busylightCommand);
    return "{ \"status\":\"OK\"}";
  }

}
