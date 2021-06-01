/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.analytics.client.api;

import com.google.gson.JsonObject;

/**
 * Equo applications will call methods of this service to register custom events
 * data. For example, in our Netflix application example, let's assume we want
 * to know which countries were top watchers of the movie The Wolf Of Wall
 * Street. The application may register a call to the Analytics API service
 * whenever a movie is played, something like this:
 * 
 * {@code equoAnalytics.registerEvent("movies_summary", jsonObject)} or
 * {@code equoAnalytics.registerEvent("movies_summary", 1, jsonObject)} or
 * {@code equoAnalytics.registerEvent("movies_summary", 1, "{"title": "The Wolf
 * of Wall Street", "Country": "Germany"}")}
 * 
 */
public interface IAnalyticsApi {

  /**
   * Register an event in which the default value of mandatory field
   * {<code>count</code> is 1.
   * 
   * @param eventKey event key to register
   */
  public void registerEvent(String eventKey);

  /**
   * Register an event with segmentation in which the default value of the
   * mandatory field {<code>count</code> is 1.
   * 
   * @param eventKey     event key to register
   * @param segmentation segmentation
   */
  public void registerEvent(String eventKey, JsonObject segmentation);

  /**
   * Register an event with segmentation in which the default value of the
   * mandatory field {<code>count</code> is 1.
   * 
   * @param eventKey             event key to register
   * @param segmentationAsString segmentation
   */
  public void registerEvent(String eventKey, String segmentationAsString);

  public void registerEvent(String eventKey, int count);

  public void registerEvent(String eventKey, int count, JsonObject segmentation);

  public void registerEvent(String eventKey, int count, String segmentationAsString);

}
