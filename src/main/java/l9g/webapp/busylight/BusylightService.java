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

  public BusylightService(
    @Value("${usb-device.vendor-id:}") int vendorId,
    @Value("${usb-device.product-id:}") int productId
  )
  {
    this.vendorId = vendorId;
    this.productId = productId;
    this.busylightDevice = null;

    HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
    hidServicesSpecification.setAutoStart(false);
    HidServices hidServices = HidManager.getHidServices(hidServicesSpecification);
    hidServices.addHidServicesListener(this);
    hidServices.start();
  }

  public synchronized int sendCommand(BusylightCommand command)
  {
    if(busylightDevice != null && command != null)
    {
      if(busylightDevice.isClosed() &&  ! busylightDevice.open())
      {
        log.error("Could not open device");
        return -1;
      }

      return busylightDevice.write(command.getMessage(), MESSAGE_LENGTH, (byte)0);
    }
    log.error("device not connected or wrong message length");
    return -2;
  }

  public void solidColor(String name)
  {
    log.debug("solid color : {}", name);
    sendCommand(BusylightCommand.solidColor(name));
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
