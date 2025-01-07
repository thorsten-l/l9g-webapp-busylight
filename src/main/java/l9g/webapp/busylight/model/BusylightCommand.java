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

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Schema(description = "Represents a command sent to a Busylight device.")
public class BusylightCommand
{
    @Schema(description = "Command byte of the Busylight message.", type = "integer", format = "int32", minimum = "0", maximum = "255", example = "0")
    private int command;

    @Schema(description = "Code byte of the Busylight message. (e.g. 71='G' for green)", type = "integer", format = "int32", minimum = "0", maximum = "255", example = "71")
    private int code;

    @Schema(description = "Red color value (0-255).", type = "integer", format = "int32", minimum = "0", maximum = "255", example = "0")
    private int red;

    @Schema(description = "Green color value (0-255).", type = "integer", format = "int32", minimum = "0", maximum = "255", example = "0")
    private int green;

    @Schema(description = "Blue color value (0-255).", type = "integer", format = "int32", minimum = "0", maximum = "255", example = "0")
    private int blue;

    @Schema(description = "Advanced data byte 0 (purpose depends on the command).", type = "integer", format = "int32", minimum = "0", maximum = "255", example = "0")
    private int advanced0;

    @Schema(description = "Advanced data byte 1 (purpose depends on the command).", type = "integer", format = "int32", minimum = "0", maximum = "255", example = "0")
    private int advanced1;

    @Schema(description = "Advanced data byte 2 (purpose depends on the command).", type = "integer", format = "int32", minimum = "0", maximum = "255", example = "0")
    private int advanced2;


  @JsonIgnore
  public byte[] getMessage()
  {
    return new byte[]
    {
      (byte)(command & 0xff),
      (byte)(code & 0xff),
      (byte)(red & 0xff), (byte)(green & 0xff), (byte)(blue & 0xff),
      (byte)(advanced0 & 0xff), (byte)(advanced1 & 0xff), (byte)(advanced2 & 0xff)
    };
  }

  public static BusylightCommand solidColor(String colorName)
  {
    BusylightCommand bc = new BusylightCommand();
    bc.code = BusylightColor.fromName(colorName).getCode();
    return bc;
  }

}
