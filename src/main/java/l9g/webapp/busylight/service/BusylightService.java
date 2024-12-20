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
package l9g.webapp.busylight.service;

import l9g.webapp.busylight.model.BusylightCommand;
import l9g.webapp.busylight.model.BusylightStatus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.event.HidServicesEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Service
@Slf4j
public class BusylightService implements HidServicesListener
{
  private final static int MESSAGE_LENGTH = 8;

  private final int vendorId;

  private final int productId;

  @Getter
  private HidDevice busylightDevice;

  @Getter
  private BusylightStatus busylightStatus;

  public BusylightService(
    @Value("${usb-device.vendor-id:}") int vendorId,
    @Value("${usb-device.product-id:}") int productId
  )
  {
    this.busylightStatus = new BusylightStatus();
    busylightStatus.setErrorMessage("busylight disconnected");
    busylightStatus.setStatus(-4);

    this.vendorId = vendorId;
    this.productId = productId;
    this.busylightDevice = null;

    HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
    hidServicesSpecification.setAutoStart(false);
    HidServices hidServices = HidManager.getHidServices(hidServicesSpecification);
    hidServices.addHidServicesListener(this);
    hidServices.start();
  }

  public synchronized BusylightStatus sendCommand(BusylightCommand command)
  {
    busylightStatus.setLastCommand(command);

    if(busylightDevice != null && command != null)
    {
      if(busylightDevice.isClosed() &&  ! busylightDevice.open())
      {
        busylightStatus.setErrorMessage("Could not open device");
        busylightStatus.setStatus(-1);
        log.error("Could not open device");
      }
      else
      {
        busylightStatus.setErrorMessage("OK");
        busylightStatus.setStatus(0);
        busylightDevice.write(command.getMessage(), MESSAGE_LENGTH, (byte)0);
      }
    }
    else
    {
      busylightStatus.setErrorMessage("device not connected or command == null");
      busylightStatus.setStatus(-2);
      log.error("device not connected orcommand == null");
    }

    return busylightStatus;
  }

  public BusylightStatus solidColor(String name)
  {
    log.debug("solid color : {}", name);
    sendCommand(BusylightCommand.solidColor(name));
    return busylightStatus;
  }

  @Override
  public void hidDeviceAttached(HidServicesEvent hse)
  {
    HidDevice device = hse.getHidDevice();
    log.trace("hidDeviceAttached: {}", hse.getHidDevice().getProduct());
    if(device != null
      && device.getVendorId() == vendorId
      && device.getProductId() == productId)
    {
      log.debug("busylight connected");
      synchronized(this)
      {
        busylightDevice = device;
        busylightStatus.setConnected(true);
        busylightStatus.setErrorMessage("OK");
        busylightStatus.setStatus(0);
      }
    }
  }

  @Override
  public void hidDeviceDetached(HidServicesEvent hse)
  {
    log.trace("hidDeviceDetached: {}", hse.getHidDevice().getProduct());
    HidDevice device = hse.getHidDevice();
    if(device != null
      && device.getVendorId() == vendorId
      && device.getProductId() == productId)
    {
      log.debug("busylight disconnected");
      synchronized(this)
      {
        busylightDevice = null;
        busylightStatus.setConnected(false);
        busylightStatus.setErrorMessage("busylight disconnected");
        busylightStatus.setStatus(-3);
      }
    }
  }

  @Override
  public void hidFailure(HidServicesEvent hse)
  {
    log.error("failure with {} usb device", hse.getHidDevice().getProduct());
  }

  @Override
  public void hidDataReceived(HidServicesEvent hse)
  {
  }

}
