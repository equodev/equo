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

window.equo = window.equo || {};

(function (equo) {
  const VOLUME = 0.2;

  let getCurrentMediaTitleFunction;
  let mediaType;

  checkMediaType = () => {
    if (typeof mediaType === 'undefined') {
      throw 'Media type is not defined. The method equo.setMediaType must be call first and the media type must be \'audio\' or \'video\'';
    }
  };

  equo.on('playSelectedMedia', () => {
    checkMediaType();
    let currentVideo = document.getElementsByTagName(mediaType)[0];
    if (currentVideo) {
      if (currentVideo.paused) {
        currentVideo.play();
      } else {
        currentVideo.pause();
      }
    }
  });

  equo.on('turnUpVolume', () => {
    checkMediaType();
    let currentVideo = document.getElementsByTagName(mediaType)[0];
    if (currentVideo) {
      currentVideo.volume += VOLUME;
    }
  });

  equo.on('turnDownVolume', () => {
    checkMediaType();
    let currentVideo = document.getElementsByTagName(mediaType)[0];
    if (currentVideo) {
      currentVideo.volume -= VOLUME;
    }
  });

  /**
   * Useful to track data of the Equo application, for example for analytics.
   * 
   * @param {*} getMediaTitleFunction 
   */
  equo.setGetMediaTitleFunction = (getMediaTitleFunction) => {
    getCurrentMediaTitleFunction = getMediaTitleFunction;
  };

  /**
   * Play the video whenever a click ocurs on the dom Element.
   * 
   * @param {*} event 
   * @param {*} domElement 
   */
  equo.playVideoWhenClick = (domElement) => {
    $(document).on('click', domElement, function (event) {
      equo.logDebug('this is... ' + JSON.stringify(this));
      if (typeof getCurrentMediaTitleFunction !== 'undefined') {
        let mediaTitle = getCurrentMediaTitleFunction(this);
        equo.logDebug('media playing is... ' + mediaTitle);
        event.preventDefault();
		if (equo.registerEvent) {
	        equo.registerEvent({
	          key: 'movies_played',
	          segmentation: {
	            title: mediaTitle,
	            country: "Germany"
	          }
	        });
		}
      }
    });
  };

  /**
   * Play the audio whenever a click ocurs on the dom Element.
   * 
   * @param {*} event 
   * @param {*} domElement 
   */
  equo.playAudioWhenClick = (domElement) => {
    equo.playVideoWhenClick(domElement);
  };

  /**
   * Sets the type of the media you want to reproduce. It must be 'video' or 'audio'
   * 
   * @param {*} type 
   */
  equo.setMediaType = (type) => {
    if (type !== 'video' && type !== 'audio') {
      throw 'Media type must be \'audio\' or \'video\'';
    }
    mediaType = type;
  };

}(equo));
