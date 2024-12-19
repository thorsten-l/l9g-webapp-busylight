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
public class BusylightCommand
{
  private byte command;

  private byte code;

  private byte red;

  private byte green;

  private byte blue;

  private byte advanced0;

  private byte advanced1;

  private byte advanced2;

  public byte[] getMessage()
  {
    return new byte[]
    {
      command,
      code,
      red, green, blue,
      advanced0, advanced1, advanced2
    };
  }

  public static BusylightCommand solidColor( String colorName )
  {
    BusylightCommand bc = new BusylightCommand();
    bc.code = BusylightColor.fromName(colorName).getCode();
    return bc;
  }
}
