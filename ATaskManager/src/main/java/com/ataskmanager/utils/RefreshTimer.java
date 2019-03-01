package com.ataskmanager.utils;

import com.ataskmanager.ATaskManager;

import java.util.Date;
import java.util.TimerTask;

/**       Run Timer for UI Refresh
 * @author Adam Combs
 * @author Seth Wampole
 */
public class RefreshTimer extends TimerTask {

          /**
           *        Refreshes main stage UI
           */
          @Override
          public void run(){
                    ATaskManager.getATMController().refreshUI();
          }

}
