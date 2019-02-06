package com.make.equo.aer.internal.api;

import com.google.gson.JsonObject;

public interface IEquoCrashReporter {

   /**
    * Logs an unexpected crash caught by Equo's status reporter which is an
    * extension of eclipse's
    * 
    * @param segmentation
    *                     tags to append to the log
    */

   public void logCrash(JsonObject segmentation);

   /**
    * Logs a point with data from logs done by the eclipse logger
    * 
    * @param segmentation
    *                     tags to append to the log
    */

   public void logEclipse(JsonObject segmentation);

}
