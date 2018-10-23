package codeone.stockwatch.cron;

import codeone.stockwatch.cron.impl.TimerCronService;

public interface PriceCheckCronService {
     void startCron();

     /**
      * Alternative to using the ServiceLoader.
      * This way you can still hide implementations, without using services.
      */
     static PriceCheckCronService getInstance() {
          return new TimerCronService();
     }
}
