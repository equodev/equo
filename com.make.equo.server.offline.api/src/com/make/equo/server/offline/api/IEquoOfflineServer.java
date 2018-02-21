package com.make.equo.server.offline.api;

import java.io.IOException;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public interface IEquoOfflineServer {

	void save(HttpRequest originalRequest, HttpObject httpObject);

	HttpResponse getOfflineResponse(HttpRequest originalRequest) throws IOException;

}
